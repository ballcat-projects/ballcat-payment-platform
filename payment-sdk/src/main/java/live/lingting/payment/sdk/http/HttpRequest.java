package live.lingting.payment.sdk.http;

import java.util.HashMap;
import java.util.Map;
import lombok.Getter;
import lombok.Setter;

/**
 * @author lingting 2021/7/12 16:16
 */
@Getter
@Setter
public abstract class HttpRequest {

	private HttpProperties properties;

	private String url;

	private Map<String, String> headers = new HashMap<>(16);

	private String body;

	public void header(HttpHeader header, String val) {
		header(header.getVal(), val);
	}

	public void header(String name, String val) {
		getHeaders().put(name, val);
	}

	/**
	 * 发起请求
	 * @return live.lingting.payment.sdk.http.HttpResponse
	 * @author lingting 2021-07-12 16:28
	 */
	public abstract HttpResponse exec();

}
