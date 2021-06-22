package live.lingting.sdk.client;

import static live.lingting.sdk.constant.SdkConstants.FORWARD_SLASH;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpUtil;
import java.util.Map;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import live.lingting.sdk.constant.SdkConstants;
import live.lingting.sdk.domain.HttpProperties;
import live.lingting.sdk.exception.MixException;
import live.lingting.sdk.model.MixModel;
import live.lingting.sdk.request.MixRequest;
import live.lingting.sdk.response.MixResponse;
import live.lingting.sdk.util.JacksonUtils;
import live.lingting.sdk.util.MixUtils;

/**
 * @author lingting 2021/6/7 20:00
 */
@Slf4j
@Getter
@RequiredArgsConstructor
public class DefaultMixClient implements MixClient {

	private final String serverUrl;

	private final String apiKey;

	private final String apiSecurity;

	@Override
	public <M extends MixModel, R extends MixResponse<?>> R execute(MixRequest<M, R> request) throws MixException {

		try {
			return request(request);
		}
		catch (Exception e) {
			throw new MixException("请求异常!", e);
		}
	}

	private <M extends MixModel, R extends MixResponse<?>> R request(MixRequest<M, R> request) throws MixException {

		final M model = request.getModel();
		if (model == null) {
			throw new MixException("参数基础数据为空!");
		}
		model.valid();

		final Map<String, String> params = request.getParams();
		// 填充参数, 但是不覆盖原有数据
		params.computeIfAbsent(SdkConstants.FIELD_KEY, k -> apiKey);
		params.computeIfAbsent(SdkConstants.FIELD_NONCE, k -> RandomUtil.randomString(6));
		params.computeIfAbsent(SdkConstants.FIELD_SIGN, k -> MixUtils.sign(apiSecurity, params));

		return getResponse(request, params);
	}

	private <M extends MixModel, R extends MixResponse<?>> R getResponse(MixRequest<M, R> request,
			Map<String, String> params) {
		final HttpProperties hp = request.getProperties();
		final HttpRequest post = HttpUtil.createPost(getUrlStr(request));

		post.setConnectionTimeout(hp.getConnectTimeout());
		post.setReadTimeout(hp.getReadTimeout());

		final String type = SdkConstants.HTTP_TYPE_JSON;
		post.header("Accept", type);
		post.header("User-Agent", "live-lingting-sdk");
		post.header("Content-Type", type);
		final String body = JacksonUtils.toJson(params);
		post.body(body);

		if (log.isDebugEnabled()) {
			log.debug("[MixPay请求数据] url: {}, Content-Type: {}, body: {}", post.getUrl(), type, body);
		}

		final String resStr = post.execute().body();

		if (log.isDebugEnabled()) {
			log.debug("[MixPay请求返回数据] url: {}, Content-Type: {}, body: {}", post.getUrl(), type, resStr);
		}
		return request.convert(resStr);
	}

	private <M extends MixModel, R extends MixResponse<?>> String getUrlStr(MixRequest<M, R> request) {
		String url = serverUrl;

		if (!url.endsWith(FORWARD_SLASH)) {
			url = url + FORWARD_SLASH;
		}
		String rp = request.getPath();
		if (rp.startsWith(FORWARD_SLASH)) {
			return url + rp.substring(1);
		}
		return url + rp;
	}

}
