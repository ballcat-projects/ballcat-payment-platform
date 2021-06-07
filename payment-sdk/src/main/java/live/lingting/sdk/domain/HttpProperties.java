package live.lingting.sdk.domain;

import lombok.Getter;
import lombok.Setter;

/**
 * http 配置
 *
 * @author lingting 2021/6/7 20:42
 */
@Getter
@Setter
public class HttpProperties {

	/**
	 * 连接超时, 单位: 毫秒
	 */
	private Integer connectTimeout = 10000;

	/**
	 * 读取超时, 单位: 毫秒
	 */
	private Integer readTimeout = 30000;

	/**
	 * 是否校验ssl
	 */
	private Boolean verifierSsl = true;

}
