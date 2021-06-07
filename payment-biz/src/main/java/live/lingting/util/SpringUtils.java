package live.lingting.util;

import cn.hutool.core.util.ArrayUtil;
import java.util.Map;
import lombok.Setter;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

/**
 * @author lingting 2021/4/9 14:27
 */
@Component
public class SpringUtils implements ApplicationContextAware {

	@Setter
	private static ApplicationContext context;

	private static Boolean prod;

	public static <T> T getBean(String name) {
		return (T) context.getBean(name);
	}

	public static <T> T getBean(Class<T> clazz) {
		return context.getBean(clazz);
	}

	public static <T> T getBean(String name, Class<T> clazz) {
		return context.getBean(name, clazz);
	}

	public static <T> Map<String, T> getBeansOfType(Class<T> type) {
		return context.getBeansOfType(type);
	}

	public static String[] getBeanNamesForType(Class<?> type) {
		return context.getBeanNamesForType(type);
	}

	public static String getProperty(String key) {
		return context.getEnvironment().getProperty(key);
	}

	public static String[] getActiveProfiles() {
		return context.getEnvironment().getActiveProfiles();
	}

	public static Environment getEnvironment() {
		return context.getEnvironment();
	}

	public static boolean isProd() {
		if (prod == null) {
			prod = ArrayUtil.contains(getActiveProfiles(), "prod");
		}
		return prod;
	}

	public static ApplicationContext getContext() {
		return context;
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		SpringUtils.setContext(applicationContext);
	}

}
