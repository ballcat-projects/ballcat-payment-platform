package live.lingting.rate;

import java.math.BigDecimal;
import live.lingting.sdk.enums.Currency;

/**
 * @author lingting 2021/6/21 17:07
 */
public interface BaseRate {

	/**
	 * 获取指定货币汇率
	 * @param currency 货币
	 * @return java.math.BigDecimal
	 * @author lingting 2021-06-11 15:54
	 */
	BigDecimal get(Currency currency);

}
