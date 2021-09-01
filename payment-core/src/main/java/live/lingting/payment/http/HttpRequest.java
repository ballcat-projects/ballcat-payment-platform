package live.lingting.payment.http;

import java.util.HashMap;
import java.util.Map;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author lingting 2021/7/12 16:16
 */
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class HttpRequest {

	@Setter
	private HttpProperties properties = HttpProperties.unlimited();

	@Setter
	private String url;

	@Setter(AccessLevel.PROTECTED)
	private Map<String, String> headers = new HashMap<>(16);

	private String body;

	public void header(HttpHeader header, String val) {
		header(header.getVal(), val);
	}

	public void header(String name, String val) {
		getHeaders().put(name, val);
	}

	public void auth(String val) {
		header(HttpHeader.AUTHORIZATION, val);
	}

	/**
	 * 发起请求
	 * @return live.lingting.payment.http.HttpResponse
	 * @author lingting 2021-07-12 16:28
	 */
	public abstract HttpResponse exec();

	public boolean hasHeader(HttpHeader header) {
		return hasHeader(header.getVal());
	}

	public boolean hasHeader(String name) {
		return getHeaders().containsKey(name);
	}

	public String getHeader(HttpHeader header) {
		return getHeader(header.getVal());
	}

	public String getHeader(String name) {
		return getHeaders().get(name);
	}

	public void setBody(String body) {
		this.body = body;
	}

	public void setBody(String body, String contentType) {
		this.body = body;
		header(HttpHeader.CONTENT_TYPE, contentType);
	}

	/**
	 * 设置 连接超时
	 * @param timeout 连接超时, 单位: 毫秒
	 */
	public void setConnectTimeout(long timeout) {
		properties.setConnectTimeout(timeout);
	}

	/**
	 * 设置 读取超时
	 * @param timeout 读取超时, 单位: 毫秒
	 */
	public void setReadTimeout(long timeout) {
		properties.setReadTimeout(timeout);
	}

}
