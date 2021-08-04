package live.lingting.payment.api.util;

import java.util.Collection;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import live.lingting.payment.sdk.constant.SdkConstants;

/**
 * @author lingting 2021/4/29 16:16
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UriUtils {

	public static boolean contains(Collection<String> uris, String uri) {
		for (String cUri : uris) {
			// 配置值以 / 结尾, 要求开头匹配, 否则要求全匹配
			boolean exist = cUri.endsWith(SdkConstants.FORWARD_SLASH) ? uri.startsWith(cUri) : uri.equals(cUri);
			if (exist) {
				return true;
			}
		}
		return false;
	}

}
