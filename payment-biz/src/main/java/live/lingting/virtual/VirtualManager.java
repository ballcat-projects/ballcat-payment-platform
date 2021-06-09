package live.lingting.virtual;

import com.hccake.ballcat.common.core.exception.BusinessException;
import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import live.lingting.entity.Pay;
import live.lingting.entity.Project;
import live.lingting.entity.VirtualAddress;
import live.lingting.enums.ResponseCode;
import live.lingting.sdk.enums.PayStatus;
import live.lingting.sdk.model.MixVirtualPayModel;
import live.lingting.sdk.response.MixVirtualPayResponse;
import live.lingting.service.PayService;
import live.lingting.service.VirtualAddressService;

/**
 * @author lingting 2021/6/7 22:48
 */
@Component
@RequiredArgsConstructor
public class VirtualManager {

	public static final Long EXPIRE_TIME = TimeUnit.HOURS.toMinutes(2);

	private final PayService payService;

	private final VirtualAddressService virtualAddressService;

	/**
	 * 虚拟货币预下单
	 * @author lingting 2021-06-07 22:50
	 */
	@Transactional(rollbackFor = Exception.class)
	public MixVirtualPayResponse.Data pay(MixVirtualPayModel model, Project project) {
		VirtualAddress va = virtualAddressService.lock(model);

		if (va == null) {
			throw new BusinessException(ResponseCode.NO_ADDRESS_AVAILABLE);
		}

		final Pay pay = new Pay().setAddress(va.getAddress()).setChain(model.getChain())
				.setCurrency(model.getContract().getCurrency()).setNotifyUrl(model.getNotifyUrl())
				.setProjectTradeNo(model.getProjectTradeNo()).setProjectId(project.getId()).setStatus(PayStatus.WAIT);

		if (!payService.save(pay)) {
			throw new BusinessException(ResponseCode.PAY_SAVE_FAIL);
		}
		final MixVirtualPayResponse.Data data = new MixVirtualPayResponse.Data();
		data.setAddress(va.getAddress());
		data.setExpireTime(LocalDateTime.now().plusMinutes(EXPIRE_TIME));
		data.setTradeNo(pay.getTradeNo());
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
		if (payService.fail(pay.getTradeNo(), desc, retryEndTime)) {
			virtualAddressService.unlock(pay.getAddress());
		}
	}

}
