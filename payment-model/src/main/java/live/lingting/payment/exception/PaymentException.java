package live.lingting.payment.exception;

import lombok.Getter;
import live.lingting.payment.enums.ResponseCode;

/**
 * @author lingting 2021-08-03 15:31
 */
@Getter
public class PaymentException extends Exception {

	private Integer code;

	public PaymentException(int code, String message) {
		super(message);
		this.code = code;
	}

	public PaymentException(ResponseCode code) {
		super(code.getMessage());
		this.code = code.getCode();
	}

	public PaymentException(ResponseCode code, String message) {
		super(message);
		this.code = code.getCode();
	}

}
