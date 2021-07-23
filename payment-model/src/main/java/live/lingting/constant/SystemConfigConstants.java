package live.lingting.constant;

import lombok.experimental.UtilityClass;

/**
 * @author lingting 2021/6/18 9:41
 */
@UtilityClass
public class SystemConfigConstants {

	public static final Long MIN = 1L;

	public static final String VIRTUAL_SUBMIT_HASH_TIMEOUT = "virtual_submit_hash_timeout";

	public static final Long VIRTUAL_SUBMIT_HASH_TIMEOUT_DEFAULT = 120L;

	public static final String PAY_EMPTY_INFO_TIMEOUT = "pay_empty_info_timeout";

	public static final Long PAY_EMPTY_INFO_TIMEOUT_DEFAULT = 360L;

	public static final String PAY_FAIL_TIMEOUT = "pay_fail_timeout";

	public static final Long PAY_FAIL_TIMEOUT_DEFAULT = 10L;

	public static final String PAY_INFO_CREATE_TIMEOUT = "pay_info_create_timeout";

	public static final Long PAY_INFO_CREATE_TIMEOUT_DEFAULT = 720L;

	public static final String PAY_RETRY_TIMEOUT = "pay_retry_timeout";

	public static final Long PAY_RETRY_TIMEOUT_DEFAULT = 60L;

	public static final String REAL_EXPIRE_TIMEOUT = "real_expire_timeout";

	public static final Long REAL_EXPIRE_TIMEOUT_DEFAULT = 1440L;

	public static final String TEST = "test";

	public static final Long TEST_DEFAULT = 0L;

}
