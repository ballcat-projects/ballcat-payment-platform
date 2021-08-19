package live.lingting.payment.wx.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.experimental.Accessors;
import live.lingting.payment.util.JacksonUtils;
import live.lingting.payment.wx.enums.ResponseCode;

/**
 * @author lingting 2021/8/19 11:43
 */
@Data
@Accessors(chain = true)
public class WxPayOrderCloseResponse {

	/**
	 * 返回状态码. 此字段是通信标识，非交易标识，交易是否成功需要查看result_code来判断
	 */
	@JsonProperty("return_code")
	private ResponseCode returnCode;

	/**
	 * 返回信息. 返回信息，如非空，为错误原因: 1.签名失败 2.参数格式校验错误
	 */
	@JsonProperty("return_msg")
	private String returnMsg;

	/**
	 * 应用APPID. 调用接口提交的应用ID
	 */
	@JsonProperty("appid")
	private String appId;

	/**
	 * 商户号. 调用接口提交的商户号
	 */
	@JsonProperty("mch_id")
	private String mchId;

	/**
	 * 随机字符串. 微信返回的随机字符串
	 */
	@JsonProperty("nonce_str")
	private String nonceStr;

	/**
	 * 签名. 微信返回的签名，详见签名算法
	 */
	@JsonProperty("sign")
	private String sign;

	/**
	 * 业务结果. SUCCESS/FAIL
	 */
	@JsonProperty("result_code")
	private ResponseCode resultCode;

	@JsonProperty("result_msg")
	private String resultMsg;

	/**
	 * 错误代码. 详细参见第6节错误列表
	 */
	@JsonProperty("err_code")
	private String errCode;

	/**
	 * 错误代码描述. 错误返回的信息描述
	 */
	@JsonProperty("err_code_des")
	private String errCodeDes;

	/**
	 * 返回的原始数据
	 */
	private Map<String, String> raw;

	public static WxPayOrderCloseResponse of(Map<String, String> res) {
		return JacksonUtils.toObj(JacksonUtils.toJson(res), WxPayOrderCloseResponse.class).setRaw(res);
	}

	public boolean isSuccess() {
		return ResponseCode.SUCCESS.equals(returnCode) && ResponseCode.SUCCESS.equals(resultCode);
	}

	@Getter
	@AllArgsConstructor
	public enum ErrCode {

		/**
		 * 已支付, 无法关闭, 请当做正常的交易处理
		 */
		ORDER_PAID("ORDERPAID"),
		/**
		 * 系统错误, 重新调用
		 */
		SYSTEM_ERROR("SYSTEMERROR"),
		/**
		 * 已关闭
		 */
		ORDER_CLOSED("ORDERCLOSED"),
		/**
		 * 其他 - 重试
		 */
		OTHER(""),

		;

		private final String val;

		public static ErrCode of(String val) {
			for (ErrCode e : values()) {
				if (e.getVal().equals(val)) {
					return e;
				}
			}
			return OTHER;
		}

	}

}
