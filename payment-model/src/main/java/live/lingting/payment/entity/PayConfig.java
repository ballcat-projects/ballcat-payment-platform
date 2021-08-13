package live.lingting.payment.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import live.lingting.payment.sdk.enums.ThirdPart;

/**
 * @author lingting 2021/8/10 10:13
 */
@Getter
@Setter
@ApiModel("支付配置")
@Accessors(chain = true)
@TableName(value = "lingting_payment_config")
public class PayConfig {

	@TableId
	@ApiModelProperty("配置id")
	private Integer id;

	/**
	 * 标识
	 */
	@ApiModelProperty("标识")
	@NotEmpty(message = "支付配置标识不能为空!")
	private String mark;

	@NotNull(message = "请选择所属第三方!")
	@ApiModelProperty("第三方")
	private ThirdPart thirdPart;

	@ApiModelProperty("支付宝-appId")
	private String aliAppId;

	@ApiModelProperty("支付宝-私钥")
	private String aliPrivateKey;

	@ApiModelProperty("支付宝-支付公钥")
	private String aliPayPublicKey;

	@ApiModelProperty("支付宝-返回值格式")
	private String aliFormat;

	@ApiModelProperty("支付宝-字符集")
	private String aliCharset;

	@ApiModelProperty("支付宝-签名类型")
	private String aliSignType;

	@ApiModelProperty("微信-appId")
	private String wxAppId;

	@ApiModelProperty("微信-商户id")
	private String wxMchId;

	@ApiModelProperty("微信-商户key")
	private String wxMchKey;

	@TableLogic
	@ApiModelProperty("是否删除")
	private Long deleted;

}
