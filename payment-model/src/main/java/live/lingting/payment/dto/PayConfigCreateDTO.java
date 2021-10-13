package live.lingting.payment.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import lombok.Data;
import live.lingting.payment.entity.PayConfig;
import live.lingting.payment.sdk.enums.ThirdPart;

/**
 * @author lingting 2021/8/20 16:12
 */
@Data
@ApiModel("支付配置创建参数")
public class PayConfigCreateDTO {

	@ApiModelProperty("标识")
	@NotEmpty(message = "支付配置不能为空!")
	private String mark;

	@NotNull(message = "请选择所属第三方!")
	@ApiModelProperty("第三方")
	private ThirdPart thirdPart;

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

	@ApiModelProperty("开户行")
	private String bank;

	@ApiModelProperty("银行卡-卡号")
	private String cardNumber;

	public PayConfig toEntity() {
		return new PayConfig().setMark(getMark()).setThirdPart(getThirdPart()).setDisabled(getDisabled())
				.setAliAppId(getAliAppId()).setAliPrivateKey(getAliPrivateKey())
				.setAliPayPublicKey(getAliPayPublicKey()).setAliFormat(getAliFormat()).setAliCharset(getAliCharset())
				.setAliSignType(getAliSignType()).setWxAppId(getWxAppId()).setWxMchId(getWxMchId())
				.setWxMchKey(getWxMchKey()).setBank(getBank()).setCardNumber(getCardNumber());
	}

}
