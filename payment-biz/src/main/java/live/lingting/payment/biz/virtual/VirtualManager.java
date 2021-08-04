package live.lingting.payment.biz.virtual;

import live.lingting.payment.exception.PaymentException;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import live.lingting.payment.biz.Redis;
import live.lingting.payment.biz.config.PaymentConfig;
import live.lingting.payment.biz.service.PayService;
import live.lingting.payment.biz.service.VirtualAddressService;
import live.lingting.payment.entity.Pay;
import live.lingting.payment.entity.Project;
import live.lingting.payment.enums.ResponseCode;
import live.lingting.payment.sdk.enums.PayStatus;
import live.lingting.payment.sdk.model.MixVirtualPayModel;
import live.lingting.payment.sdk.model.MixVirtualRetryModel;
import live.lingting.payment.sdk.model.MixVirtualSubmitModel;
import live.lingting.payment.sdk.response.MixVirtualPayResponse;
import live.lingting.payment.sdk.response.MixVirtualRetryResponse;
import live.lingting.payment.sdk.util.MixUtils;

/**
 * @author lingting 2021/6/7 22:48
 */
@Getter
@Component
@RequiredArgsConstructor
public class VirtualManager {

	private final Redis redis;

	private final PayService payService;

	private final VirtualAddressService virtualAddressService;

	private final PaymentConfig config;

	/**
	 * 虚拟货币预下单
	 *
	 * @author lingting 2021-06-07 22:50
	 */
	@SneakyThrows
	@Transactional(rollbackFor = Exception.class)
	public MixVirtualPayResponse.Data pay(MixVirtualPayModel model, Project project) {
		final Pay pay = payService.virtualCreate(model, project);
		final MixVirtualPayResponse.Data data = new MixVirtualPayResponse.Data();
		data.setAddress(pay.getAddress());
		data.setExpireTime(LocalDateTime.now().plusMinutes(config.getVirtualSubmitTimeout()));
		data.setTradeNo(pay.getTradeNo());
		return data;
	}

	@SneakyThrows
	@Transactional(rollbackFor = Exception.class)
	public void submit(MixVirtualSubmitModel model) {
		Pay pay = payService.getByNo(model.getTradeNo(), model.getProjectTradeNo());
		// 非虚拟支付 或者 非等待支付状态
		if (!pay.getCurrency().getVirtual() || !PayStatus.WAIT.equals(pay.getStatus())) {
			throw new PaymentException(ResponseCode.HASH_DISABLED);
		}
		model.setHash(MixUtils.clearHash(model.getHash()));
		if (!MixUtils.validHash(pay.getChain(), model.getHash())) {
			throw new PaymentException(ResponseCode.HASH_ERROR);
		}
		if (!payService.virtualSubmit(pay, model.getHash())) {
			throw new PaymentException(ResponseCode.HASH_SUBMIT_FAIL);
		}
	}

	@SneakyThrows
	@Transactional(rollbackFor = Exception.class)
	public MixVirtualRetryResponse.Data retry(MixVirtualRetryModel model) {
		Pay pay = payService.getByNo(model.getTradeNo(), model.getProjectTradeNo());
		// 非失败支付 或 重试结束时间小于等于当前时间
		if (!PayStatus.RETRY.equals(pay.getStatus()) || pay.getRetryEndTime().compareTo(LocalDateTime.now()) < 1) {
			throw new PaymentException(ResponseCode.RETRY_DISABLES);
		}
		// 更新hash, 且 hash校验不通过
		if (StringUtils.hasText(model.getHash()) && !MixUtils.validHash(pay.getChain(), model.getHash())) {
			throw new PaymentException(ResponseCode.HASH_ERROR);
		}
		if (!payService.virtualRetry(pay, model.getHash())) {
			throw new PaymentException(ResponseCode.RETRY_FAIL);
		}
		final MixVirtualRetryResponse.Data data = new MixVirtualRetryResponse.Data();
		data.setTradeNo(pay.getTradeNo());
		data.setRetryEndTime(pay.getRetryEndTime());
		return data;
	}

	@SneakyThrows
	@Transactional(rollbackFor = Exception.class)
	public void success(Pay pay) {
		if (payService.success(pay)) {
			virtualAddressService.unlock(pay.getAddress());
		}
	}

	@Transactional(rollbackFor = Exception.class)
	public void fail(Pay pay, String desc, Long minutes) {
		LocalDateTime retryEndTime = minutes == null ? null : LocalDateTime.now().plusMinutes(minutes);

		if (payService.fail(pay, desc, retryEndTime) && retryEndTime == null) {
			virtualAddressService.unlock(pay.getAddress());
		}
	}

}
