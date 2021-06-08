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

	/**
	 * 暂无可用地址!
	 */
	NO_ADDRESS_AVAILABLE(233002, "暂无可用地址!"),

	/**
	 * 支付信息保存失败!
	 */
	PAY_SAVE_FAIL(233003, "支付信息保存失败!"),

	;

	private final Integer code;

	private final String message;

}
