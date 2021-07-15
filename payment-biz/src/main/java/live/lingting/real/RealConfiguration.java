package live.lingting.real;

import com.hccake.extend.pay.ali.AliPay;
import com.hccake.extend.pay.ali.constants.AliPayConstant;
import com.hccake.extend.pay.wx.WxPay;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author lingting 2021/7/14 14:52
 */
@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties(RealProperties.class)
public class RealConfiguration {

	private final RealProperties properties;

	@Bean
	public AliPay aliPay() {
		final RealProperties.Ali ali = properties.getAli();
		final AliPay aliPay = new AliPay(AliPayConstant.SERVER_URL_PROD, ali.getAppId(), ali.getPrivateKey(),
				ali.getFormat(), ali.getCharset(), ali.getAlipayPublicKey(), ali.getSignType());
		aliPay.setNotifyUrl(ali.getNotifyUrl());
		aliPay.setReturnUrl(ali.getReturnUrl());
		return aliPay;
	}

	@Bean
	public WxPay wxPay() {
		final RealProperties.Wx wx = properties.getWx();
		final WxPay wxPay = new WxPay(wx.getAppId(), wx.getMchId(), wx.getMckKey(), false);
		wxPay.setNotifyUrl(wx.getNotifyUrl());
		wxPay.setReturnUrl(wx.getReturnUrl());
		return wxPay;
	}

}
