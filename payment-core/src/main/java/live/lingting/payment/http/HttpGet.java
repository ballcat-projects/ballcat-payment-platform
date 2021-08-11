package live.lingting.payment.http;

import cn.hutool.http.HttpUtil;

/**
 * @author lingting 2021/8/4 下午6:59
 */
public class HttpGet extends HttpRequest {

	public static HttpGet of(String url) {
		return of(url, new HttpProperties());
	}

	public static HttpGet of(String url, HttpProperties properties) {
		final HttpGet http = new HttpGet();
		http.setUrl(url);
		http.setProperties(properties);
		return http;
	}

	@Override
	public HttpResponse exec() {
		cn.hutool.http.HttpRequest http = HttpUtil.createGet(getUrl());
		http.setConnectionTimeout(getProperties().getConnectTimeout().intValue());
		http.setReadTimeout(getProperties().getReadTimeout().intValue());

		http.body(getBody());
		http.headerMap(getHeaders(), true);

		return new HttpResponse(http.execute());
	}

}
