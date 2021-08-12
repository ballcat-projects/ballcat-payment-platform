package live.lingting.payment.rate;

import java.math.BigDecimal;
import java.util.concurrent.TimeUnit;

/**
 * @author lingting 2021/6/21 17:07
 */
public interface BaseRate {

	/**
	 * 请求连接超时, 5s
	 */
	Long CONNECT_TIMEOUT = TimeUnit.SECONDS.toMillis(5);

	/**
	 * 请求返回值读取超时, 10s
	 */
	Long READ_TIMEOUT = TimeUnit.SECONDS.toMillis(10);

	/**
	 * 获取指定货币代码兑人民币汇率
	 * @param code 货币代码
	 * @return java.math.BigDecimal
	 * @author lingting 2021-06-11 15:54
	 */
	BigDecimal get(String code);

}
