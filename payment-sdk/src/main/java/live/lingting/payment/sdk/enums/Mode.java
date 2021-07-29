package live.lingting.payment.sdk.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author lingting 2021/6/4 13:13
 */
@Getter
@AllArgsConstructor
public enum Mode {

	/**
	 * QR
	 */
	QR("二维码支付"),
	/**
	 * TRANSFER
	 */
	TRANSFER("转账"),

	;

	private final String desc;

}
