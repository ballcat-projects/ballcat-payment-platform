package live.lingting.payment.rate;

import java.math.BigDecimal;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import live.lingting.payment.sdk.enums.Currency;

/**
 * @author lingting 2021/6/11 15:53
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class Rate {

	public static final BigDecimal CNY_RATE = BigDecimal.ONE;

	public static final String CNY_CODE = "CNY";

	private final List<BaseRate> rates;

	/**
	 * 获取指定货币兑人民币汇率
	 * @param currency 货币
	 * @return java.math.BigDecimal
	 * @author lingting 2021-06-11 15:54
	 */
	public BigDecimal get(Currency currency) {
		// usdt 按美元处理
		if (Currency.USDT.equals(currency)) {
			return get("USD");
		}
		return get(currency.name());
	}

	/**
	 * 根据货币代码获取兑人民币汇率
	 * @param code 货币代码
	 * @return java.math.BigDecimal, 返回值可能为 null 需要做好非空交易
	 * @author lingting 2021-08-12 14:12
	 */
	@Nullable
	public BigDecimal get(String code) {
		if (CNY_CODE.equals(code)) {
			return CNY_RATE;
		}
		BigDecimal decimal;
		for (BaseRate rate : rates) {
			try {
				decimal = rate.get(code);
				if (decimal != null) {
					return decimal;
				}
			}
			catch (Exception e) {
				log.error("获取汇率时异常! 执行类: {}", rate.getClass(), e);
			}
		}
		return null;
	}

}
