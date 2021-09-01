package live.lingting.payment.sdk.constant;

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

	public static final String FIELD_RATE = "rate";

	public static final String NOTIFY_URL_PREFIX = "http";

	public static final String HTTP_PROTOCOL_HTTPS = "https";

	public static final Integer SUCCESS_CODE = 200;

	public static final String SUCCESS_BODY = "success";

	public static final String FORWARD_SLASH = "/";

	/**
	 * -------------------------------PATH-----------------------------------------
	 */
	public static final String MIX_REAL_PAU_PATH = "pay";

	public static final String MIX_VIRTUAL_PAY_PATH = "pay/virtual";

	public static final String MIX_SUBMIT_PAY_PATH = "pay/virtual/submit";

	public static final String MIX_RETRY_PAY_PATH = "pay/virtual/retry";

	public static final String MIX_FORCIBLY_RETRY_PATH = "forcibly/retry";

	public static final String MIX_FORCIBLY_FAIL_PATH = "forcibly/fail";

	public static final String MIX_QUERY_PATH = "query";

	public static final String MIX_RATE_PATH = "rate";

	public static final String MIX_BANK = "bank";

	/**
	 * -------------------------------HTTP-----------------------------------------
	 */

	public static final String HTTP_METHOD_POST = "POST";

	public static final String HTTP_TYPE_JSON = "application/json;charset=utf-8";

}
