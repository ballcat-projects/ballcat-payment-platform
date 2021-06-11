package live.lingting.rate;

import java.math.BigDecimal;
import live.lingting.sdk.enums.Currency;

/**
 * @author lingting 2021/6/11 15:53
 */
public interface Rate {

	BigDecimal CNY_RATE = BigDecimal.ONE;

	/**
	 * 获取指定货币汇率
	 * @param currency 货币
	 * @return java.math.BigDecimal
	 * @author lingting 2021-06-11 15:54
	 */
	BigDecimal get(Currency currency);

}
