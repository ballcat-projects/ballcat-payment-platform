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
	/**
	 * 项目交易号已存在
	 */
	PROJECT_NO_REPEAT(233004, "项目交易号已存在!"),
	/**
	 * Hash无效
	 */
	HASH_ERROR(233005, "Hash无效!"),
	/**
	 * Hash提交失败
	 */
	HASH_SUBMIT_FAIL(233006, "Hash提交失败!"),
	/**
	 * Hash已被使用
	 */
	HASH_EXIST(233007, "Hash已被使用!"),
	/**
	 * 禁止提交Hash
	 */
	HASH_DISABLED(233008, "禁止提交Hash"),
	/**
	 * 禁止重试!
	 */
	RETRY_DISABLES(233009, "禁止重试!"),
	/**
	 * 重试失败
	 */
	RETRY_FAIL(233010, "重试失败!"),
	/**
	 * 未知支付信息!
	 */
	PAY_NOT_FOUND(233011, "未知支付信息!"),

	;

	private final Integer code;

	private final String message;

}
