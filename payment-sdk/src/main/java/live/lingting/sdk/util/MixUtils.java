package live.lingting.sdk.util;

import static live.lingting.sdk.constant.SdkConstants.FIELD_KEY;
import static live.lingting.sdk.constant.SdkConstants.FIELD_NONCE;
import static live.lingting.sdk.constant.SdkConstants.FIELD_SIGN;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import lombok.SneakyThrows;
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

	/**
	 * 签名
	 * @param security security
	 * @param params 参数
	 * @return java.lang.String
	 * @author lingting 2021-04-29 14:24
	 */
	@SneakyThrows
	public static String sign(String security, Map<String, String> params) {
		String[] keyArray = params.keySet().toArray(new String[0]);
		// 参数key排序
		Arrays.sort(keyArray);
		// 构建排序后的用于签名的字符串
		StringBuilder paramsStr = new StringBuilder();
		// 参数值
		Object val;
		for (String k : keyArray) {
			// sign 字段不参与签名
			if (k.equals(FIELD_SIGN)
					// 参数值为空，不参与签名
					|| (val = params.get(k)) == null) {
				continue;
			}

			paramsStr.append(k).append("=").append(val).append("&");
		}
		paramsStr.append("security=").append(security);

		// 构建签名方式
		final Mac mac = Mac.getInstance("HmacSHA256");
		SecretKeySpec sk = new SecretKeySpec(security.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
		mac.init(sk);

		// 签名
		byte[] bytes = mac.doFinal(paramsStr.toString().getBytes(StandardCharsets.UTF_8));

		// 构建返回的签名字符串
		StringBuilder builder = new StringBuilder();

		for (byte b : bytes) {
			builder.append(Integer.toHexString((b & 0xFF) | 0x100), 1, 3);
		}

		return builder.toString().toUpperCase();
	}

	/**
	 * 对参数进行验签
	 * @author lingting 2021-06-07 16:47
	 */
	public static boolean verifySign(String security, Map<String, String> params) {
		// 没有 sign 参数 或 key 参数 或 nonce 参数, 失败
		if (!params.containsKey(FIELD_SIGN) || !params.containsKey(FIELD_KEY) || !params.containsKey(FIELD_NONCE)) {
			return false;
		}

		// 参数中的签名与生成的签名一致, 则成功
		return params.get(FIELD_SIGN).equals(sign(security, params));
	}

}
