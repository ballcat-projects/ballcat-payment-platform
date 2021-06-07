package live.lingting.api.enums;

import com.hccake.ballcat.common.model.result.ResultCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author lingting 2021/6/7 16:44
 */
@Getter
@AllArgsConstructor
public enum ApiResponseCode implements ResultCode {
	/**
	 * 重复请求!
	 */
	REPEAT_REQUEST(20000,"重复请求!"),
	/**
	 * 签名异常!
	 */
	SIGN_ERROR(20001,"签名异常!"),
	/**
	 * 禁止访问!
	 */
	DISABLED(20002,"禁止访问!"),

	;

	private final Integer code;
	private final String message;
}
