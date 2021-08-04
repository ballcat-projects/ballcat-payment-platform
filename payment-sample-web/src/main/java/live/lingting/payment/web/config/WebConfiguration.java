package live.lingting.payment.web.config;

import com.hccake.ballcat.log.handler.CustomAccessLogHandler;
import com.hccake.ballcat.log.handler.CustomOperationLogHandler;
import com.hccake.ballcat.log.handler.LoginLogHandler;
import com.hccake.ballcat.log.service.AccessLogService;
import com.hccake.ballcat.log.service.LoginLogService;
import com.hccake.ballcat.log.service.OperationLogService;
import com.hccake.ballcat.log.thread.AccessLogSaveThread;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author lingting 2021/6/4 0:09
 */
@MapperScan(basePackages = "live.lingting.**.mapper")
@ComponentScan(basePackages = "live.lingting")
@Configuration(proxyBeanMethods = false)
public class WebConfiguration {

	@Bean
	public CustomAccessLogHandler accessLogHandler(AccessLogService service) {
		return new CustomAccessLogHandler(new AccessLogSaveThread(service));
	}

	@Bean
	public LoginLogHandler loginLogHandler(LoginLogService service) {
		return new LoginLogHandler(service);
	}

	@Bean
	public CustomOperationLogHandler operationLogHandler(OperationLogService service) {
		return new CustomOperationLogHandler(service);
	}

}
