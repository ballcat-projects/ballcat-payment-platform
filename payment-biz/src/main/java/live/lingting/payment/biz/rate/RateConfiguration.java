package live.lingting.payment.biz.rate;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author lingting 2021/6/11 15:55
 */
@RequiredArgsConstructor
@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties(RateProperties.class)
public class RateConfiguration {

	private final RateProperties properties;

	@Bean
	@ConditionalOnMissingBean(YyRate.class)
	@ConditionalOnProperty(prefix = "mix.rate.yy", name = "code")
	public YyRate defaultRate() {
		return new YyRate(properties.getYy());
	}

}
