package live.lingting.rate;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author lingting 2021/6/11 15:55
 */
@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties(RateProperties.class)
public class RateConfiguration {

	@ConditionalOnMissingBean(Rate.class)
	@ConditionalOnProperty(prefix = "mix.rate", value = "code")
	public Rate defaultRate(RateProperties properties) {
		return new YyRate(properties);
	}

}
