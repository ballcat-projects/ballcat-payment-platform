package live.lingting.api.manager;

import com.hccake.ballcat.common.core.exception.BusinessException;
import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import live.lingting.Redis;
import live.lingting.entity.Pay;
import live.lingting.entity.Project;
import live.lingting.enums.ResponseCode;
import live.lingting.sdk.model.MixVirtualPayModel;
import live.lingting.sdk.model.MixVirtualSubmitModel;
import live.lingting.sdk.response.MixVirtualPayResponse;
import live.lingting.sdk.util.MixUtils;
import live.lingting.service.PayService;
import live.lingting.service.VirtualAddressService;

/**
 * @author lingting 2021/6/7 22:48
 */
@Component
@RequiredArgsConstructor
public class VirtualManager {

	public static final Long EXPIRE_TIME = TimeUnit.HOURS.toMinutes(2);

	private final Redis redis;

	private final PayService payService;

	private final VirtualAddressService virtualAddressService;

	/**
	 * 虚拟货币预下单
	 * @author lingting 2021-06-07 22:50
	 */
	@Transactional(rollbackFor = Exception.class)
	public MixVirtualPayResponse.Data pay(MixVirtualPayModel model, Project project) {
		final Pay pay = payService.virtualCreate(model, project);
		final MixVirtualPayResponse.Data data = new MixVirtualPayResponse.Data();
		data.setAddress(pay.getAddress());
		data.setExpireTime(LocalDateTime.now().plusMinutes(EXPIRE_TIME));
		data.setTradeNo(pay.getTradeNo());
		return data;
	}

	@Transactional(rollbackFor = Exception.class)
	public void submit(MixVirtualSubmitModel model) {
		Pay pay = payService.getByNo(model.getTradeNo(), model.getProjectTradeNo());
		model.setHash(MixUtils.clearHash(model.getHash()));
		if (!MixUtils.validHash(pay.getChain(), model.getHash())) {
			throw new BusinessException(ResponseCode.HASH_ERROR);
		}
		if (!payService.virtualSubmit(pay, model.getHash())) {
			throw new BusinessException(ResponseCode.HASH_SUBMIT_FAIL);
		}
	}

	@Transactional(rollbackFor = Exception.class)
	public void success(Pay pay) {
		if (payService.success(pay)) {
			virtualAddressService.unlock(pay.getAddress());
		}
	}

	@Transactional(rollbackFor = Exception.class)
	public void fail(Pay pay, String desc, LocalDateTime retryEndTime) {
		if (payService.fail(pay.getTradeNo(), desc, retryEndTime)) {
			virtualAddressService.unlock(pay.getAddress());
		}
	}

}
