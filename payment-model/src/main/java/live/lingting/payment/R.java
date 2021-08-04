package live.lingting.payment;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import live.lingting.payment.enums.ResponseCode;

/**
 * 响应信息主体
 *
 * @param <T>
 * @author Hccake
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class R<T> {

	private static final long serialVersionUID = 1L;

	private int code;

	private String message;

	private T data;

	public static <T> R<T> ok() {
		return ok(null);
	}

	public static <T> R<T> ok(T data) {
		return new R<T>().setCode(ResponseCode.SUCCESS.getCode()).setData(data)
				.setMessage(ResponseCode.SUCCESS.getMessage());
	}

	public static <T> R<T> ok(T data, String message) {
		return new R<T>().setCode(ResponseCode.SUCCESS.getCode()).setData(data).setMessage(message);
	}

	public static <T> R<T> failed(int code, String message) {
		return new R<T>().setCode(code).setMessage(message);
	}

	public static <T> R<T> failed(ResponseCode failMsg) {
		return new R<T>().setCode(failMsg.getCode()).setMessage(failMsg.getMessage());
	}

	public static <T> R<T> failed(ResponseCode failMsg, String message) {
		return new R<T>().setCode(failMsg.getCode()).setMessage(message);
	}

}
