package live.lingting.payment.sample.exception;

import com.hccake.ballcat.common.conf.exception.GlobalExceptionHandlerResolver;
import com.hccake.ballcat.common.core.exception.handler.GlobalExceptionHandler;
import com.hccake.ballcat.common.model.result.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import live.lingting.payment.exception.PaymentException;

/**
 * @author lingting 2021/8/5 上午10:33
 */
@Slf4j
@Component
@RestControllerAdvice
public class PaymentExceptionHandlerResolver extends GlobalExceptionHandlerResolver {

	private final GlobalExceptionHandler globalExceptionHandler;

	public PaymentExceptionHandlerResolver(GlobalExceptionHandler globalExceptionHandler) {
		super(globalExceptionHandler);
		this.globalExceptionHandler = globalExceptionHandler;
	}

	@ExceptionHandler(PaymentException.class)
	@ResponseStatus(HttpStatus.OK)
	public R<String> handlePaymentException(PaymentException e) {
		log.error("支付业务异常信息 ex={}", e.getMessage());
		globalExceptionHandler.handle(e);
		return R.failed(e.getCode(), e.getMessage());
	}

}
