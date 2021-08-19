package live.lingting.payment.biz.real.third;

import com.alipay.api.internal.util.AlipayLogger;
import org.springframework.stereotype.Component;
import live.lingting.payment.ali.AliPay;
import live.lingting.payment.ali.constants.AliPayConstant;
import live.lingting.payment.entity.PayConfig;
import live.lingting.payment.sdk.enums.ThirdPart;

/**
 * @author lingting 2021/8/10 11:21
 */
@Component
public class AliManager extends AbstractThirdManager<AliPay> {

	@Override
	public void init() {
		super.init();
		// 关闭异常日志
		AlipayLogger.setNeedEnableLogger(false);
	}

	@Override
	public ThirdPart getThird() {
		return ThirdPart.ALI;
	}

	@Override
	public AliPay convertFrom(PayConfig config) {
		return new AliPay(AliPayConstant.SERVER_URL_PROD, config.getAliAppId(), config.getAliPrivateKey(),
				config.getAliFormat(), config.getAliCharset(), config.getAliPayPublicKey(), config.getAliSignType());
	}

}
