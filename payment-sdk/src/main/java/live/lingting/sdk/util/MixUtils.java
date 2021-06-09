package live.lingting.sdk.util;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.experimental.UtilityClass;
import org.springframework.util.StringUtils;
import live.lingting.sdk.enums.Chain;

/**
 * @author lingting 2021/6/9 17:06
 */
@UtilityClass
public class MixUtils {

	private static final Pattern VALID_HASH_PATTER = Pattern.compile("^[a-zA-Z0-9]{64,}$");

	private static final Integer OMNI_HASH_LEN = 64;

	private static final Integer ETH_HASH_LEN = 66;

	private static final Integer TRC_HASH_LEN = 64;

	private static final String ETH_HASH_PREFIX = "0x";

	private static final List<Integer> HASH_LEN;

	static {
		HASH_LEN = new ArrayList<>(2);
		HASH_LEN.add(64);
		HASH_LEN.add(66);
	}

	/**
	 * 清理hash
	 * @author lingting 2021-06-09 17:14
	 */
	public static String clearHash(String hash) {
		if (StringUtils.hasText(hash)) {
			// 移除不可见字符
			return hash.replaceAll("\\s", "");
		}
		return "";
	}

	/**
	 * 校验hash有效性
	 * @author lingting 2021-06-09 17:14
	 */
	public static boolean validHash(String hash) {
		if (!HASH_LEN.contains(hash.length())) {
			return false;
		}

		final Matcher matcher = VALID_HASH_PATTER.matcher(hash);
		return matcher.find();
	}

	/**
	 * 针对链对hash进行校验
	 * @author lingting 2021-06-09 17:25
	 */
	public static boolean validHash(Chain chain, String hash) {
		if (!validHash(hash)) {
			return false;
		}
		switch (chain) {
		case OMNI:
			return hash.length() == OMNI_HASH_LEN;
		case ETH:
			return hash.startsWith(ETH_HASH_PREFIX) && hash.length() == ETH_HASH_LEN;
		default:
			return hash.length() == TRC_HASH_LEN;
		}
	}

}
