package live.lingting.payment.wx.constants;

import java.math.BigDecimal;
import lombok.experimental.UtilityClass;

/**
 * @author lingting 2021/1/26 16:15
 */
@UtilityClass
public class WxPayConstant {

	/**
	 * 一百
	 */
	public static final BigDecimal HUNDRED = new BigDecimal("100");

	/**
	 * 签名字段名
	 */
	public static final String FIELD_SIGN = "sign";

	/**
	 * 签名类型字段名
	 */
	public static final String FIELD_SIGN_TYPE = "sign_type";

	/**
	 * 回调成功返回值
	 */
	public static final String CALLBACK_SUCCESS = callback("OK");

	/**
	 * 回调验签失败返回值
	 */
	public static final String CALLBACK_SIGN_ERROR = callback("签名异常");

	public static String callback(String msg) {
		return "<xml>\n" + "  <return_code><![CDATA[FAIL]]></return_code>\n" + "  <return_msg><![CDATA[" + msg
				+ "]]></return_msg>\n" + "</xml>";
	}

}
