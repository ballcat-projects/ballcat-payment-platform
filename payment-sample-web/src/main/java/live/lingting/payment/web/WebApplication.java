package live.lingting.payment.web;

import com.hccake.ballcat.common.log.access.annotation.EnableAccessLog;
import com.hccake.ballcat.common.log.operation.annotation.EnableOperationLog;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.socket.config.annotation.EnableWebSocket;

/**
 * @author lingting 2021/6/4 0:08
 */
@EnableWebSocket
@EnableAccessLog
@EnableOperationLog
@SpringBootApplication
public class WebApplication {

	public static void main(String[] args) {
		SpringApplication.run(WebApplication.class);
	}

}
