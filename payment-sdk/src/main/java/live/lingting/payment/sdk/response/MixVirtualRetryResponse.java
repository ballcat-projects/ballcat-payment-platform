package live.lingting.payment.sdk.response;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

/**
 * @author lingting 2021/6/7 17:25
 */
public class MixVirtualRetryResponse extends MixResponse<MixVirtualRetryResponse.Data> {

	public String getTradeNo() {
		if (getData() == null) {
			return null;
		}
		return getData().getTradeNo();
	}

	public LocalDateTime getRetryEndTime() {
		if (getData() == null) {
			return null;
		}
		return getData().getRetryEndTime();
	}

	@Getter
	@Setter
	public static class Data {

		private String tradeNo;

		private LocalDateTime retryEndTime;

	}

}
