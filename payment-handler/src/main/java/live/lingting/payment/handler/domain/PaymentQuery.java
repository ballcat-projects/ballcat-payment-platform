package live.lingting.payment.handler.domain;

import com.alipay.api.AlipayApiException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import lombok.Data;
import lombok.experimental.Accessors;
import live.lingting.payment.ali.AliPay;
import live.lingting.payment.ali.domain.AliPayQuery;
import live.lingting.payment.ali.enums.TradeStatus;
import live.lingting.payment.entity.Pay;
import live.lingting.payment.wx.WxPay;
import live.lingting.payment.wx.response.WxPayOrderQueryResponse;

/**
 * @author lingting 2021/8/19 11:21
 */
@Data
@Accessors(chain = true)
public class PaymentQuery {

	private static final BigDecimal HUNDRED = new BigDecimal("100");

	/**
	 * 第三方交易号
	 */
	private String thirdTradeNo;

	/**
	 * 交易金额
	 */
	private BigDecimal amount;

	/**
	 * 交易状态
	 */
	private Status status;

	private Object raw;

	public static PaymentQuery of(AliPay aliPay, Pay pay) throws AlipayApiException {
		AliPayQuery aliPayQuery = aliPay.query(pay.getTradeNo());
		PaymentQuery query = new PaymentQuery().setThirdTradeNo(aliPayQuery.getTradeNo())
				.setAmount(aliPayQuery.getAmount()).setRaw(aliPayQuery);

		if (aliPayQuery.getTradeStatus().equals(TradeStatus.SUCCESS)) {
			query.setStatus(Status.SUCCESS);
		}

		switch (aliPayQuery.getTradeStatus()) {
		case SUCCESS:
		case FINISHED:
			query.setStatus(Status.SUCCESS);
			break;
		case WAIT:
			query.setStatus(Status.WAIT);
			break;
		case CLOSED:
			query.setStatus(Status.CLOSED);
			break;
		default:
			query.setStatus(Status.UNKNOWN);
			break;
		}
		return query;
	}

	public static PaymentQuery of(WxPay wxPay, Pay pay) {
		WxPayOrderQueryResponse response = wxPay.query(pay.getTradeNo(), "");

		PaymentQuery query = new PaymentQuery().setThirdTradeNo(response.getTransactionId()).setRaw(response);
		if (response.getTotalFee() != null) {
			// 分转元
			query.setAmount(new BigDecimal(response.getTotalFee()).divide(HUNDRED, 4, RoundingMode.CEILING));
		}

		if (response.isSuccess()) {
			query.setStatus(Status.SUCCESS);
		}
		else if (response.getTradeState() != null) {
			switch (response.getTradeState()) {
			case CLOSED:
			case REFUND:
			case REVOKED:
				query.setStatus(Status.CLOSED);
				break;
			case ACCEPT:
			case NOTPAY:
			case USERPAYING:
				query.setStatus(Status.WAIT);
				break;
			case SUCCESS:
				query.setStatus(Status.SUCCESS);
				break;
			default:
				query.setStatus(Status.UNKNOWN);
				break;
			}
		}
		else {
			query.setStatus(Status.UNKNOWN);
		}

		return query;
	}

	public enum Status {

		/**
		 * 等待支付
		 */
		WAIT,
		/**
		 * 成功
		 */
		SUCCESS,
		/**
		 * 关闭
		 */
		CLOSED,
		/**
		 * 未知
		 */
		UNKNOWN,

	}

}
