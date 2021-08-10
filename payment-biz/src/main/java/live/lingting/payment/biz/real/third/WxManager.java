package live.lingting.payment.biz.real.third;

import org.springframework.stereotype.Component;
import live.lingting.payment.entity.PayConfig;
import live.lingting.payment.sdk.enums.ThirdPart;
import live.lingting.payment.wx.WxPay;

/**
 * @author lingting 2021/8/10 11:21
 */
@Component
public class WxManager extends AbstractThirdManager<WxPay> {

	@Override
	public ThirdPart getThird() {
		return ThirdPart.ALI;
	}

	@Override
	public WxPay convertFrom(PayConfig config) {
		WxPay pay = new WxPay(config.getWxAppId(), config.getWxMchId(), config.getWxMchKey(), false);
		pay.setNotifyUrl(paymentConfig.getWxNotifyUrl());
		return pay;
	}

}
