package live.lingting.payment.sdk.response;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

/**
 * @author lingting 2021/6/7 17:25
 */
public class MixVirtualPayResponse extends MixResponse<MixVirtualPayResponse.Data> {

	public String getAddress() {
		if (getData() == null) {
			return null;
		}
		return getData().getAddress();
	}

	public String getTradeNo() {
		if (getData() == null) {
			return null;
		}
		return getData().getTradeNo();
	}

	public LocalDateTime getExpireTime() {
		if (getData() == null) {
			return null;
		}
		return getData().getExpireTime();
	}

	@Getter
	@Setter
	public static class Data {

		private String address;

		private String tradeNo;

		private LocalDateTime expireTime;

	}

}
