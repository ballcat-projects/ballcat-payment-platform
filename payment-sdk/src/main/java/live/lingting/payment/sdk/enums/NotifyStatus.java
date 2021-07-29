package live.lingting.payment.sdk.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author lingting 2021/6/4 13:28
 */
@Getter
@AllArgsConstructor
public enum NotifyStatus {

	/**
	 * WAIT
	 */
	WAIT,
	/**
	 * 通知中
	 */
	ING,
	/**
	 * SUCCESS
	 */
	SUCCESS,
	/**
	 * FAIL
	 */
	FAIL,

	;

}
