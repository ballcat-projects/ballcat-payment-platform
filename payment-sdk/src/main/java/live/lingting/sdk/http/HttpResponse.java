package live.lingting.sdk.http;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

/**
 * @author lingting 2021/7/12 16:25
 */
@Getter
@Setter
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class HttpResponse {

	protected final cn.hutool.http.HttpResponse response;

	protected static HttpResponse of(cn.hutool.http.HttpResponse response) {
		return new HttpResponse(response);
	}

	public String body() {
		return response.body();
	}

}
