package live.lingting.payment.http;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author lingting 2021/9/10 16:09
 */
@Getter
@AllArgsConstructor
public enum HttpMediaType {
	/**
	 * json
	 */
	APPLICATION_JSON("application/json")

;

	private final String value;
}
