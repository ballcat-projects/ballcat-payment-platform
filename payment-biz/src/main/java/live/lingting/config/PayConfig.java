package live.lingting.config;

import static live.lingting.constant.SystemConfigConstants.PAY_EMPTY_INFO_TIMEOUT;
import static live.lingting.constant.SystemConfigConstants.PAY_EMPTY_INFO_TIMEOUT_DEFAULT;
import static live.lingting.constant.SystemConfigConstants.PAY_FAIL_TIMEOUT;
import static live.lingting.constant.SystemConfigConstants.PAY_FAIL_TIMEOUT_DEFAULT;
import static live.lingting.constant.SystemConfigConstants.PAY_INFO_CREATE_TIMEOUT;
import static live.lingting.constant.SystemConfigConstants.PAY_INFO_CREATE_TIMEOUT_DEFAULT;
import static live.lingting.constant.SystemConfigConstants.PAY_RETRY_TIMEOUT;
import static live.lingting.constant.SystemConfigConstants.PAY_RETRY_TIMEOUT_DEFAULT;
import static live.lingting.constant.SystemConfigConstants.REAL_EXPIRE_TIMEOUT;
import static live.lingting.constant.SystemConfigConstants.REAL_EXPIRE_TIMEOUT_DEFAULT;
import static live.lingting.constant.SystemConfigConstants.TEST;
import static live.lingting.constant.SystemConfigConstants.TEST_DEFAULT;
import static live.lingting.constant.SystemConfigConstants.VIRTUAL_SUBMIT_HASH_TIMEOUT;
import static live.lingting.constant.SystemConfigConstants.VIRTUAL_SUBMIT_HASH_TIMEOUT_DEFAULT;

import cn.hutool.core.convert.Convert;
import com.hccake.ballcat.system.mapper.SysConfigMapper;
import com.hccake.ballcat.system.service.SysConfigService;
import com.hccake.ballcat.system.service.impl.SysConfigServiceImpl;
import lombok.RequiredArgsConstructor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import live.lingting.constant.SystemConfigConstants;
import live.lingting.util.SpringUtils;

/**
 * @author lingting 2021/6/18 9:39
 */
@Component
@Import(SysConfigServiceImpl.class)
@MapperScan(basePackageClasses = SysConfigMapper.class)
@RequiredArgsConstructor
public class PayConfig {

	private final SysConfigService service;

	/**
	 * 虚拟货币提交Hash超时时间, 单位: 分钟
	 * @author lingting 2021-06-18 09:47
	 */
	public Long getVirtualSubmitTimeout() {
		return getByKey(VIRTUAL_SUBMIT_HASH_TIMEOUT, VIRTUAL_SUBMIT_HASH_TIMEOUT_DEFAULT);
	}

	/**
	 * 未获取到交易信息等待时长。单位: 分钟
	 * @author lingting 2021-06-18 09:54
	 */
	public Long getEmptyInfoTimeout() {
		return getByKey(PAY_EMPTY_INFO_TIMEOUT, PAY_EMPTY_INFO_TIMEOUT_DEFAULT);
	}

	/**
	 * 失败延时。单位: 分钟
	 * @author lingting 2021-06-18 09:54
	 */
	public Long getFailTimeout() {
		return getByKey(PAY_FAIL_TIMEOUT, PAY_FAIL_TIMEOUT_DEFAULT);
	}

	/**
	 * 交易信息创建时间最大限制。单位: 分钟
	 * @author lingting 2021-06-18 09:54
	 */
	public Long getInfoCreateTimeout() {
		return getByKey(PAY_INFO_CREATE_TIMEOUT, PAY_INFO_CREATE_TIMEOUT_DEFAULT);
	}

	/**
	 * 支付重试时长。单位: 分钟
	 * @author lingting 2021-06-18 09:54
	 */
	public Long getRetryTimeout() {
		return getByKey(PAY_RETRY_TIMEOUT, PAY_RETRY_TIMEOUT_DEFAULT);
	}

	/**
	 * 真实货币支付过期时间 - 指定时间内未付款记为失败
	 *
	 * mode={@link live.lingting.sdk.enums.Mode#TRANSFER} 时, 不受本值限制
	 * @author lingting 2021-07-14 15:34
	 */
	public Long getRealExpireTimeout() {
		return getByKey(REAL_EXPIRE_TIMEOUT, REAL_EXPIRE_TIMEOUT_DEFAULT);
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
		if (SpringUtils.isProd()) {
			return false;
		}
		final Long val = getByKey(TEST, TEST_DEFAULT);
		return !val.equals(TEST_DEFAULT);
	}

	private Long getByKey(String key, Long defaultVal) {
		final String value = service.getConfValueByKey(key);
		Long v;

		if (!StringUtils.hasText(value) || (v = Convert.toLong(value)) < SystemConfigConstants.MIN) {
			return defaultVal;
		}

		return v;
	}

}
