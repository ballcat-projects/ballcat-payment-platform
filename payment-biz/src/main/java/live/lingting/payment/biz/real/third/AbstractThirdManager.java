package live.lingting.payment.biz.real.third;

import static org.slf4j.LoggerFactory.getLogger;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.beans.factory.annotation.Autowired;
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

	@Autowired
	protected PayConfigService service;

	@Autowired
	protected PaymentConfig paymentConfig;

	private boolean init = false;

	private final Map<String, T> cache = new ConcurrentHashMap<>(16);

	public void reload() {
		synchronized (cache) {
			List<PayConfig> list = service.listByThird(getThird());
			for (PayConfig config : list) {
				T t = convertFrom(config);
				cache.put(config.getMark(), t);
			}
			init = true;
		}
	}

	/**
	 * 重新加载指定标识的配置
	 * @author lingting 2021-08-20 16:48
	 */
	public void reload(String... marks) {
		for (String mark : marks) {
			PayConfig config = service.getByMarkAndThird(mark, getThird());
			if (config == null) {
				getLogger(getClass()).error("支付配置加载异常! 标识: {}, 第三方: {}", mark, getThird());
				continue;
			}
			T t = convertFrom(config);
			cache.put(config.getMark(), t);
		}
	}

	public T get(String mark) throws PaymentException {
		// map 为空, 且未进行初始化
		if (cache.isEmpty() && !init) {
			reload();
		}
		T t = cache.get(mark);
		if (t == null) {
			throw new PaymentException(ResponseCode.PAYMENT_CONFIG_NOT_FOUND);
		}
		t.setNotifyUrl(paymentConfig.getNotifyUrl(getThird()));
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

}
