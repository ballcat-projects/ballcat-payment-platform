package live.lingting.payment.biz.service.impl;

import static live.lingting.payment.enums.ResponseCode.PAY_NOT_FOUND;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import live.lingting.payment.Page;
import live.lingting.payment.biz.Redis;
import live.lingting.payment.biz.config.PaymentConfig;
import live.lingting.payment.biz.mapper.PayMapper;
import live.lingting.payment.biz.service.PayService;
import live.lingting.payment.biz.service.VirtualAddressService;
import live.lingting.payment.constant.PayConstants;
import live.lingting.payment.entity.Pay;
import live.lingting.payment.entity.Project;
import live.lingting.payment.entity.VirtualAddress;
import live.lingting.payment.enums.ResponseCode;
import live.lingting.payment.exception.PaymentException;
import live.lingting.payment.sdk.enums.Currency;
import live.lingting.payment.sdk.enums.Mode;
import live.lingting.payment.sdk.enums.NotifyStatus;
import live.lingting.payment.sdk.enums.PayStatus;
import live.lingting.payment.sdk.model.MixVirtualPayModel;
import live.lingting.payment.vo.PayVO;

/**
 * @author lingting 2021/6/4 13:40
 */
@Service
@RequiredArgsConstructor
public class PayServiceImpl extends ServiceImpl<PayMapper, Pay> implements PayService {

	private final Redis redis;

	private final VirtualAddressService virtualAddressService;

	private final PaymentConfig config;

	@Override
	public Page<Pay> list(Page<Pay> page, Pay pay) {
		return baseMapper.list(page, pay);
	}

	@Override
	public Page<PayVO> listVo(Page<Pay> page, Pay qo) {
		IPage<PayVO> iPage = baseMapper.listVo(page.toPage(), baseMapper.getWrapper(qo));
		return new Page<>(iPage.getRecords(), iPage.getTotal());
	}

	@Override
	public List<Pay> list(Pay pay) {
		return baseMapper.selectList(baseMapper.getWrapper(pay));
	}

	@Override
	public List<Pay> listVirtualTimeout(LocalDateTime maxTime) {
		return baseMapper.listVirtualTimeout(maxTime);
	}

	@Override
	public List<Pay> listVirtualRetryTimeout() {
		return baseMapper.listVirtualRetryTimeout();
	}

	@Override
	public List<Pay> listNotify() {
		return baseMapper.listNotify();
	}

	@Override
	public Pay getByNo(String tradeNo, String projectTradeNo) throws PaymentException {
		Pay pay = new Pay();

		if (StringUtils.hasText(tradeNo)) {
			pay.setTradeNo(tradeNo);
		}
		else {
			pay.setProjectTradeNo(projectTradeNo);
		}

		pay = baseMapper.selectOne(baseMapper.getWrapper(pay));

		if (pay == null) {
			throw new PaymentException(PAY_NOT_FOUND);
		}

		return pay;
	}

	@Override
	public long count(Pay pay) {
		return baseMapper.selectCount(baseMapper.getWrapper(pay));
	}

	@Override
	public Pay virtualCreate(MixVirtualPayModel model, Project project) throws PaymentException {
		VirtualAddress va = virtualAddressService.lock(model, project);

		if (va == null) {
			throw new PaymentException(ResponseCode.NO_ADDRESS_AVAILABLE);
		}
		Pay pay = new Pay().setAddress(va.getAddress()).setChain(model.getChain())
				.setCurrency(model.getContract().getCurrency()).setNotifyUrl(model.getNotifyUrl())
				.setProjectTradeNo(model.getProjectTradeNo());
		create(project, pay);
		return pay;
	}

	private void create(Project project, Pay pay) throws PaymentException {
		String key = PayConstants.getProjectTradeNoLock(project.getId(), pay.getProjectTradeNo());

		if (!redis.setIfAbsent(key, "", TimeUnit.DAYS.toSeconds(1))) {
			throw new PaymentException(ResponseCode.PROJECT_NO_REPEAT);
		}

		// 上锁成功, 从数据库查询
		if (count(new Pay().setProjectTradeNo(pay.getProjectTradeNo())) > 0) {
			throw new PaymentException(ResponseCode.PROJECT_NO_REPEAT);
		}

		pay.setProjectId(project.getId()).setStatus(PayStatus.WAIT);
		try {
			if (!save(pay)) {
				throw new PaymentException(ResponseCode.PAY_SAVE_FAIL);
			}
		}
		catch (Exception e) {
			redis.delete(key);
			throw e;
		}
	}

	@Override
	public boolean virtualSubmit(Pay pay, String hash) throws PaymentException {
		validateThirdTradeNo(pay, hash);
		return baseMapper.virtualSubmit(pay.getTradeNo(), hash);
	}

