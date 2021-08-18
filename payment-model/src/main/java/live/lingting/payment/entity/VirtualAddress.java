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
import live.lingting.payment.sdk.enums.Chain;

/**
 * @author lingting 2021/6/7 15:37
 */
@Getter
@Setter
@ApiModel("地址")
@Accessors(chain = true)
@TableName(value = "lingting_payment_virtual_address", autoResultMap = true)
public class VirtualAddress {

	@TableId
	@ApiModelProperty("地址id")
	private Integer id;

	@ApiModelProperty("链")
	private Chain chain;

	@ApiModelProperty("地址")
	private String address;

	/**
	 * 是否禁用
	 */
	@ApiModelProperty("是否禁用")
	private Boolean disabled;

	/**
	 * 是否使用中
	 */
	@ApiModelProperty("是否使用中")
	@TableField("`using`")
	private Boolean using;

	/**
	 * USDT 余额
	 */
	@ApiModelProperty("USDT 余额")
	private BigDecimal usdtAmount;

	@TableField(fill = FieldFill.INSERT)
	private LocalDateTime createTime;

}
