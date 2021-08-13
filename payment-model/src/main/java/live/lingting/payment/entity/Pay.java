package live.lingting.payment.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import live.lingting.payment.sdk.enums.Chain;
import live.lingting.payment.sdk.enums.Currency;
import live.lingting.payment.sdk.enums.Mode;
import live.lingting.payment.sdk.enums.NotifyStatus;
import live.lingting.payment.sdk.enums.PayStatus;
import live.lingting.payment.sdk.enums.ThirdPart;

/**
 * 支付信息
 *
 * @author lingting 2021/6/4 11:05
 */
@Getter
@Setter
@ApiModel("支付信息")
@Accessors(chain = true)
@TableName("lingting_payment_pay")
public class Pay {

	/**
	 * 交易号
	 */
	@ApiModelProperty("交易号")
	@TableId(type = IdType.ASSIGN_UUID)
	private String tradeNo;

	/**
	 * 所属项目
	 */
	@ApiModelProperty("所属项目")
	private Integer projectId;

	/**
	 * 项目交易号
	 */
	@ApiModelProperty("项目交易号")
	private String projectTradeNo;

	/**
	 * 第三方交易号
	 */
	@ApiModelProperty("第三方交易号")
	private String thirdPartTradeNo;

	/**
	 * 支付状态
	 */
	@ApiModelProperty("支付状态")
	private PayStatus status;

	/**
	 * 支付金额
	 */
	@ApiModelProperty("支付金额")
	private BigDecimal amount;

	/**
	 * 支付货币
	 */
	@ApiModelProperty("支付货币")
	private Currency currency;

	/**
	 * 链
	 */
	@ApiModelProperty("链")
	private Chain chain;

	/**
	 * 收款地址
	 */
	@ApiModelProperty("收款地址")
	private String address;

	/**
	 * 重试截止时间
	 */
	@ApiModelProperty("重试截止时间")
	private LocalDateTime retryEndTime;

	/**
	 * 第三方
	 */
	@ApiModelProperty("第三方")
	private ThirdPart thirdPart;

	/**
	 * 支付模式
	 */
	@ApiModelProperty("支付模式")
	private Mode mode;

	/**
	 * 支付配置标识
	 */
	@ApiModelProperty("支付配置标识")
	private String configMark;

	/**
	 * 描述
	 */
	@ApiModelProperty("描述")
	@TableField("`desc`")
	private String desc;

	/**
	 * 通知地址
	 */
	@ApiModelProperty("通知地址")
	private String notifyUrl;

	/**
	 * 通知状态, 当某个回调成功时: SUCCESS, 所有回调失败时: FAIL, 还有剩余回调次数时: WAIT
	 */
	@ApiModelProperty("通知状态")
	private NotifyStatus notifyStatus;

	@ApiModelProperty("完成时间")
	private LocalDateTime completeTime;

	@ApiModelProperty("创建时间")
	@TableField(fill = FieldFill.INSERT)
	private LocalDateTime createTime;

}
