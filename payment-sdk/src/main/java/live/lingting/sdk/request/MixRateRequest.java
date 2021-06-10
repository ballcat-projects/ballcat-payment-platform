package live.lingting.sdk.request;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import live.lingting.sdk.constant.SdkConstants;
import live.lingting.sdk.model.MixRateModel;
import live.lingting.sdk.response.MixRateResponse;

/**
 * 汇率
 *
 * @author lingting 2021/6/7 17:21
 */
@Getter
@Setter
@Accessors(chain = true)
public class MixRateRequest extends AbstractMixRequest<MixRateModel, MixRateResponse> {

	@Override
	public String getPath() {
		return SdkConstants.MIX_RATE_PATH;
	}

}