	@Override
	public boolean virtualRetry(Pay pay, String hash) throws PaymentException {
		// 第二次请求的hash与第一次提交的一致
		if (hash.equals(pay.getThirdPartTradeNo())) {
			hash = "";
		}
		if (StringUtils.hasText(hash)) {
			validateThirdTradeNo(pay, hash);
		}
		return baseMapper.virtualRetry(pay.getTradeNo(), hash);
	}

	@Override
	public boolean virtualRetrySubmit(Pay pay, String hash) throws PaymentException {
		// 第二次请求的hash与第一次提交的一致
		if (hash.equals(pay.getThirdPartTradeNo())) {
			hash = "";
		}
		if (StringUtils.hasText(hash)) {
			validateThirdTradeNo(pay, hash);
		}

		return baseMapper.virtualRetrySubmit(pay.getTradeNo(), hash);
	}

	@Override
	public boolean notifying(Pay pay) {
		return baseMapper.notifying(pay);
	}

	@Override
	public void validateThirdTradeNo(Pay pay, String thirdTradeNo) throws PaymentException {
		String key = PayConstants.getPayThirdTradeNoLock(pay, thirdTradeNo);
		if (!redis.setIfAbsent(key, "", TimeUnit.DAYS.toSeconds(1))) {
			throw new PaymentException(ResponseCode.TRADE_NO_EXIST);
		}

		final LambdaQueryWrapper<Pay> wrapper = Wrappers.<Pay>lambdaQuery()
				// thirdTradeNo
				.eq(Pay::getThirdPartTradeNo, thirdTradeNo)
				// 限制状态
				.in(Pay::getStatus, PayStatus.WAIT, PayStatus.RETRY, PayStatus.SUCCESS);

		if (baseMapper.selectCount(wrapper) > 0) {
			throw new PaymentException(ResponseCode.TRADE_NO_EXIST);
		}
	}

	@Override
	public boolean fail(Pay pay, String desc, LocalDateTime retryEndTime) {
		final boolean fail = baseMapper.fail(pay, desc, retryEndTime);
		if (fail) {
			// 失败移除hash锁
			final String key = PayConstants.getPayThirdTradeNoLock(pay, pay.getThirdPartTradeNo());
			redis.delete(key);
		}
		return fail;
	}

	@Override
	public boolean success(Pay pay) {
		return baseMapper.success(pay);
	}

	@Override
	public void notifyComplete(Pay pay, NotifyStatus status) {
		baseMapper.notifyComplete(pay, status);
	}

	@Override
	public void notifyWait(Pay pay) {
		baseMapper.notifyWait(pay);
	}

	@Override
	public void forciblyRetry(String tradeNo, String projectTradeNo) throws PaymentException {
		Pay pay = getByNo(tradeNo, projectTradeNo);
		if (pay == null || !Currency.USDT.equals(pay.getCurrency()) || !PayStatus.WAIT.equals(pay.getStatus())
				|| !StringUtils.hasText(pay.getThirdPartTradeNo())) {
			throw new PaymentException(ResponseCode.PROHIBIT_OPERATION);
		}

		if (!baseMapper.forciblyRetry(pay.getTradeNo(), config.getRetryTimeout())) {
			throw new PaymentException(ResponseCode.OPERATION_FAILED);
		}
	}

	@Override
	public void forciblyFail(String tradeNo, String projectTradeNo) throws PaymentException {
		Pay pay = getByNo(tradeNo, projectTradeNo);
		final boolean prohibit = pay == null
				|| (!PayStatus.WAIT.equals(pay.getStatus()) && !PayStatus.RETRY.equals(pay.getStatus()));
		if (prohibit) {
			throw new PaymentException(ResponseCode.PROHIBIT_OPERATION);
		}

		if (!baseMapper.forciblyFail(pay.getTradeNo())) {
			throw new PaymentException(ResponseCode.OPERATION_FAILED);
		}
	}

	@Override
	public List<Pay> listWaitTransfer() {
		final LambdaQueryWrapper<Pay> wrapper = Wrappers.<Pay>lambdaQuery()
				// 限制支付模式
				.eq(Pay::getMode, Mode.TRANSFER)
				// 状态为等待
				.eq(Pay::getStatus, PayStatus.WAIT);
		return baseMapper.selectList(wrapper);
	}

	@Override
	public List<Pay> listRealExpire(LocalDateTime maxTime) {
		final LambdaQueryWrapper<Pay> wrapper = Wrappers.<Pay>lambdaQuery()
				// 限制支付模式
				.eq(Pay::getMode, Mode.QR)
				// 状态为等待
				.eq(Pay::getStatus, PayStatus.WAIT)
				// 限制货币为真实货币
				.in(Pay::getCurrency, Currency.REAL_LIST)
				// 时间
				.le(Pay::getCreateTime, maxTime);
		return baseMapper.selectList(wrapper);
	}

}
