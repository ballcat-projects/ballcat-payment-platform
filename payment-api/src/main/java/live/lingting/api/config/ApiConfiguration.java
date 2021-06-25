package live.lingting.api.config;

import static live.lingting.constant.TableConstants.FIELD_PROJECT_ID;

import com.hccake.ballcat.common.datascope.DataScope;
import com.hccake.ballcat.common.datascope.handler.AbstractDataPermissionHandler;
import com.hccake.ballcat.common.datascope.handler.DataPermissionHandler;
import com.hccake.ballcat.common.datascope.interceptor.DataPermissionInterceptor;
import com.hccake.ballcat.common.datascope.processor.DataScopeSqlProcessor;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import net.sf.jsqlparser.expression.Alias;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.LongValue;
import net.sf.jsqlparser.expression.operators.relational.ExpressionList;
import net.sf.jsqlparser.expression.operators.relational.InExpression;
import net.sf.jsqlparser.schema.Column;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import live.lingting.api.filter.SignFilter;
import live.lingting.api.log.LogFilter;
import live.lingting.api.properties.ApiProperties;
import live.lingting.api.util.SecurityUtils;
import live.lingting.constant.TableConstants;
import live.lingting.util.SpringUtils;

/**
 * @author lingting 2021/6/7 15:54
 */
@MapperScan(basePackages = "live.lingting.**.mapper")
@ComponentScan(basePackages = "live.lingting")
@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties(ApiProperties.class)
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

	@Bean
	@DependsOn("springUtils")
	public FilterRegistrationBean<LogFilter> logFilterFilterRegistrationBean()
			throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
		final Class<LogFilter> sc = LogFilter.class;
		// 获取构造函数 参数类型
		final Class<?>[] argumentTypes = sc.getConstructors()[0].getParameterTypes();

		// 参数
		Object[] arguments = new Object[argumentTypes.length];

		for (int i = 0; i < argumentTypes.length; i++) {
			arguments[i] = SpringUtils.getBean(argumentTypes[i]);
		}

		FilterRegistrationBean<LogFilter> bean = new FilterRegistrationBean<>();
		bean.setFilter(sc.getConstructor(argumentTypes).newInstance(arguments));
		bean.addUrlPatterns("/*");
		// 比 traceId 后执行
		bean.setOrder(Integer.MAX_VALUE - 1);
		return bean;
	}

	/**
	 * ------------------- data scope -------------------
	 */
	@Bean
	public DataPermissionHandler dataPermissionHandler(List<DataScope> scopes) {
		return new AbstractDataPermissionHandler(scopes) {
			@Override
			public boolean ignorePermissionControl(String s) {
				// 有登录角色, 鉴权
				return SecurityUtils.getProject() == null;
			}
		};
	}

	@Bean
	public DataPermissionInterceptor dataPermissionInterceptor(DataPermissionHandler handler) {
		return new DataPermissionInterceptor(new DataScopeSqlProcessor(), handler);
	}

	@Bean
	public DataScope projectScope() {
		return new DataScope() {
			@Override
			public String getResource() {
				return "project";
			}

			@Override
			public Collection<String> getTableNames() {
				return Collections.singleton(TableConstants.TN_PAY);
			}

			@Override
			public Expression getExpression(String s, Alias alias) {
				Column column = new Column(alias == null ? FIELD_PROJECT_ID : alias.getName() + "." + FIELD_PROJECT_ID);
				ExpressionList expressionList = new ExpressionList();
				final List<Expression> list = Collections
						.singletonList(new LongValue(SecurityUtils.getProject().getId()));
				expressionList.setExpressions(list);
				return new InExpression(column, expressionList);
			}
		};
	}

}
