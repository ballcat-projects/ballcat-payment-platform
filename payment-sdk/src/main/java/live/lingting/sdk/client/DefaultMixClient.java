package live.lingting.sdk.client;

import static live.lingting.sdk.constant.SdkConstants.FORWARD_SLASH;

import cn.hutool.core.util.RandomUtil;
import com.hccake.ballcat.common.util.JsonUtils;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import live.lingting.sdk.MixPay;
import live.lingting.sdk.constant.SdkConstants;
import live.lingting.sdk.domain.HttpProperties;
import live.lingting.sdk.exception.MixException;
import live.lingting.sdk.model.MixModel;
import live.lingting.sdk.request.MixRequest;
import live.lingting.sdk.response.MixResponse;
import live.lingting.sdk.util.StreamUtils;

/**
 * @author lingting 2021/6/7 20:00
 */
@Getter
@RequiredArgsConstructor
public class DefaultMixClient implements MixClient {

	private static final HostnameVerifier VERIFIER;

	private final String serverUrl;

	private final String apiKey;

	private final String apiSecurity;

	static {
		// 不允许URL的主机名和服务器的标识主机名不匹配的情况
		VERIFIER = (hostname, session) -> false;
	}

	@Override
	public <M extends MixModel, R extends MixResponse<?>> R execute(MixRequest<M, R> request) throws MixException {

		try {
			return request(request);
		}
		catch (IOException e) {
			throw new MixException("请求异常!", e);
		}
	}

	private <M extends MixModel, R extends MixResponse<?>> R request(MixRequest<M, R> request)
			throws MixException, IOException {
		request.valid();
		final Map<String, String> params = request.getParams();
		// 填充参数, 但是不覆盖原有数据
		params.computeIfAbsent(SdkConstants.FIELD_KEY, k -> apiKey);
		params.computeIfAbsent(SdkConstants.FIELD_NONCE, k -> RandomUtil.randomString(6));
		params.computeIfAbsent(SdkConstants.FIELD_SIGN, k -> MixPay.sign(apiSecurity, params));

		byte[] content = JsonUtils.toJson(params).getBytes(StandardCharsets.UTF_8);

		HttpURLConnection hc = null;
		OutputStream out = null;
		InputStream is = null;
		try {
			hc = getConnect(request);
			out = hc.getOutputStream();
			out.write(content);

			// 异常返回值为空
			if ((is = hc.getErrorStream()) == null) {
				// 正常返回值
				is = hc.getInputStream();
			}

			return request.convert(StreamUtils.toString(is));
		}
		catch (MalformedURLException e) {
			throw new MixException("url 构建异常!", e);
		}
		catch (IOException e) {
			throw new MixException("http连接 构建异常!", e);
		}
		finally {
			if (out != null) {
				out.close();
			}
			if (is != null) {
				is.close();
			}
			if (hc != null) {
				hc.disconnect();
			}
		}
	}

	private <M extends MixModel, R extends MixResponse<?>> HttpURLConnection getConnect(MixRequest<M, R> request)
			throws IOException {
		HttpURLConnection hc;
		final HttpProperties hp = request.getProperties();
		URL url = getUrl(request);
		if (url.getProtocol().equals(SdkConstants.HTTP_PROTOCOL_HTTPS)) {
			final HttpsURLConnection hsc = (HttpsURLConnection) url.openConnection();

			if (!hp.getVerifierSsl()) {
				hsc.setHostnameVerifier(VERIFIER);
			}
			hc = hsc;
		}
		else {
			hc = (HttpURLConnection) url.openConnection();
		}

		hc.setConnectTimeout(hp.getConnectTimeout());
		hc.setReadTimeout(hp.getReadTimeout());
		hc.setRequestMethod(SdkConstants.HTTP_METHOD_POST);
		hc.setDoInput(true);
		hc.setDoOutput(true);
		hc.setRequestProperty("Accept", SdkConstants.HTTP_TYPE_JSON);
		hc.setRequestProperty("User-Agent", "live-lingting-sdk");
		hc.setRequestProperty("Content-Type", SdkConstants.HTTP_TYPE_JSON);
		return hc;
	}

	private <M extends MixModel, R extends MixResponse<?>> URL getUrl(MixRequest<M, R> request)
			throws MalformedURLException {
		String url = serverUrl;

		if (!url.endsWith(FORWARD_SLASH)) {
			url = url + FORWARD_SLASH;
		}
		String rp = request.getPath();
		if (rp.startsWith(FORWARD_SLASH)) {
			return new URL(url + rp.substring(1));
		}
		return new URL(url + rp);
	}

}
