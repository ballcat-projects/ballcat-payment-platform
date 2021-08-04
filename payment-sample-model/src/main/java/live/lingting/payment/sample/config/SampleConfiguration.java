package live.lingting.payment.sample.config;

import com.hccake.ballcat.system.mapper.SysConfigMapper;
import com.hccake.ballcat.system.service.SysConfigService;
import com.hccake.ballcat.system.service.impl.SysConfigServiceImpl;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import live.lingting.payment.biz.config.BizConfiguration;
import live.lingting.payment.biz.config.PaymentConfig;

/**
 * @author lingting 2021/8/4 下午1:58
 */
@MapperScan(basePackages = "live.lingting.**.mapper")
@ComponentScan(basePackages = "live.lingting",
		excludeFilters = { @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = BizConfiguration.class) })
@Configuration
@Import(SysConfigServiceImpl.class)
@MapperScan(basePackageClasses = SysConfigMapper.class)
public class SampleConfiguration {

	@Bean
	public PaymentConfig paymentConfig(SysConfigService service) {
		return new ApiPaymentConfig(service);
	}

}
