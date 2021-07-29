package live.lingting.payment.sdk.model;

import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;
import org.springframework.util.StringUtils;
import live.lingting.payment.sdk.constant.SdkConstants;
import live.lingting.payment.sdk.exception.MixException;
import live.lingting.payment.sdk.exception.MixRequestParamsValidException;

/**
 * 请求参数基础数据
 *
 * @author lingting 2021/6/7 19:24
 */
@Getter
@Setter
public abstract class MixModel implements Serializable {

	private static final long serialVersionUID = 1L;

	private String tradeNo;

	private String projectTradeNo;

	private String notifyUrl;

	/**
	 * <p>
	 * 校验请求参数是否有效
	 * </p>
	 * <p>
	 * 仅在 getModel() 返回值不为 null 时执行
	 * </p>
	 * @author lingting 2021-06-07 19:27
	 * @exception MixException 校验失败抛出,参数有效性异常
	 */
	public abstract void valid() throws MixException;

	/**
	 * 在需要校验的时候调用!
	 * @author lingting 2021-06-07 19:45
	 */
	public void validNotifyUrl() throws MixException {
		if (!StringUtils.hasText(getNotifyUrl())) {
			throw new MixRequestParamsValidException("回调通知地址不能为空!");
		}

		if (!getNotifyUrl().startsWith(SdkConstants.NOTIFY_URL_PREFIX)) {
			throw new MixRequestParamsValidException("回调通知地址不是正确的http请求地址!");
		}
	}

	/**
	 * 交易 项目交易号和 交易号
	 * @author lingting 2021-06-09 17:05
	 */
	public void validNo() throws MixException {
		if (!StringUtils.hasText(getProjectTradeNo()) && !StringUtils.hasText(getTradeNo())) {
			throw new MixRequestParamsValidException("项目交易号和交易号不能同时为空");
		}
	}

}
