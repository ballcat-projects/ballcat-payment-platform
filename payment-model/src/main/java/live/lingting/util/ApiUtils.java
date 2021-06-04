package live.lingting.util;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.RandomUtil;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import live.lingting.entity.Project;

/**
 * @author lingting 2021/6/4 17:12
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ApiUtils {

	/**
	 * 生成api Key
	 * @author lingting 2021-04-29 13:56
	 */
	public static String generateKey() {
		return RandomUtil.randomString(16);
	}

	/**
	 * 生成api security
	 * @author lingting 2021-04-29 13:56
	 */
	public static String generateSecurity() {
		return IdUtil.fastSimpleUUID();
	}

	public static Project fillApi(Project project) {
		project.setApiKey(generateKey());
		project.setApiSecurity(generateSecurity());
		return project;
	}

}
