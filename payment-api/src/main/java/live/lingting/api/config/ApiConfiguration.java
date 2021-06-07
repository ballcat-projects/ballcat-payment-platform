package live.lingting.api.config;

import java.lang.reflect.InvocationTargetException;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import live.lingting.api.filter.SignFilter;
import live.lingting.util.SpringUtils;

/**
 * @author lingting 2021/6/7 15:54
 */
@MapperScan(basePackages = "live.lingting.**.mapper")
@ComponentScan(basePackages = "live.lingting")
@Configuration(proxyBeanMethods = false)
public class ApiConfiguration {

	@Bean
	@DependsOn("springUtils")
	public FilterRegistrationBean<SignFilter> signFilterRegistrationBean()
			throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
		final Class<SignFilter> sc = SignFilter.class;
		// 获取构造函数 参数类型
		final Class<?>[] argumentTypes = sc.getConstructors()[0].getParameterTypes();

		// 参数
		Object[] arguments = new Object[argumentTypes.length];

		for (int i = 0; i < argumentTypes.length; i++) {
			arguments[i] = SpringUtils.getBean(argumentTypes[i]);
		}

		FilterRegistrationBean<SignFilter> bean = new FilterRegistrationBean<>();
		bean.setFilter(sc.getConstructor(argumentTypes).newInstance(arguments));
		bean.addUrlPatterns("/*");
		bean.setOrder(Integer.MAX_VALUE);
		return bean;
	}

}
