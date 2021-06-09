package live.lingting;

import cn.hutool.core.util.StrUtil;
import java.util.concurrent.TimeUnit;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import live.lingting.entity.VirtualAddress;
import live.lingting.sdk.enums.Chain;
import live.lingting.virtual.currency.bitcoin.BitcoinServiceImpl;
import live.lingting.virtual.currency.bitcoin.endpoints.BitcoinEndpoints;
import live.lingting.virtual.currency.bitcoin.properties.BitcoinProperties;
import live.lingting.virtual.currency.core.PlatformService;
import live.lingting.virtual.currency.etherscan.EtherscanServiceImpl;
import live.lingting.virtual.currency.etherscan.endpoints.EtherscanEndpoints;
import live.lingting.virtual.currency.etherscan.properties.EtherscanProperties;
import live.lingting.virtual.currency.tronscan.TronscanServiceImpl;
import live.lingting.virtual.currency.tronscan.endpoints.TronscanEndpoints;
import live.lingting.virtual.currency.tronscan.properties.TronscanProperties;

/**
 * @author lingting 2021/6/8 15:23
 */
@Component
public class VirtualHandler {

	public static final String OMNI_REQUEST_LOCK = "virtual:omni:request:lock";

	public static final String ETHERSCAN_PROJECT_ID = "57479f626d994fc58e94199a07ce37e5";

	public static final String ETHERSCAN_PROJECT_SECURITY = "40d81f800fed4b199e8b2ef4ec644be6";

	public static final String TRONSCAN_API_KEY = "40d81f800fed4b199e8b2ef4ec644be6";

	private final Redis redis;

	private final BitcoinServiceImpl bitcoin;

	private final EtherscanServiceImpl etherscan;

	private final TronscanServiceImpl tronscan;

	public VirtualHandler(Redis redis) {
		this.redis = redis;
		bitcoin = new BitcoinServiceImpl(new BitcoinProperties().setEndpoints(BitcoinEndpoints.MAINNET)
				// 获取锁, 8秒后自动释放
				.setLock(() -> redis.setIfAbsent(OMNI_REQUEST_LOCK, StrUtil.EMPTY, TimeUnit.SECONDS.toSeconds(8)))
				.setUnlock(() -> true));

		etherscan = new EtherscanServiceImpl(
				new EtherscanProperties().setEndpoints(EtherscanEndpoints.MAINNET).setProjectId(ETHERSCAN_PROJECT_ID));

		tronscan = new TronscanServiceImpl(
				new TronscanProperties().setEndpoints(TronscanEndpoints.MAINNET).setApiKey(TRONSCAN_API_KEY));
	}

	public PlatformService<?> getService(Chain chain) {
		switch (chain) {
		case OMNI:
			return bitcoin;
		case ETH:
			return etherscan;
		default:
			return tronscan;
		}
	}

	/**
	 * 校验地址可用性
	 * @author lingting 2021-06-09 09:40
	 */
	public boolean valid(VirtualAddress va) {
		if (va == null || va.getChain() == null || !StringUtils.hasText(va.getAddress())) {
			return false;
		}

		final PlatformService<?> service = getService(va.getChain());
		return service.validate(va.getAddress());
	}

}
