package live.lingting.payment.biz.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.baomidou.mybatisplus.core.incrementer.IdentifierGenerator;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import live.lingting.payment.biz.mybatis.FillMetaObjectHandle;
import live.lingting.payment.biz.mybatis.IdGenerator;

/**
 * @author lingting 2021/8/3 下午9:38
 */
@Configuration(proxyBeanMethods = false)
@ComponentScan(basePackages = "live.lingting")
@MapperScan(basePackages = "live.lingting.**.mapper")
public class PaymentBizConfiguration {

	@Bean
	@ConditionalOnMissingBean(PaymentConfig.class)
	public PaymentConfig liveLintingPaymentConfig() {
		return new PaymentConfig();
	}

	@Bean
	@ConditionalOnMissingBean(MetaObjectHandler.class)
	public MetaObjectHandler liveLintingPaymentMetaObjectHandler() {
		return new FillMetaObjectHandle();
	}

	@Bean
	@ConditionalOnMissingBean(IdentifierGenerator.class)
	public IdentifierGenerator liveLintingPaymentIdentifierGenerator() {
		return new IdGenerator();
	}

}
