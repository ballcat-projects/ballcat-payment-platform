package live.lingting.payment.sdk;

import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.IdUtil;
import java.math.BigDecimal;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import live.lingting.payment.sdk.enums.Chain;
import live.lingting.payment.sdk.enums.Currency;
import live.lingting.payment.sdk.enums.SdkContract;
import live.lingting.payment.sdk.enums.ThirdPart;
import live.lingting.payment.sdk.response.MixQueryResponse;
import live.lingting.payment.sdk.response.MixRateResponse;
import live.lingting.payment.sdk.response.MixRealPayResponse;
import live.lingting.payment.sdk.response.MixVirtualPayResponse;
import live.lingting.payment.sdk.response.MixVirtualRetryResponse;
import live.lingting.payment.sdk.response.MixVirtualSubmitResponse;

/**
 * @author lingting 2021/6/7 23:08
 */
class MixPayTest {

	private static final Snowflake SNOWFLAKE = IdUtil.createSnowflake(1, 1);

	private static MixPay mixPay;

	private MixVirtualPayResponse mixVirtualPayResponse;

	private MixVirtualSubmitResponse mixVirtualSubmitResponse;

	private MixVirtualRetryResponse mixVirtualRetryResponse;

	@BeforeAll
	public static void init() {
		mixPay = new MixPay("http://192.168.1.237:18002", "phwzykzyqijanz2q", "c63ff87a2381449985d7afbc5c84f6fb",
				"http://127.0.0.1:23302/test");
	}

	@Test
	void realQrPay() throws Exception {
		final MixRealPayResponse response = mixPay.realQrPay(SNOWFLAKE.nextIdStr(), new BigDecimal("0.01"),
				ThirdPart.WX, Currency.CNY, "test", "测试-支付平台");
		System.out.println(response.toString());
	}

	@Test
	void realTransferPay() throws Exception {
		final MixRealPayResponse response = mixPay.realTransferPay(SNOWFLAKE.nextIdStr(), "13123123123123",
				ThirdPart.ALI, Currency.CNY, "test", "测试-支付平台");
		System.out.println(response.toString());
	}

	@Test
	void virtualPay() throws Exception {
		mixVirtualPayResponse = mixPay.virtualPay(SNOWFLAKE.nextIdStr(), SdkContract.USDT, Chain.ETH);
		System.out.println(mixVirtualPayResponse.toString());
	}

	@Test
	void virtualSubmit() throws Exception {
		virtualPay();
		String hash = "33c4be98527a8fafba8e1e5ff1c8f32f45e6ddd74e923915da588b4fd707ee04";
		if (mixVirtualPayResponse.isSuccess()) {
			mixVirtualSubmitResponse = mixPay.virtualSubmit(mixVirtualPayResponse.getTradeNo(), "", hash);
			System.out.println(mixVirtualSubmitResponse.toString());
		}
	}

	@Test
	void virtualRetry() throws Exception {
		String tradeNo = "111404767951348961280";
		String hash = "";
		mixVirtualRetryResponse = mixPay.virtualRetry(tradeNo, "", hash);
		System.out.println(mixVirtualRetryResponse.toString());
	}

	@Test
	void query() throws Exception {
		String tradeNo = "111402847045450076160";
		final MixQueryResponse response = mixPay.query(tradeNo, "");
		System.out.println(response.toString());
	}

	@Test
	void rate() throws Exception {
		final MixRateResponse response = mixPay.rate(Currency.USDT);
		System.out.println(response.toString());
	}

}
