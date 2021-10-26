package live.lingting.payment.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author lingting 2021/6/4 17:20
 */
@Getter
@AllArgsConstructor
public enum ResponseCode {

	/**
	 * success
	 */
	SUCCESS(200, "Success"),

	/**
	 * 重复请求!
	 */
	REPEAT_REQUEST(20000, "重复请求!"),
	/**
	 * 签名异常!
	 */
	SIGN_ERROR(20001, "签名异常!"),
	/**
	 * 禁止访问!
	 */
	DISABLED(20002, "禁止访问!"),

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
	 * 该第三方交易号已被使用
	 */
	TRADE_NO_EXIST(233007, "该第三方交易号已被使用!"),
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
	/**
	 * 禁止执行该操作!
	 */
	PROHIBIT_OPERATION(233012, "禁止执行该操作!"),
	/**
	 * 操作失败!
	 */
	OPERATION_FAILED(233013, "操作失败!"),
	/**
	 * 未知第三方!
	 */
	UNKNOWN_THIRD_PARTY(233014, "未知第三方!"),
	/**
	 * 该第三方不支持当前支付模式!
	 */
	THIRD_PARTY_NOT_SUPPORT(233015, "该第三方不支持当前支付模式!"),
	/**
	 * 禁止使用该支付方式!
	 */
	PROHIBIT_PAYMENT_METHOD(233016, "禁止使用该支付方式!"),
	/**
	 * 生成支付信息异常!
	 */
	ABNORMAL_PAYMENT_GENERATED(233017, "生成支付信息异常! 详细信息: {}"),
	/**
	 * 未找到指定支付配置!
	 */
	PAYMENT_CONFIG_NOT_FOUND(233018, "未找到指定支付配置!"),
	/**
	 * 支付关键配置缺失, 请检查!
	 */
	PAYMENT_CONFIG_ERROR(233019, "支付关键配置缺失, 请检查!"),
	/**
	 * 支付配置已被禁用!
	 */
	PAYMENT_CONFIG_DISABLED(233020, "支付配置已被禁用!"),
	/**
	 * 已存在相同支付配置!
	 */
	PAYMENT_CONFIG_EXIST(233020, "已存在相同支付配置!"),
	/**
	 * 重新提交失败
	 */
	RETRY_SUBMIT_FAIL(233021, "重新提交失败!"),

	;

	private final Integer code;

	private final String message;

}
