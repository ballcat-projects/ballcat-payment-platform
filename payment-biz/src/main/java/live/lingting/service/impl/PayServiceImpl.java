package live.lingting.service.impl;

import static live.lingting.enums.ResponseCode.PAY_NOT_FOUND;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.hccake.ballcat.common.core.exception.BusinessException;
import com.hccake.ballcat.common.model.domain.PageResult;
import com.hccake.extend.mybatis.plus.service.impl.ExtendServiceImpl;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import live.lingting.Page;
import live.lingting.Redis;
import live.lingting.constant.PayConstants;
import live.lingting.entity.Pay;
import live.lingting.entity.Project;
import live.lingting.entity.VirtualAddress;
import live.lingting.enums.ResponseCode;
import live.lingting.mapper.PayMapper;
import live.lingting.sdk.enums.Currency;
import live.lingting.sdk.enums.NotifyStatus;
import live.lingting.sdk.enums.PayStatus;
import live.lingting.sdk.model.MixVirtualPayModel;
import live.lingting.service.PayService;
import live.lingting.service.VirtualAddressService;
import live.lingting.virtual.VirtualConfig;

/**
 * @author lingting 2021/6/4 13:40
 */
@Service
@RequiredArgsConstructor
public class PayServiceImpl extends ExtendServiceImpl<PayMapper, Pay> implements PayService {

	private final Redis redis;

	private final VirtualAddressService virtualAddressService;

	private final VirtualConfig config;

	@Override
	public PageResult<Pay> list(Page<Pay> page, Pay pay) {
		return baseMapper.list(page, pay);
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
	public Pay getByNo(String tradeNo, String projectTradeNo) {
		Pay pay = new Pay();

		if (StringUtils.hasText(tradeNo)) {
			pay.setTradeNo(tradeNo);
		}
		else {
			pay.setProjectTradeNo(projectTradeNo);
		}

		pay = baseMapper.selectOne(baseMapper.getWrapper(pay));

		if (pay == null) {
			throw new BusinessException(PAY_NOT_FOUND);
		}

		return pay;
	}

	@Override
	public long count(Pay pay) {
		return baseMapper.selectCount(baseMapper.getWrapper(pay));
	}

	@Override
	public Pay virtualCreate(MixVirtualPayModel model, Project project) {
		VirtualAddress va = virtualAddressService.lock(model);

		if (va == null) {
			throw new BusinessException(ResponseCode.NO_ADDRESS_AVAILABLE);
		}
		Pay pay = new Pay().setAddress(va.getAddress()).setChain(model.getChain())
				.setCurrency(model.getContract().getCurrency()).setNotifyUrl(model.getNotifyUrl())
				.setProjectTradeNo(model.getProjectTradeNo());
		create(project, pay);
		return pay;
	}

	private void create(Project project, Pay pay) {
		String key = PayConstants.getProjectTradeNoLock(project.getId(), pay.getProjectTradeNo());

		if (!redis.setIfAbsent(key, "", TimeUnit.DAYS.toSeconds(1))) {
			throw new BusinessException(ResponseCode.PROJECT_NO_REPEAT);
		}

		// 上锁成功, 从数据库查询
		if (count(new Pay().setProjectTradeNo(pay.getProjectTradeNo())) > 0) {
			throw new BusinessException(ResponseCode.PROJECT_NO_REPEAT);
		}

		pay.setProjectId(project.getId()).setStatus(PayStatus.WAIT);
		try {
			if (!save(pay)) {
				throw new BusinessException(ResponseCode.PAY_SAVE_FAIL);
			}
		}
		catch (Exception e) {
			redis.delete(key);
			throw e;
		}
	}

	@Override
	public boolean virtualSubmit(Pay pay, String hash) {
		validateHash(pay, hash);

		return baseMapper.virtualSubmit(pay.getTradeNo(), hash);
	}

	@Override
	public boolean virtualRetry(Pay pay, String hash) {
		// 第二次请求的hash与第一次提交的一致
		if (hash.equals(pay.getThirdPartTradeNo())) {
			hash = "";
		}
		if (StringUtils.hasText(hash)) {
			validateHash(pay, hash);
		}
		return baseMapper.virtualRetry(pay.getTradeNo(), hash);
	}

	@Override
	public boolean notifying(Pay pay) {
		return baseMapper.notifying(pay);
	}

	private void validateHash(Pay pay, String hash) {
		String key = PayConstants.getVirtualHashLock(pay.getChain(), hash);
		if (!redis.setIfAbsent(key, "", TimeUnit.DAYS.toSeconds(1))) {
			throw new BusinessException(ResponseCode.HASH_EXIST);
		}

		final LambdaQueryWrapper<Pay> wrapper = Wrappers.<Pay>lambdaQuery()
				// hash
				.eq(Pay::getThirdPartTradeNo, hash)
				// 限制状态
				.in(Pay::getStatus, PayStatus.WAIT, PayStatus.SUCCESS);

		if (baseMapper.selectCount(wrapper) > 0) {
			throw new BusinessException(ResponseCode.HASH_EXIST);
		}
	}

	@Override
	public boolean fail(Pay pay, String desc, LocalDateTime retryEndTime) {
		final boolean fail = baseMapper.fail(pay, desc, retryEndTime);
		if (fail) {
			// 失败移除hash锁
			final String key = PayConstants.getVirtualHashLock(pay.getChain(), pay.getThirdPartTradeNo());
			redis.delete(key);
		}
		return fail;
	}

	@Override
	public boolean success(Pay pay) {
		return baseMapper.success(pay.getTradeNo(), pay.getAmount());
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
	public void forciblyRetry(String tradeNo) {
		Pay pay = getById(tradeNo);
		if (!pay.getCurrency().equals(Currency.USDT) || !pay.getStatus().equals(PayStatus.WAIT)
				|| !StringUtils.hasText(pay.getThirdPartTradeNo())) {
			throw new BusinessException(ResponseCode.PROHIBIT_OPERATION);
		}

		if (!baseMapper.forciblyRetry(tradeNo, config.getRetryTimeout())) {
			throw new BusinessException(ResponseCode.OPERATION_FAILED);
		}
	}

	@Override
	public void forciblyFail(String tradeNo) {
		Pay pay = getById(tradeNo);
		if (!pay.getStatus().equals(PayStatus.WAIT) && !pay.getStatus().equals(PayStatus.RETRY)) {
			throw new BusinessException(ResponseCode.PROHIBIT_OPERATION);
		}

		if (!baseMapper.forciblyFail(tradeNo)) {
			throw new BusinessException(ResponseCode.OPERATION_FAILED);
		}
	}

}
