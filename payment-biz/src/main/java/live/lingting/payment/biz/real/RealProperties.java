package live.lingting.payment.biz.real;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author lingting 2021/7/14 14:52
 */
@Getter
@Setter
@ConfigurationProperties("mix.real")
public class RealProperties {

	private Ali ali;

	private Wx wx;

	@Getter
	@Setter
	public static class Ali {

		private String appId;

		/**
		 * rsa私钥(应用私钥)
		 */
		private String privateKey;

		private String format = "json";

		private String charset = "utf-8";

		/**
		 * 支付宝公钥
		 */
		private String alipayPublicKey;

		private String signType = "RSA2";

		private String returnUrl;

		private String notifyUrl;

	}

	@Getter
	@Setter
	public static class Wx {

		private String appId;

		private String mchId;

		private String mckKey;

		private String returnUrl;

		private String notifyUrl;

	}

}
