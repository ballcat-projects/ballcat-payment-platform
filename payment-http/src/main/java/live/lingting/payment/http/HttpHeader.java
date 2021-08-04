package live.lingting.payment.http;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author lingting 2021/7/12 16:39
 */
@Getter
@AllArgsConstructor
public enum HttpHeader {

	/**
	 * Accept
	 */
	ACCEPT("Accept"),
	/**
	 * User_Agent
	 */
	USER_AGENT("User-Agent"),
	/**
	 * Content_Type
	 */
	CONTENT_TYPE("Content-Type"),
	/**
	 * Authorization
	 */
	AUTHORIZATION("Authorization"),

	;

	private final String val;

}
