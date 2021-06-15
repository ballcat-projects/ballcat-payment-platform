package live.lingting.sdk;

import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.IdUtil;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import live.lingting.sdk.enums.Chain;
import live.lingting.sdk.enums.Currency;
import live.lingting.sdk.enums.SdkContract;
import live.lingting.sdk.response.MixQueryResponse;
import live.lingting.sdk.response.MixRateResponse;
import live.lingting.sdk.response.MixVirtualPayResponse;
import live.lingting.sdk.response.MixVirtualRetryResponse;
import live.lingting.sdk.response.MixVirtualSubmitResponse;

/**
 * @author lingting 2021/6/7 23:08
 */
@Slf4j
class MixPayTest {

	private static final Snowflake SNOWFLAKE = IdUtil.createSnowflake(1, 1);

	private static MixPay mixPay;

	private MixVirtualPayResponse mixVirtualPayResponse;

	private MixVirtualSubmitResponse mixVirtualSubmitResponse;

	private MixVirtualRetryResponse mixVirtualRetryResponse;

	@BeforeAll
	public static void init() {
		mixPay = new MixPay("http://192.168.1.5:23302", "h8u45pyloe8zmefy", "f528dc13cc87416e9734716221f67244",
				"http://192.168.1.5:23302/test");
	}

	@SneakyThrows
	@Test
	void virtualPay() {
		mixVirtualPayResponse = mixPay.virtualPay(SNOWFLAKE.nextIdStr(), SdkContract.USDT, Chain.OMNI);
		log.info(mixVirtualPayResponse.toString());
	}

	@SneakyThrows
	@Test
	void virtualSubmit() {
		virtualPay();
		String hash = "33c4be98527a8fafba8e1e5ff1c8f32f45e6ddd74e923915da588b4fd707ee04";
		if (mixVirtualPayResponse.isSuccess()) {
			mixVirtualSubmitResponse = mixPay.virtualSubmit(mixVirtualPayResponse.getTradeNo(), "", hash);
			log.info(mixVirtualSubmitResponse.toString());
		}
	}

	@SneakyThrows
	@Test
	void virtualRetry() {
		String tradeNo = "111404767951348961280";
		String hash = "";
		mixVirtualRetryResponse = mixPay.virtualRetry(tradeNo, "", hash);
		log.info(mixVirtualRetryResponse.toString());
	}

	@Test
	@SneakyThrows
	void query() {
		String tradeNo = "111402847045450076160";
		final MixQueryResponse response = mixPay.query(tradeNo, "");
		log.info(response.toString());
	}

	@Test
	@SneakyThrows
	void rate() {
		final MixRateResponse response = mixPay.rate(Currency.USDT);
		log.info(response.toString());
	}

}
