package live.lingting.sdk.constant;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * @author lingting 2021/6/7 16:32
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SdkConstants {

	public static final String FIELD_SIGN = "sign";

	public static final String FIELD_KEY = "key";

	public static final String FIELD_NONCE = "nonce";

	public static final String NOTIFY_URL_PREFIX = "http";

	public static final String HTTP_PROTOCOL_HTTPS = "https";

	public static final Integer SUCCESS_CODE = 200;

	public static final String FORWARD_SLASH = "/";

	/**
	 * -------------------------------PATH-----------------------------------------
	 */
	public static final String MIX_VIRTUAL_PAY_PATH = "pay/virtual";

	public static final String MIX_SUBMIT_PAY_PATH = "pay/virtual/submit";

	public static final String MIX_RETRY_PAY_PATH = "pay/virtual/retry";

	/**
	 * -------------------------------HTTP-----------------------------------------
	 */

	public static final String HTTP_METHOD_POST = "POST";

	public static final String HTTP_TYPE_JSON = "application/json;charset=utf-8";

}
