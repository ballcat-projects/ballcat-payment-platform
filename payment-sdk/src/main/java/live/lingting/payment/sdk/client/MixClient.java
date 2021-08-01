package live.lingting.payment.sdk.client;

import live.lingting.payment.sdk.exception.MixException;
import live.lingting.payment.sdk.model.MixModel;
import live.lingting.payment.sdk.request.MixRequest;
import live.lingting.payment.sdk.response.MixResponse;

/**
 * 请求处理客户端
 *
 * @author lingting 2021/6/7 20:00
 */
public interface MixClient {

	/**
	 * 根据 request 内容执行请求
	 * @param request request内容
	 * @return R
	 * @throws MixException 请求异常
	 * @author lingting 2021-06-07 20:40
	 */
	<M extends MixModel, R extends MixResponse<?>> R execute(MixRequest<M, R> request) throws MixException;

}
