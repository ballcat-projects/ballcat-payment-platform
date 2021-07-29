package live.lingting.payment.sdk.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author lingting 2021/6/7 17:38
 */
@Getter
@AllArgsConstructor
public enum SdkContract {

	/**
	 * USDT
	 */
	USDT(Currency.USDT),

	;

	private final Currency currency;

}
