package live.lingting.sdk.client;

import live.lingting.sdk.exception.MixException;
import live.lingting.sdk.model.MixModel;
import live.lingting.sdk.request.MixRequest;
import live.lingting.sdk.response.MixResponse;

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
	 * @author lingting 2021-06-07 20:40
	 * @throws MixException 请求异常
	 */
	<M extends MixModel, R extends MixResponse<?>> R execute(MixRequest<M, R> request) throws MixException;

}
