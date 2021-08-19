package live.lingting.payment.ali.domain;

import static live.lingting.payment.ali.constants.AliPayConstant.CODE_SUCCESS;

import com.alipay.api.response.AlipayTradeCloseResponse;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * @author lingting 2021/8/19 14:27
 */
@Getter
@ToString
@Accessors(chain = true)
@Setter(AccessLevel.PRIVATE)
public class AliPayClose {

	private AlipayTradeCloseResponse raw;

	private String code;

	private String msg;

	private String subCode;

	private String subMsg;

	/**
	 * 平台订单号
	 */
	private String sn;

	/**
	 * 支付宝订单号
	 */
	private String tradeNo;

	public boolean isSuccess() {
		return CODE_SUCCESS.equals(getCode());
	}

	public static AliPayClose of(AlipayTradeCloseResponse raw) {
		AliPayClose close = new AliPayClose();
		if (raw == null) {
			return close;
		}

		close.setRaw(raw).setCode(raw.getCode()).setMsg(raw.getMsg()).setSubCode(raw.getSubCode())
				.setSubMsg(raw.getSubMsg());
		return close.setTradeNo(raw.getTradeNo()).setSn(raw.getOutTradeNo());
	}

}
