package live.lingting.payment.sdk.response;

import lombok.Getter;
import lombok.Setter;
import live.lingting.payment.sdk.constant.SdkConstants;
import live.lingting.payment.sdk.util.JacksonUtils;

/**
 * @author lingting 2021/6/7 17:25
 */
@Getter
@Setter
public abstract class MixResponse<D> {

	private Integer code;

	private String message;

	private D data;

	/**
	 * 请求是否成功
	 * @author lingting 2021-06-07 20:37
	 */
	public boolean isSuccess() {
		return code != null && code.equals(SdkConstants.SUCCESS_CODE);
	}

	@Override
	public String toString() {
		return JacksonUtils.toJson(this);
	}

}
