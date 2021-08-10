package live.lingting.payment.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import live.lingting.payment.sdk.enums.ThirdPart;

/**
 * @author lingting 2021/8/10 10:13
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName(value = "lingting_payment_config")
public class PayConfig {

	@TableId
	private Integer id;

	/**
	 * 标志
	 */
	private String mark;

	private ThirdPart thirdPart;

	private String aliAppId;

	private String aliPrivateKey;

	private String aliPayPublicKey;

	private String aliFormat;

	private String aliCharset;

	private String aliSignType;

	private String wxAppId;

	private String wxMchId;

	private String wxMchKey;

	@TableLogic
	private Long deleted;

}
