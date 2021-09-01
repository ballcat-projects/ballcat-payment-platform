package live.lingting.payment.sdk.client;

import static live.lingting.payment.sdk.constant.SdkConstants.FORWARD_SLASH;

import cn.hutool.core.util.RandomUtil;
import java.util.Map;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import live.lingting.payment.http.HttpGet;
import live.lingting.payment.http.HttpHeader;
import live.lingting.payment.http.HttpMethod;
import live.lingting.payment.http.HttpPost;
import live.lingting.payment.http.HttpRequest;
import live.lingting.payment.sdk.constant.SdkConstants;
import live.lingting.payment.sdk.exception.MixException;
import live.lingting.payment.sdk.model.MixModel;
import live.lingting.payment.sdk.request.MixRequest;
import live.lingting.payment.sdk.response.MixResponse;
import live.lingting.payment.sdk.util.MixUtils;
import live.lingting.payment.util.JacksonUtils;

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
		catch (MixException e) {
			throw e;
		}
		catch (Exception e) {
			throw new MixException("请求异常! " + e.getMessage(), e);
		}
	}

	protected <M extends MixModel, R extends MixResponse<?>> R request(MixRequest<M, R> request) throws MixException {

		final M model = request.getModel();
		if (model == null) {
			throw new MixException("参数基础数据为空!");
		}
		model.valid();

		final Map<String, String> params = request.getParams();
		// 填充参数, 但是不覆盖原有数据
		params.computeIfAbsent(SdkConstants.FIELD_KEY, k -> getApiKey());
		params.computeIfAbsent(SdkConstants.FIELD_NONCE, k -> RandomUtil.randomString(6));
		params.computeIfAbsent(SdkConstants.FIELD_SIGN, k -> MixUtils.sign(getApiSecurity(), params));

		return getResponse(request, params);
	}

	protected <M extends MixModel, R extends MixResponse<?>> R getResponse(MixRequest<M, R> request,
			Map<String, String> params) {
		HttpRequest hr = getRequest(request, params);

		// 不存在UA则添加一个sdk的UA
		if (!hr.hasHeader(HttpHeader.USER_AGENT)) {
			hr.header(HttpHeader.USER_AGENT, "live-lingting-sdk");
		}

		if (log.isDebugEnabled()) {
			log.debug("[MixPay请求数据] url: {}, Content-Type: {}, body: {}", hr.getUrl(),
					hr.getHeader(HttpHeader.CONTENT_TYPE), hr.getBody());
		}

		final String resStr = hr.exec().getBody();

		if (log.isDebugEnabled()) {
			log.debug("[MixPay请求返回数据] url: {}, Content-Type: {}, body: {}", hr.getUrl(),
					hr.getHeader(HttpHeader.CONTENT_TYPE), resStr);
		}
		return request.convert(resStr);
	}

	protected <M extends MixModel, R extends MixResponse<?>> HttpRequest getRequest(MixRequest<M, R> request,
			Map<String, String> params) {
		if (request.getMethod().equals(HttpMethod.GET)) {
			return handlerGetRequest(request, params);
		}
		else {
			return handlerPostRequest(request, params);
		}
	}

	protected <M extends MixModel, R extends MixResponse<?>> HttpRequest handlerGetRequest(MixRequest<M, R> request,
			Map<String, String> params) {
		HttpGet get = HttpGet.of(getUrlStr(request), request.getProperties());
		final String body = JacksonUtils.toJson(params);
		get.setBody(body);
		return get;
	}

	protected <M extends MixModel, R extends MixResponse<?>> HttpRequest handlerPostRequest(MixRequest<M, R> request,
			Map<String, String> params) {
		HttpPost post = HttpPost.of(getUrlStr(request), request.getProperties());

		final String type = SdkConstants.HTTP_TYPE_JSON;
		post.header(HttpHeader.ACCEPT, type);
		post.header(HttpHeader.CONTENT_TYPE, type);
		final String body = JacksonUtils.toJson(params);
		post.setBody(body);
		return post;
	}

	protected <M extends MixModel, R extends MixResponse<?>> String getUrlStr(MixRequest<M, R> request) {
		String url = getServerUrl();

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
