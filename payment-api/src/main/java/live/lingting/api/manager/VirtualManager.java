package live.lingting.api.manager;

import com.hccake.ballcat.common.core.exception.BusinessException;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import live.lingting.Redis;
import live.lingting.api.ApiConfig;
import live.lingting.entity.Pay;
import live.lingting.entity.Project;
import live.lingting.enums.ResponseCode;
import live.lingting.sdk.enums.PayStatus;
import live.lingting.sdk.model.MixVirtualPayModel;
import live.lingting.sdk.model.MixVirtualRetryModel;
import live.lingting.sdk.model.MixVirtualSubmitModel;
import live.lingting.sdk.response.MixVirtualPayResponse;
import live.lingting.sdk.response.MixVirtualRetryResponse;
import live.lingting.sdk.util.MixUtils;
import live.lingting.service.PayService;
import live.lingting.service.VirtualAddressService;

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

	private final ApiConfig config;

	/**
	 * 虚拟货币预下单
	 * @author lingting 2021-06-07 22:50
	 */
	@Transactional(rollbackFor = Exception.class)
	public MixVirtualPayResponse.Data pay(MixVirtualPayModel model, Project project) {
		final Pay pay = payService.virtualCreate(model, project);
		final MixVirtualPayResponse.Data data = new MixVirtualPayResponse.Data();
		data.setAddress(pay.getAddress());
		data.setExpireTime(LocalDateTime.now().plusMinutes(config.getVirtualSubmitTimeout()));
		data.setTradeNo(pay.getTradeNo());
		return data;
	}

	@Transactional(rollbackFor = Exception.class)
	public void submit(MixVirtualSubmitModel model) {
		Pay pay = payService.getByNo(model.getTradeNo(), model.getProjectTradeNo());
		// 非虚拟支付 或者 非等待支付状态
		if (!pay.getCurrency().getVirtual() || !PayStatus.WAIT.equals(pay.getStatus())) {
			throw new BusinessException(ResponseCode.HASH_DISABLED);
		}
		model.setHash(MixUtils.clearHash(model.getHash()));
		if (!MixUtils.validHash(pay.getChain(), model.getHash())) {
			throw new BusinessException(ResponseCode.HASH_ERROR);
		}
		if (!payService.virtualSubmit(pay, model.getHash())) {
			throw new BusinessException(ResponseCode.HASH_SUBMIT_FAIL);
		}
	}

	@Transactional(rollbackFor = Exception.class)
	public MixVirtualRetryResponse.Data retry(MixVirtualRetryModel model) {
		Pay pay = payService.getByNo(model.getTradeNo(), model.getProjectTradeNo());
		// 非失败支付 或 重试结束时间小于等于当前时间
		if (!PayStatus.RETRY.equals(pay.getStatus()) || pay.getRetryEndTime().compareTo(LocalDateTime.now()) < 1) {
			throw new BusinessException(ResponseCode.RETRY_DISABLES);
		}
		// 更新hash, 且 hash校验不通过
		if (StringUtils.hasText(model.getHash()) && !MixUtils.validHash(pay.getChain(), model.getHash())) {
			throw new BusinessException(ResponseCode.HASH_ERROR);
		}
		if (!payService.virtualRetry(pay, model.getHash())) {
			throw new BusinessException(ResponseCode.RETRY_FAIL);
		}
		final MixVirtualRetryResponse.Data data = new MixVirtualRetryResponse.Data();
		data.setTradeNo(pay.getTradeNo());
		data.setRetryEndTime(pay.getRetryEndTime());
		return data;
	}

	@Transactional(rollbackFor = Exception.class)
	public void success(Pay pay) {
		if (payService.success(pay)) {
			virtualAddressService.unlock(pay.getAddress());
		}
	}

	@Transactional(rollbackFor = Exception.class)
	public void fail(Pay pay, String desc, LocalDateTime retryEndTime) {
		if (payService.fail(pay, desc, retryEndTime) && retryEndTime == null) {
			virtualAddressService.unlock(pay.getAddress());
		}
	}

}
