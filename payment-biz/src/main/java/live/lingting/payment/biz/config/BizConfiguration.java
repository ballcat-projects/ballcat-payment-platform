package live.lingting.payment.biz.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author lingting 2021/8/3 下午9:38
 */
@Configuration
public class BizConfiguration {

	@Bean
	@ConditionalOnMissingBean
	public PaymentConfig paymentConfig() {
		return new PaymentConfig();
	}

}
