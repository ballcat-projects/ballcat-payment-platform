package live.lingting.payment.api.config;

import cn.hutool.core.util.ArrayUtil;
import com.hccake.ballcat.common.model.result.R;
import com.hccake.ballcat.common.model.result.SystemResultCode;
import com.hccake.ballcat.oauth.properties.SecurityProperties;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.web.AuthenticationEntryPoint;
import live.lingting.payment.http.utils.JacksonUtils;

/**
 * @author Hccake
 * @version 1.0
 * @date 2019/9/27 17:28 资源服务器配置
 */
@Configuration(proxyBeanMethods = false)
@EnableResourceServer
@EnableGlobalMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor
@EnableConfigurationProperties(SecurityProperties.class)
public class CustomResourceServerConfigurer extends ResourceServerConfigurerAdapter {

	private final SecurityProperties securityProperties;

	/**
	 * Add resource-server specific properties (like a resource id). The defaults should
	 * work for many applications, but you might want to change at least the resource id.
	 * @param resources configurer for the resource server
	 * @throws Exception if there is a problem
	 */
	@Override
	public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
		// 配置自定义异常处理
		resources.authenticationEntryPoint(new AuthenticationEntryPoint() {
			@Override
			public void commence(HttpServletRequest request, HttpServletResponse response,
					AuthenticationException authException) throws IOException, ServletException {
				response.setHeader("Content-Type", MediaType.APPLICATION_JSON.toString());
				response.setStatus(HttpStatus.UNAUTHORIZED.value());
				R<Object> r = R.failed(SystemResultCode.UNAUTHORIZED, authException.getMessage());
				response.getWriter().write(JacksonUtils.toJson(r));
			}
		});
	}

	/**
	 * 通过重载，配置如何通过拦截器保护请求
	 * @param httpSecurity HttpSecurity
	 * @throws Exception 异常信息
	 */
	@Override
	public void configure(HttpSecurity httpSecurity) throws Exception {
		// @formatter:off
		httpSecurity
				// 拦截 url 配置
				.authorizeRequests()
				.antMatchers(ArrayUtil.toArray(securityProperties.getIgnoreUrls(), String.class))
				.permitAll()
				.anyRequest().authenticated()

				// 使用token鉴权时 关闭 session 缓存
				.and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)

				// 关闭 csrf 跨站攻击防护
				.and().csrf().disable();
		// @formatter:on

		// 允许嵌入iframe
		if (!securityProperties.isIframeDeny()) {
			httpSecurity.headers().frameOptions().disable();
		}
	}

}
