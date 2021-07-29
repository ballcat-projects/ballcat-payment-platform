package live.lingting.payment.sdk.request;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import live.lingting.payment.sdk.constant.SdkConstants;
import live.lingting.payment.sdk.model.MixVirtualPayModel;
import live.lingting.payment.sdk.response.MixVirtualPayResponse;

/**
 * 虚拟货币 - 预下单
 *
 * @author lingting 2021/6/7 17:21
 */
@Getter
@Setter
@Accessors(chain = true)
public class MixVirtualPayRequest extends AbstractMixRequest<MixVirtualPayModel, MixVirtualPayResponse> {

	@Override
	public String getPath() {
		return SdkConstants.MIX_VIRTUAL_PAY_PATH;
	}

}
