package live.lingting.payment.sdk.exception;

/**
 * @author lingting 2021/6/7 20:10
 */
public class MixException extends Exception {

	public MixException() {
	}

	public MixException(String message) {
		super(message);
	}

	public MixException(String message, Throwable cause) {
		super(message, cause);
	}

	public MixException(Throwable cause) {
		super(cause);
	}

	public MixException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
