package live.lingting.payment.sdk.response;

import lombok.Getter;
import lombok.Setter;

/**
 * @author lingting 2021/6/10 9:46
 */
public class MixRealPayResponse extends MixResponse<MixRealPayResponse.Data> {

	public String getQr() {
		if (getData() == null) {
			return null;
		}
		return getData().getQr();
	}

	public String getTradeNo() {
		if (getData() == null) {
			return null;
		}
		return getData().getTradeNo();
	}

	@Getter
	@Setter
	public static class Data {

		private String qr;

		private String tradeNo;

	}

}
