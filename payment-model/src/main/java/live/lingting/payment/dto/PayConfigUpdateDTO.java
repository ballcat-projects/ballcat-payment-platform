package live.lingting.payment.dto;

import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.NotNull;
import lombok.Data;
import live.lingting.payment.entity.PayConfig;

/**
 * @author lingting 2021/8/20 16:12
 */
@Data
@ApiModel("支付配置更新参数")
public class PayConfigUpdateDTO {

	@TableId
	@NotNull(message = "请指定要更新的配置ID!")
	@ApiModelProperty("配置id")
	private Integer id;

	@NotNull(message = "请选择是否禁用")
	@ApiModelProperty("是否禁用")
	private Boolean disabled;

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

	public PayConfig toEntity() {
		return new PayConfig().setId(getId()).setDisabled(getDisabled()).setAliAppId(getAliAppId())
				.setAliPrivateKey(getAliPrivateKey()).setAliPayPublicKey(getAliPayPublicKey())
				.setAliFormat(getAliFormat()).setAliCharset(getAliCharset()).setAliSignType(getAliSignType())
				.setWxAppId(getWxAppId()).setWxMchId(getWxMchId()).setWxMchKey(getWxMchKey());
	}

}
