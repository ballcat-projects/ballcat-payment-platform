package live.lingting.sdk.domain;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;
import live.lingting.sdk.enums.Chain;
import live.lingting.sdk.enums.Currency;
import live.lingting.sdk.enums.Mode;
import live.lingting.sdk.enums.NotifyStatus;
import live.lingting.sdk.enums.PayStatus;
import live.lingting.sdk.enums.ThirdPart;
import live.lingting.sdk.util.MixUtils;

/**
 * @author lingting 2021/6/16 22:11
 */
@Getter
@Setter
public class MixCallback {

	/**
	 * 交易号
	 */
	private String tradeNo;

	/**
	 * 所属项目
	 */
	private Integer projectId;

	/**
	 * 项目交易号
	 */
	private String projectTradeNo;

	/**
	 * 第三方交易号
	 */
	private String thirdPartTradeNo;

	/**
	 * 支付状态
	 */
	private PayStatus status;

	/**
	 * 支付金额
	 */
	private BigDecimal amount;

	/**
	 * 支付货币
	 */
	private Currency currency;

	/**
	 * 链
	 */
	private Chain chain;

	/**
	 * 收款地址
	 */
	private String address;

	/**
	 * 重试截止时间
	 */
	private LocalDateTime retryEndTime;

	/**
	 * 第三方
	 */
	private ThirdPart thirdPart;

	/**
	 * 支付模式
	 */
	private Mode mode;

	/**
	 * 描述
	 */
	private String desc;

	/**
	 * 通知地址
	 */
	private String notifyUrl;

	/**
	 * 通知状态, 当某个回调成功时: SUCCESS, 所有回调失败时: FAIL, 还有剩余回调次数时: WAIT
	 */
	private NotifyStatus notifyStatus;

	private LocalDateTime completeTime;

	private LocalDateTime createTime;

	private String key;

	private String nonce;

	private String sign;

	/**
	  * 回调验签
	  * @author lingting 2021-06-16 22:19
	  */
	public boolean verifySign(String security) {
		return MixUtils.verifySign(security, this);
	}

}
