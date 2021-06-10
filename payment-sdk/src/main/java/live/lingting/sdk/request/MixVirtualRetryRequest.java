package live.lingting.sdk.request;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import live.lingting.sdk.constant.SdkConstants;
import live.lingting.sdk.model.MixVirtualRetryModel;
import live.lingting.sdk.response.MixVirtualRetryResponse;

/**
 * 虚拟货币 - 重试
 *
 * @author lingting 2021/6/7 17:21
 */
@Getter
@Setter
@Accessors(chain = true)
public class MixVirtualRetryRequest extends AbstractMixRequest<MixVirtualRetryModel, MixVirtualRetryResponse> {

	@Override
	public String getPath() {
		return SdkConstants.MIX_RETRY_PAY_PATH;
	}

}
