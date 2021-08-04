package live.lingting.payment.biz.config;

import static live.lingting.payment.constant.PaymentConfigConstants.PAY_EMPTY_INFO_TIMEOUT_DEFAULT;
import static live.lingting.payment.constant.PaymentConfigConstants.PAY_FAIL_TIMEOUT_DEFAULT;
import static live.lingting.payment.constant.PaymentConfigConstants.PAY_INFO_CREATE_TIMEOUT_DEFAULT;
import static live.lingting.payment.constant.PaymentConfigConstants.PAY_RETRY_TIMEOUT_DEFAULT;
import static live.lingting.payment.constant.PaymentConfigConstants.REAL_EXPIRE_TIMEOUT_DEFAULT;
import static live.lingting.payment.constant.PaymentConfigConstants.VIRTUAL_SUBMIT_HASH_TIMEOUT_DEFAULT;

/**
 * @author lingting 2021/6/18 9:39
 */
public class PaymentConfig {

	/**
	 * 虚拟货币提交Hash超时时间, 单位: 分钟
	 *
	 * @author lingting 2021-06-18 09:47
	 */
	public Long getVirtualSubmitTimeout() {
		return VIRTUAL_SUBMIT_HASH_TIMEOUT_DEFAULT;
	}

	/**
	 * 未获取到交易信息等待时长。单位: 分钟
	 *
	 * @author lingting 2021-06-18 09:54
	 */
	public Long getEmptyInfoTimeout() {
		return PAY_EMPTY_INFO_TIMEOUT_DEFAULT;
	}

	/**
	 * 失败延时。单位: 分钟
	 *
	 * @author lingting 2021-06-18 09:54
	 */
	public Long getFailTimeout() {
		return PAY_FAIL_TIMEOUT_DEFAULT;
	}

	/**
	 * 交易信息创建时间最大限制。单位: 分钟
	 *
	 * @author lingting 2021-06-18 09:54
	 */
	public Long getInfoCreateTimeout() {
		return PAY_INFO_CREATE_TIMEOUT_DEFAULT;
	}

	/**
	 * 支付重试时长。单位: 分钟
	 *
	 * @author lingting 2021-06-18 09:54
	 */
	public Long getRetryTimeout() {
		return PAY_RETRY_TIMEOUT_DEFAULT;
	}

	/**
	 * 真实货币支付过期时间 - 指定时间内未付款记为失败
	 *
	 * mode={@link live.lingting.payment.sdk.enums.Mode#TRANSFER} 时, 不受本值限制
	 *
	 * @author lingting 2021-07-14 15:34
	 */
	public Long getRealExpireTimeout() {
		return REAL_EXPIRE_TIMEOUT_DEFAULT;
	}

	/**
	 * <p>
	 * 是否作为测试用模块启动.
	 * </p>
	 *
	 * <p>
	 * 作为测试用模块启动不会对请求进行验签.
	 * </p>
	 * <p>
	 * 作为测试用模块启动所有支付信息不会去实际验证是否成功, 每笔支付有50%的几率成功或失败!
	 * </p>
	 */
	public boolean isTest() {
		return false;
	}

}
