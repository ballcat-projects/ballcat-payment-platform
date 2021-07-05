package live.lingting.sdk.request;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import live.lingting.sdk.constant.SdkConstants;
import live.lingting.sdk.model.MixForciblyRetryModel;
import live.lingting.sdk.response.MixForciblyRetryResponse;

/**
 * 查询
 *
 * @author lingting 2021/6/7 17:21
 */
@Getter
@Setter
@Accessors(chain = true)
public class MixForciblyRetryRequest extends AbstractMixRequest<MixForciblyRetryModel, MixForciblyRetryResponse> {

	@Override
	public String getPath() {
		return SdkConstants.MIX_FORCIBLY_RETRY_PATH;
	}

}
