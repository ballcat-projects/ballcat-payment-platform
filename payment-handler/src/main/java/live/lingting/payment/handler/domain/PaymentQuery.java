package live.lingting.payment.handler.domain;

import java.math.BigDecimal;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author lingting 2021/8/19 11:21
 */
@Data
@Accessors(chain = true)
public class PaymentQuery {

	/**
	 * 第三方交易号
	 */
	private String thirdTradeNo;

	/**
	 * 交易金额
	 */
	private BigDecimal amount;

	/**
	 * 交易是否成功
	 */
	private Boolean success;


}
