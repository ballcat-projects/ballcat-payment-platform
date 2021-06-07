package live.lingting.sdk.request;

import com.hccake.ballcat.common.util.JsonUtils;
import com.hccake.ballcat.common.util.json.TypeReference;
import java.util.Map;
import lombok.Getter;
import lombok.Setter;
import org.springframework.util.StringUtils;
import live.lingting.sdk.constant.SdkConstants;
import live.lingting.sdk.domain.HttpProperties;
import live.lingting.sdk.exception.MixException;
import live.lingting.sdk.exception.MixRequestParamsValidException;
import live.lingting.sdk.model.MixModel;
import live.lingting.sdk.response.MixResponse;

/**
 * @author lingting 2021/6/7 17:33
 */
@Getter
@Setter
public abstract class AbstractMixRequest<M extends MixModel, R extends MixResponse<?>> implements MixRequest<M, R> {

	private M model;

	@Override
	public Map<String, String> getParams() throws MixRequestParamsValidException {
		final M m = getModel();

		if (m == null) {
			throw new MixRequestParamsValidException("参数基础数据为空!");
		}

		return JsonUtils.toObj(JsonUtils.toJson(m), new TypeReference<Map<String, String>>() {
		});
	}

	@Override
	public HttpProperties getProperties() {
		return new HttpProperties();
	}

	@Override
	public R convert(String resStr) {
		return JsonUtils.toObj(resStr, new TypeReference<R>() {
		});
	}

	/**
	 * 在需要校验的时候调用!
	 * @author lingting 2021-06-07 19:45
	 */
	public void validNotifyUrl() throws MixException {
		String url = getModel() == null ? null : getModel().getNotifyUrl();

		if (!StringUtils.hasText(url)) {
			throw new MixRequestParamsValidException("回调通知地址不能为空!");
		}

		if (!url.startsWith(SdkConstants.NOTIFY_URL_PREFIX)) {
			throw new MixRequestParamsValidException("回调通知地址不是正确的http请求地址!");
		}
	}

}
