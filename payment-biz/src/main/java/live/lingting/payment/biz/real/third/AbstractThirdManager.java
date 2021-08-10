package live.lingting.payment.biz.real.third;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import live.lingting.payment.biz.config.PaymentConfig;
import live.lingting.payment.biz.service.PayConfigService;
import live.lingting.payment.entity.PayConfig;
import live.lingting.payment.enums.ResponseCode;
import live.lingting.payment.exception.PaymentException;
import live.lingting.payment.sdk.enums.ThirdPart;

/**
 * @author lingting 2021/8/10 10:11
 */
public abstract class AbstractThirdManager<T> {

	@Autowired
	protected PayConfigService service;

	@Autowired
	protected PaymentConfig paymentConfig;

	private Map<String, T> cache;

	@PostConstruct
	public void init() {
		List<PayConfig> list = service.listByThird(getThird());

		if (CollectionUtils.isEmpty(list)) {
			return;
		}
		cache = new ConcurrentHashMap<>(list.size());

		for (PayConfig config : list) {
			T t = convertFrom(config);
			cache.put(config.getMark(), t);
		}
	}

	public T get(String mark) throws PaymentException {
		T t = cache.get(mark);
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

}
