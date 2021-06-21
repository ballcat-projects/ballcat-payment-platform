package live.lingting.rate;

import java.math.BigDecimal;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import live.lingting.sdk.enums.Currency;

/**
 * @author lingting 2021/6/11 15:53
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class Rate {

	public static final BigDecimal CNY_RATE = BigDecimal.ONE;

	private final List<BaseRate> rates;

	/**
	 * 获取指定货币汇率
	 * @param currency 货币
	 * @return java.math.BigDecimal
	 * @author lingting 2021-06-11 15:54
	 */
	public BigDecimal get(Currency currency) {
		if (currency == Currency.CNY) {
			return CNY_RATE;
		}
		BigDecimal decimal;
		for (BaseRate rate : rates) {
			try {
				decimal = rate.get(currency);
				if (decimal != null) {
					return decimal;
				}
			}
			catch (Exception ignored) {
				// ignored
			}
		}
		return null;
	}

}
