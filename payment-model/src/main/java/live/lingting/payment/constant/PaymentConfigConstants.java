package live.lingting.payment.constant;

import lombok.experimental.UtilityClass;

/**
 * @author lingting 2021/6/18 9:41
 */
@UtilityClass
public class PaymentConfigConstants {

	public static final Long VIRTUAL_SUBMIT_HASH_TIMEOUT_DEFAULT = 120L;

	public static final Long PAY_EMPTY_INFO_TIMEOUT_DEFAULT = 360L;

	public static final Long PAY_FAIL_TIMEOUT_DEFAULT = 10L;

	public static final Long PAY_INFO_CREATE_TIMEOUT_DEFAULT = 720L;

	public static final Long PAY_RETRY_TIMEOUT_DEFAULT = 60L;

	public static final Long REAL_EXPIRE_TIMEOUT_DEFAULT = 1440L;

}
