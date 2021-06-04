package live.lingting.enums;

import com.hccake.ballcat.common.model.result.ResultCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author lingting 2021/6/4 17:20
 */
@Getter
@AllArgsConstructor
public enum ResponseCode implements ResultCode {
	/**
	 * 未找到项目!
	 */
	PROJECT_NOT_FOUND(233001, "未找到项目!"),

	;

	private final Integer code;

	private final String message;

}
