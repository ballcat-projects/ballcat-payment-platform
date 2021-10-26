package live.lingting.payment.sdk.request;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import live.lingting.payment.sdk.constant.SdkConstants;
import live.lingting.payment.sdk.model.MixVirtualRetrySubmitModel;
import live.lingting.payment.sdk.response.MixVirtualRetrySubmitResponse;

/**
 * 虚拟货币 - 重新提交
 *
 * @author lingting 2021/6/7 17:21
 */
@Getter
@Setter
@Accessors(chain = true)
public class MixVirtualRetrySubmitRequest
		extends AbstractMixRequest<MixVirtualRetrySubmitModel, MixVirtualRetrySubmitResponse> {

	@Override
	public String getPath() {
		return SdkConstants.MIX_RETRY_PAY_PATH;
	}

}
