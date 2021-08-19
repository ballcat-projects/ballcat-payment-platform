package live.lingting.payment.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import live.lingting.payment.sdk.enums.Chain;
import live.lingting.payment.sdk.enums.Currency;
import live.lingting.payment.sdk.enums.Mode;
import live.lingting.payment.sdk.enums.NotifyStatus;
import live.lingting.payment.sdk.enums.PayStatus;
import live.lingting.payment.sdk.enums.ThirdPart;

/**
 * @author lingting 2021/8/19 10:21
 */
@Data
@ApiModel("支付信息列表返回")
public class PayVO {

	@ApiModelProperty("交易号")
	@TableId(type = IdType.ASSIGN_UUID)
	private String tradeNo;

	@ApiModelProperty("所属项目")
	private Integer projectId;

	@ApiModelProperty("所属项目名")
	private String projectName;

	@ApiModelProperty("项目交易号")
	private String projectTradeNo;

	@ApiModelProperty("第三方交易号")
	private String thirdPartTradeNo;

	@ApiModelProperty("支付状态")
	private PayStatus status;

	@ApiModelProperty("支付金额")
	private BigDecimal amount;

	@ApiModelProperty("支付货币")
	private Currency currency;

	@ApiModelProperty("链")
	private Chain chain;

	@ApiModelProperty("收款地址")
	private String address;

	@ApiModelProperty("重试截止时间")
	private LocalDateTime retryEndTime;

	@ApiModelProperty("第三方")
	private ThirdPart thirdPart;

	@ApiModelProperty("支付模式")
	private Mode mode;

	@ApiModelProperty("支付配置标识")
	private String configMark;

	@ApiModelProperty("描述")
	private String desc;

	@ApiModelProperty("通知地址")
	private String notifyUrl;

	@ApiModelProperty("通知状态")
	private NotifyStatus notifyStatus;

	@ApiModelProperty("完成时间")
	private LocalDateTime completeTime;

	@ApiModelProperty("创建时间")
	private LocalDateTime createTime;

}
