package live.lingting.payment.ali.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import lombok.experimental.Accessors;
import live.lingting.payment.ali.AliPay;
import live.lingting.payment.ali.enums.TradeStatus;
import live.lingting.payment.util.JacksonUtils;

/**
 * @author lingting 2021/1/26 13:31
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class AliPayCallback {

	@JsonIgnore
	private Map<String, String> raw;

	private String gmtCreate;

	private String charset;

	private String sellerEmail;

	private String subject;

	private String sign;

	private String buyerId;

	private BigDecimal invoiceAmount;

	private String notifyId;

	private List<FundBill> fundBillList;

	private String notifyType;

	private TradeStatus tradeStatus;

	private BigDecimal receiptAmount;

	private String appId;

	private BigDecimal buyerPayAmount;

	private String signType;

	private String sellerId;

	private String gmtPayment;

	private String notifyTime;

	private String version;

	private String outTradeNo;

	private BigDecimal totalAmount;

	private String tradeNo;

	private String authAppId;

	private String buyerLogonId;

	private BigDecimal pointAmount;

	/**
	 * 解析回调参数
	 * @param callbackParams 所有回调参数
	 * @return live.lingting.payment.ali.domain.AliPayCallback
	 * @author lingting 2021-01-26 14:39
	 */
	public static AliPayCallback of(Map<String, String> callbackParams) throws JsonProcessingException {

		Map<String, Object> map = new HashMap<>(callbackParams);
		String fundBillListStr = callbackParams.get("fund_bill_list").replaceAll("&quot;", "\"");
		map.put("fund_bill_list", JacksonUtils.toObj(fundBillListStr, List.class));
		// 覆盖原值
		callbackParams.put("fund_bill_list", fundBillListStr);
		return JacksonUtils.toObj(JacksonUtils.toJson(map), AliPayCallback.class).setRaw(callbackParams);
	}

	@SneakyThrows
	public boolean checkSign(AliPay aliPay) {
		return aliPay.checkSignV1(getRaw()) || aliPay.checkSignV2(getRaw());
	}

	@Data
	public static class FundBill {

		private BigDecimal amount;

		private String fundChannel;

	}

}
