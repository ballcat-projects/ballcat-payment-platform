package live.lingting.sdk;

import static live.lingting.sdk.constant.SdkConstants.FIELD_KEY;
import static live.lingting.sdk.constant.SdkConstants.FIELD_NONCE;
import static live.lingting.sdk.constant.SdkConstants.FIELD_SIGN;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Map;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import lombok.Getter;
import lombok.SneakyThrows;
import live.lingting.sdk.client.DefaultMixClient;
import live.lingting.sdk.client.MixClient;
import live.lingting.sdk.enums.Chain;
import live.lingting.sdk.enums.SdkContract;
import live.lingting.sdk.exception.MixException;
import live.lingting.sdk.model.MixVirtualPayModel;
import live.lingting.sdk.request.MixVirtualPayRequest;
import live.lingting.sdk.response.MixVirtualPayResponse;

/**
 * 混合支付
 *
 * @author lingting 2021/6/4 13:34
 */
@Getter
public class MixPay {

	private final String serverUrl;

	private final String apiKey;

	private final String apiSecurity;

	private final String notifyUrl;

	private final MixClient client;

	public MixPay(String serverUrl, String apiKey, String apiSecurity, String notifyUrl) {
		this(serverUrl, apiKey, apiSecurity, notifyUrl, new DefaultMixClient(serverUrl, apiKey, apiSecurity));
	}

	public MixPay(String serverUrl, String apiKey, String apiSecurity, String notifyUrl, MixClient client) {
		this.serverUrl = serverUrl;
		this.apiKey = apiKey;
		this.apiSecurity = apiSecurity;
		this.notifyUrl = notifyUrl;
		this.client = client;
	}

	public MixVirtualPayResponse virtualPay(String projectTradeNo, SdkContract contract, Chain chain)
			throws MixException {
		MixVirtualPayModel model = new MixVirtualPayModel();
		model.setChain(chain);
		model.setContract(contract);
		model.setProjectTradeNo(projectTradeNo);
		model.setNotifyUrl(notifyUrl);
		return virtualPay(model);
	}

	public MixVirtualPayResponse virtualPay(MixVirtualPayModel model) throws MixException {
		MixVirtualPayRequest request = new MixVirtualPayRequest();
		request.setModel(model);
		return client.execute(request);
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
