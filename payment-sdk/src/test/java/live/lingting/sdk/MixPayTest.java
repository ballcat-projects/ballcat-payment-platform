package live.lingting.sdk;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import live.lingting.sdk.enums.Chain;
import live.lingting.sdk.enums.SdkContract;
import live.lingting.sdk.response.MixVirtualPayResponse;

/**
 * @author lingting 2021/6/7 23:08
 */
@Slf4j
public class MixPayTest {

	private static MixPay mixPay;

	@BeforeAll
	public static void init() {
		mixPay = new MixPay("http://127.0.0.1:23302", "h8u45pyloe8zmefy", "f528dc13cc87416e9734716221f67244",
				"http://www.baidu.com");
	}

	@SneakyThrows
	@Test
	void virtualPay() {
		final MixVirtualPayResponse response = mixPay.virtualPay("123", SdkContract.USDT, Chain.OMNI);
		log.info(response.toString());
	}

}
