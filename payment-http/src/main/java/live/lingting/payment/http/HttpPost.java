package live.lingting.payment.http;

import cn.hutool.http.HttpUtil;
import lombok.Getter;
import lombok.Setter;

/**
 * @author lingting 2021/7/12 16:23
 */
@Getter
@Setter
public class HttpPost extends HttpRequest {

	public static HttpPost of(String url) {
		return of(url, new HttpProperties());
	}

	public static HttpPost of(String url, HttpProperties properties) {
		final HttpPost http = new HttpPost();
		http.setUrl(url);
		http.setProperties(properties);
		return http;
	}

	@Override
	public HttpResponse exec() {
		final cn.hutool.http.HttpRequest http = HttpUtil.createPost(getUrl());
		final HttpProperties properties = getProperties();

		http.setReadTimeout(properties.getReadTimeout().intValue());
		http.setConnectionTimeout(properties.getConnectTimeout().intValue());

		http.headerMap(getHeaders(), true);
		http.body(getBody());

		return HttpResponse.of(http.execute());
	}

}
