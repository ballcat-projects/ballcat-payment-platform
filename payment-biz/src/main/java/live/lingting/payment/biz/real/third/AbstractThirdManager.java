package live.lingting.payment.biz.real.third;

import static org.slf4j.LoggerFactory.getLogger;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.beans.factory.annotation.Autowired;
import live.lingting.payment.biz.Redis;
import live.lingting.payment.biz.config.PaymentConfig;
import live.lingting.payment.biz.service.PayConfigService;
import live.lingting.payment.entity.PayConfig;
import live.lingting.payment.enums.ResponseCode;
import live.lingting.payment.exception.PaymentException;
import live.lingting.payment.pay.ThirdPay;
import live.lingting.payment.sdk.enums.ThirdPart;

/**
 * @author lingting 2021/8/10 10:11
 */
public abstract class AbstractThirdManager<T extends ThirdPay> {

	private static final String CONFIG_UPDATE_KEY = "live:lingting:payment:config:";

	@Autowired
	protected PayConfigService service;

	@Autowired
	protected PaymentConfig paymentConfig;

	@Autowired
	protected Redis redis;

	/**
	 * 用于标记是否更新
	 */
	private String updateVal;

	/**
	 * 可正常使用的
	 */
	private final Map<String, T> normal = new ConcurrentHashMap<>(16);

	/**
	 * 禁用的
	 */
	private final Map<String, T> disabled = new ConcurrentHashMap<>(16);

	/**
	 * 被删除的
	 */
	private final Map<String, T> deleted = new ConcurrentHashMap<>(16);

	public void reload() {
		synchronized (normal) {
			// 防止并发下, 一次更新, 多次重新加载
			if (updateVal.equals(redis.get(getConfigUpdateKey(getThird())))) {
				return;
			}
			List<String> marks = new ArrayList<>(16);
			// 正常和被禁用
			List<PayConfig> list = service.listByThird(getThird());
			for (PayConfig config : list) {
				put(config.getMark(), config);
				marks.add(config.getMark());
			}
			// 不在已加载范围内的被删除配置
			list = service.listDeletedByIgnore(marks);
			for (PayConfig config : list) {
				put(config.getMark(), config);
			}
			updateVal = redis.get(getConfigUpdateKey(getThird()));
		}
	}

	/**
	 * 重新加载指定标识的配置
	 * @author lingting 2021-08-20 16:48
	 */
	public void reload(String... marks) {
		for (String mark : marks) {
			put(mark, service.getByMarkAndThird(mark, getThird()));
		}
	}

	private void init() {
		// 更新标记不存在或者与缓存不一致
		if (updateVal == null || !updateVal.equals(redis.get(getConfigUpdateKey(getThird())))) {
			reload();
		}
	}

	public T get(String mark) throws PaymentException {
		// 初始化
		init();
		T t = normal.get(mark);
		if (t == null) {
			if (disabled.containsKey(mark)) {
				throw new PaymentException(ResponseCode.PAYMENT_CONFIG_DISABLED);
			}
			throw new PaymentException(ResponseCode.PAYMENT_CONFIG_NOT_FOUND);
		}
		t.setNotifyUrl(paymentConfig.getNotifyUrl(getThird()));
		return t;
	}

	/**
	 * <p>
	 * 获取配置 - 正常或者禁用或者被删除
	 * </p>
	 * <p>
	 * 仅用于回调处理和失效交易处理
	 * </p>
	 * @author lingting 2021-08-20 17:19
	 */
	public T getAll(String mark) throws PaymentException {
		// 初始化
		init();
		// 获取正常的
		T t = normal.get(mark);

		// 禁用的
		if (t == null) {
			t = disabled.get(mark);
		}

		// 被删除的
		if (t == null) {
			t = deleted.get(mark);
		}

		if (t == null) {
			throw new PaymentException(ResponseCode.PAYMENT_CONFIG_NOT_FOUND);
		}
		return t;
	}

	/**
	 * 获取第三方
	 * @return live.lingting.payment.sdk.enums.ThirdPart
	 * @author lingting 2021-08-10 11:05
	 */
	public abstract ThirdPart getThird();

	/**
	 * 从支付配置中转换
	 * @param config 支付配置
	 * @return T
	 * @author lingting 2021-08-10 11:09
	 */
	public abstract T convertFrom(PayConfig config);

	private void put(String mark, PayConfig config) {
		if (config == null) {
			getLogger(getClass()).error("支付配置加载异常! 标识: {}, 第三方: {}", mark, getThird());
			return;
		}
		String key = config.getMark();
		T t = convertFrom(config);

		// 删除
		if (config.getDeleted() > 0) {
			deleted.put(key, t);
			// 从正常和禁用中移除
			normal.remove(key);
			disabled.remove(key);
		}
		// 禁用
		else if (config.getDisabled()) {
			disabled.put(key, t);
			// 从正常和删除中移除
			normal.remove(key);
			deleted.remove(key);
		}
		// 正常
		else {
			normal.put(config.getMark(), t);
			// 从删除和禁用中移除
			deleted.remove(key);
			disabled.remove(key);
		}
	}

	/**
	 * 获取配置更新key
	 * @author lingting 2021-08-25 22:08
	 */
	public static String getConfigUpdateKey(ThirdPart tp) {
		return CONFIG_UPDATE_KEY + tp.name();
	}

}
