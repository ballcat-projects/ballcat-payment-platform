package live.lingting.payment.sdk.response;

import lombok.Getter;
import lombok.Setter;

/**
 * @author lingting 2021/9/1 14:16
 */
public class MixBankResponse extends MixResponse<MixBankResponse.Data> {

	public String getCard() {
		if (getData() == null) {
			return null;
		}
		return getData().getCard();
	}

	@Getter
	@Setter
	public static class Data {

		/**
		 * 卡号
		 */
		private String card;

	}

}
