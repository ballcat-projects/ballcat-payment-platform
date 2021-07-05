package live.lingting.sdk.request;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import live.lingting.sdk.constant.SdkConstants;
import live.lingting.sdk.model.MixForciblyFailModel;
import live.lingting.sdk.response.MixForciblyFailResponse;

/**
 * 查询
 *
 * @author lingting 2021/6/7 17:21
 */
@Getter
@Setter
@Accessors(chain = true)
public class MixForciblyFailRequest extends AbstractMixRequest<MixForciblyFailModel, MixForciblyFailResponse> {

	@Override
	public String getPath() {
		return SdkConstants.MIX_FORCIBLY_FAIL_PATH;
	}

}
