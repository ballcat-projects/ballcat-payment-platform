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
		final HttpPost post = new HttpPost();
		post.setUrl(url);
		post.setProperties(properties);
		return post;
	}

	@Override
	public HttpResponse exec() {
		final cn.hutool.http.HttpRequest post = HttpUtil.createPost(getUrl());
		final HttpProperties properties = getProperties();

		post.setReadTimeout(properties.getReadTimeout().intValue());
		post.setConnectionTimeout(properties.getConnectTimeout().intValue());

		post.headerMap(getHeaders(), true);
		post.body(getBody());

		return HttpResponse.of(post.execute());
	}

}
