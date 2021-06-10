package live.lingting.sdk.request;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import live.lingting.sdk.constant.SdkConstants;
import live.lingting.sdk.model.MixQueryModel;
import live.lingting.sdk.response.MixQueryResponse;

/**
 * 查询
 *
 * @author lingting 2021/6/7 17:21
 */
@Getter
@Setter
@Accessors(chain = true)
public class MixQueryRequest extends AbstractMixRequest<MixQueryModel, MixQueryResponse> {

	@Override
	public String getPath() {
		return SdkConstants.MIX_QUERY_PATH;
	}

}
