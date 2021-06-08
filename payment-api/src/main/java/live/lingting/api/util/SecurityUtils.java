package live.lingting.api.util;

import lombok.experimental.UtilityClass;
import org.springframework.security.core.Authentication;
import live.lingting.entity.Project;

/**
 * @author lingting 2021/6/7 22:32
 */
@UtilityClass
public class SecurityUtils {

	public static Project getProject() {
		final Authentication authentication = com.hccake.ballcat.oauth.util.SecurityUtils.getAuthentication();
		if (authentication == null) {
			return null;
		}

		final Object principal = authentication.getPrincipal();
		if (principal instanceof Project) {
			return (Project) principal;
		}
		return null;
	}

}
