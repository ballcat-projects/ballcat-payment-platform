package live.lingting.payment.rate;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;
import java.math.RoundingMode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import live.lingting.payment.http.HttpGet;
import live.lingting.payment.http.HttpResponse;
import live.lingting.payment.sdk.enums.Currency;
import live.lingting.payment.util.JacksonUtils;

/**
 * 阿里云购买的 易源数据 汇率查询api
 *
 * @author lingting 2021/5/25 10:24
 */
@Slf4j
public class YyRate implements BaseRate {

	private static final String URL = "https://ali-waihui.showapi.com/waihui-transform";

	private final String appCode;

	private final String appKey;

	private final String appSecret;

	public YyRate(RateProperties.Yy yy) {
		this.appCode = yy.getCode();
		this.appKey = yy.getKey();
		this.appSecret = yy.getSecurity();
	}

	@Override
	public BigDecimal get(String code) {
		if (Rate.CNY_CODE.equals(code)) {
			return Rate.CNY_RATE;
		}

		HttpGet http = HttpGet.of(URL + Req.of(code, Currency.CNY.name(), "1"));
		http.setConnectTimeout(CONNECT_TIMEOUT);
		http.setReadTimeout(READ_TIMEOUT);

		http.auth("APPCODE " + appCode);
		final HttpResponse response = http.exec();
		final BigDecimal rate = new BigDecimal(Res.of(response.getBody()).getBody().getMoney());
		// 保留4位小数
		return rate.setScale(4, RoundingMode.DOWN);
	}

	@Getter
	@Setter
	@AllArgsConstructor
	public static class Req {

		private String from;

		private String to;

		private String money;

		public static Req of(String from, String to, String money) {
			return new Req(from, to, money);
		}

		@Override
		public String toString() {
			return String.format("?fromCode=%s&toCode=%s&money=%s", from, to, money);
		}

	}

	@NoArgsConstructor
	@Data
	public static class Res {

		@JsonProperty("showapi_res_code")
		private Integer code;

		@JsonProperty("showapi_res_error")
		private String error;

		@JsonProperty("showapi_res_body")
		private Body body;

		public static Res of(String res) {
			return JacksonUtils.toObj(res, Res.class);
		}

		@NoArgsConstructor
		@Data
		public static class Body {

			@JsonProperty("ret_code")
			private Integer code;

			@JsonProperty("money")
			private String money;

		}

	}

}
