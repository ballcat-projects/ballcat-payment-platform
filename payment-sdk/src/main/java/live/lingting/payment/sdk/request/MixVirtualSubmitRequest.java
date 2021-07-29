package live.lingting.payment.sdk.request;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import live.lingting.payment.sdk.constant.SdkConstants;
import live.lingting.payment.sdk.model.MixVirtualSubmitModel;
import live.lingting.payment.sdk.response.MixVirtualSubmitResponse;

/**
 * 虚拟货币 - 提交Hash
 *
 * @author lingting 2021/6/7 17:21
 */
@Getter
@Setter
@Accessors(chain = true)
public class MixVirtualSubmitRequest extends AbstractMixRequest<MixVirtualSubmitModel, MixVirtualSubmitResponse> {

	@Override
	public String getPath() {
		return SdkConstants.MIX_SUBMIT_PAY_PATH;
	}

}
