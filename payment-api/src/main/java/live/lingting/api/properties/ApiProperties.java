package live.lingting.api.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author lingting 2021/6/17 17:32
 */
@Getter
@Setter
@ConfigurationProperties("mix.api")
public class ApiProperties {

	/**
	 * <p>
	 * 是否作为测试用模块启动.
	 * </p>
	 *
	 * <p>
	 * 作为测试用模块启动不会对请求进行验签.
	 * </p>
	 * <p>
	 * 作为测试用模块启动所有支付信息不会去实际验证是否成功, 每笔支付有50%的几率成功或失败!
	 * </p>
	 */
	private boolean test = false;

}
