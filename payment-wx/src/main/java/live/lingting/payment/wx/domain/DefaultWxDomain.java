package live.lingting.payment.wx.domain;

import java.util.Map;
import lombok.SneakyThrows;
import live.lingting.payment.http.HttpPost;
import live.lingting.payment.wx.enums.RequestSuffix;
import live.lingting.payment.wx.utils.WxPayUtil;

/**
 * 微信域名管理
 *
 * @author lingting 2021/1/26 16:05
 */
public class DefaultWxDomain implements WxDomain {

	private static final String FLAG = "/";

	/**
	 * 是否使用沙箱
	 */
	private final boolean sandbox;

	private DefaultWxDomain(boolean sandbox) {
		this.sandbox = sandbox;
	}

	public static DefaultWxDomain of(boolean sandbox) {
		return new DefaultWxDomain(sandbox);
	}

	@Override
	@SneakyThrows
	public String sendRequest(Map<String, String> params, RequestSuffix rs) {
		// 获取请求地址
		String url = getUrl(rs.getSuffix());
		HttpPost post = HttpPost.of(url);
		post.header("Content-Type", "text/xml");
		post.setBody(WxPayUtil.mapToXml(params));
		return post.exec().getBody();
	}

	/**
	 * 根据微信的建议, 这里后续需要加上主备切换的功能
	 * @return java.lang.String
	 * @author lingting 2021-01-29 17:50
	 */
	public String getDomain() {
		return MAIN1;
	}

	public String getUrl(String suffix) {
		if (suffix.startsWith(FLAG)) {
			suffix = suffix.substring(1);
		}

		if (sandbox) {
			return getDomain() + "sandboxnew/pay/" + suffix;
		}
		return getDomain() + "pay/" + suffix;
	}

}
