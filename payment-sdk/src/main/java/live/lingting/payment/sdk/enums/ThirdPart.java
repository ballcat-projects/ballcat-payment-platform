package live.lingting.payment.sdk.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author lingting 2021/6/4 11:38
 */
@Getter
@AllArgsConstructor
public enum ThirdPart {

	/**
	 * WX
	 */
	WX,
	/**
	 * ALI
	 */
	ALI,
	/**
	 * 银行卡 - 未知银行
	 */
	BC_UNKNOWN,

	;

}
