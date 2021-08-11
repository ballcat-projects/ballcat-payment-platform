package live.lingting.payment.pay;

/**
 * @author lingting 2021/8/11 13:56
 */
public interface ThirdPay {

	/**
	 * 设置通知地址
	 * @param url url
	 * @author lingting 2021-08-11 13:57
	 */
	void setNotifyUrl(String url);

	/**
	 * 设置结束跳转地址
	 * @param url url
	 * @author lingting 2021-08-11 13:57
	 */
	void setReturnUrl(String url);

}
