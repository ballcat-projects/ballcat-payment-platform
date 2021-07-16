package live.lingting.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import live.lingting.sdk.model.MixRealPayModel;
import live.lingting.sdk.model.MixVirtualPayModel;

/**
 * @author lingting 2021/7/16 10:11
 */
@Getter
@AllArgsConstructor
public enum ProjectScope {

	/**
	 * USDT
	 */
	USDT("USDT"),
	/**
	 * WX
	 */
	WX("微信"),
	/**
	 * ALI
	 */
	ALI("支付宝"),

	;

	private final String desc;

	public static ProjectScope get(MixRealPayModel model) {
		switch (model.getThirdPart()) {
		case WX:
			return WX;
		case ALI:
			return ALI;
		default:
			return null;
		}
	}

	public static ProjectScope get(MixVirtualPayModel model) {
		switch (model.getContract()) {
		case USDT:
			return USDT;
		default:
			return null;
		}
	}

}
