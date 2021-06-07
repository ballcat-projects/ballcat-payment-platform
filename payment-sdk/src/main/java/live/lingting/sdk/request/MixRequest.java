package live.lingting.sdk.request;

import java.util.Map;
import live.lingting.sdk.domain.HttpProperties;
import live.lingting.sdk.exception.MixException;
import live.lingting.sdk.model.MixModel;
import live.lingting.sdk.response.MixResponse;

/**
 * @author lingting 2021/6/7 17:22
 */
public interface MixRequest<M extends MixModel, R extends MixResponse<?>> {

	/**
	 * 获取http请求 path
	 * @return java.lang.String
	 * @author lingting 2021-06-07 20:48
	 */
	String getPath();

	/**
	 * 获取参数基础数据
	 * @return M
	 * @author lingting 2021-06-07 19:34
	 */
	M getModel();

	/**
	 * 请求参数
	 * @return java.util.Map<java.lang.String,java.lang.String>
	 * @author lingting 2021-06-07 19:51
	 * @exception MixException 参数有效性异常
	 */
	Map<String, String> getParams() throws MixException;

	/**
	 * 获取http请求配置
	 * @return live.lingting.sdk.domain.HttpProperties
	 * @author lingting 2021-06-07 20:43
	 */
	HttpProperties getProperties();

	/**
	 * 返回值字符串转返回值类
	 * @param resStr 返回值字符串
	 * @return R
	 * @author lingting 2021-06-07 21:38
	 */
	R convert(String resStr);

}