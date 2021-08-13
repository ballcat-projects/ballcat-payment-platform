package live.lingting.payment.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
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
import live.lingting.payment.sdk.enums.NotifyStatus;

/**
 * 通知
 *
 * @author lingting 2021/6/10 16:22
 */
@Getter
@Setter
@ApiModel("通知")
@Accessors(chain = true)
@TableName("lingting_payment_notify")
public class Notify {

	@TableId
	@ApiModelProperty("通知id")
	private Long id;

	/**
	 * 所属项目
	 */
	@ApiModelProperty("所属项目")
	private Integer projectId;

	/**
	 * 交易号
	 */
	@ApiModelProperty("交易号")
	private String tradeNo;

	@ApiModelProperty("通知地址")
	private String notifyUrl;

	/**
	 * 通知状态
	 */
	@ApiModelProperty("通知状态")
	private NotifyStatus status;

	/**
	 * 下次通知时间
	 */
	@ApiModelProperty("下次通知时间")
	private LocalDateTime nextTime;

	/**
	 * 通知次数
	 */
	@ApiModelProperty("通知次数")
	private Integer count;

	/**
	 * 汇率
	 */
	@ApiModelProperty("汇率")
	private BigDecimal rate;

	@TableField(fill = FieldFill.INSERT)
	private LocalDateTime createTime;

}
