package live.lingting.payment.sdk.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author lingting 2021/6/4 11:09
 */
@Getter
@AllArgsConstructor
public enum PayStatus {

	/**
	 * 等待支付
	 */
	WAIT("等待支付"),
	/**
	 * 重试
	 */
	RETRY("重试"),
	/**
	 * 成功
	 */
	SUCCESS("成功"),
	/**
	 * 失败
	 */
	FAIL("失败"),

	;

	private final String desc;

}
