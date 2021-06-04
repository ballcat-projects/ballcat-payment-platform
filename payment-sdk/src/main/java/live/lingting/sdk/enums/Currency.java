package live.lingting.sdk.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author lingting 2021/6/4 11:26
 */
@Getter
@AllArgsConstructor
public enum Currency {

	/**
	 * CNY
	 */
	CNY(false),
	/**
	 * USDT
	 */
	USDT(true),

	;

	private final Boolean virtual;

}
