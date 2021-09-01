package live.lingting.payment.sdk.model;

import java.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;
import org.springframework.util.StringUtils;
import live.lingting.payment.sdk.enums.Currency;
import live.lingting.payment.sdk.enums.Mode;
import live.lingting.payment.sdk.enums.ThirdPart;
import live.lingting.payment.sdk.exception.MixException;
import live.lingting.payment.sdk.exception.MixRequestParamsValidException;

/**
 * 查询
 *
 * @author lingting 2021/6/7 17:17
 */
@Getter
@Setter
public class MixRealPayModel extends MixModel {

	public static final BigDecimal MIN = new BigDecimal("0.01");

	private static final long serialVersionUID = 1L;

	/**
	 * 商品信息
	 */
	private String subject;

	private ThirdPart thirdPart;

	/**
	 * 支付配置标识
	 */
	private String mark;

	private Mode mode;

	private Currency currency;

	private BigDecimal amount;

	private String thirdPartTradeNo;

	@Override
	public void valid() throws MixException {
		validNotifyUrl();

		String msg = "";
		if (!Currency.REAL_LIST.contains(getCurrency())) {
			msg = "暂不支持该货币: " + (getCurrency() == null ? "" : getCurrency().name());
		}
		else if (!StringUtils.hasText(getProjectTradeNo())) {
			msg = "项目交易号不能为空";
		}
		else if (!StringUtils.hasText(getMark())) {
			msg = "支付配置标识不能为空";
		}
		else if (ThirdPart.BC_UNKNOWN.equals(getThirdPart())) {
			msg = "该第三方不支持支付操作";
		}
		else if (Mode.QR.equals(getMode())) {
			if (getAmount() == null || getAmount().compareTo(BigDecimal.ZERO) < 1) {
				msg = "支付金额异常";
			}
			else if (getAmount().compareTo(MIN) < 0) {
				msg = "支付金额最小值为" + MIN.toPlainString();
			}
			else if (!StringUtils.hasText(getSubject())) {
				msg = "商品信息不能为空!";
			}
		}
		else if (Mode.TRANSFER.equals(getMode())) {
			if (!StringUtils.hasText(thirdPartTradeNo)) {
				msg = "第三方交易号不能为空!";
			}
		}

		if (StringUtils.hasText(msg)) {
			throw new MixRequestParamsValidException(msg);
		}
	}

}
