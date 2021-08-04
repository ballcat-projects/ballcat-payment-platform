package live.lingting.payment.biz.virtual;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import live.lingting.payment.biz.Redis;
import live.lingting.payment.entity.Pay;
import live.lingting.payment.entity.VirtualAddress;
import live.lingting.payment.sdk.enums.Chain;
import live.lingting.virtual.currency.bitcoin.BitcoinServiceImpl;
import live.lingting.virtual.currency.bitcoin.contract.OmniContract;
import live.lingting.virtual.currency.bitcoin.endpoints.BitcoinEndpoints;
import live.lingting.virtual.currency.bitcoin.properties.BitcoinProperties;
import live.lingting.virtual.currency.core.Contract;
import live.lingting.virtual.currency.core.PlatformService;
import live.lingting.virtual.currency.core.model.TransactionInfo;
import live.lingting.virtual.currency.etherscan.EtherscanServiceImpl;
import live.lingting.virtual.currency.etherscan.contract.EtherscanContract;
import live.lingting.virtual.currency.etherscan.endpoints.EtherscanEndpoints;
import live.lingting.virtual.currency.etherscan.properties.EtherscanProperties;
import live.lingting.virtual.currency.tronscan.TronscanServiceImpl;
import live.lingting.virtual.currency.tronscan.contract.TronscanContract;
import live.lingting.virtual.currency.tronscan.endpoints.TronscanEndpoints;
import live.lingting.virtual.currency.tronscan.properties.TronscanProperties;

/**
 * @author lingting 2021/6/8 15:23
 */
@Slf4j
@Component
@EnableConfigurationProperties(VirtualProperties.class)
public class VirtualHandler {

	public static final String OMNI_REQUEST_LOCK = "virtual:omni:request:lock";

	private final BitcoinServiceImpl bitcoin;

	private final EtherscanServiceImpl etherscan;

	private final TronscanServiceImpl tronscan;

	public VirtualHandler(Redis redis, VirtualProperties properties) {
		bitcoin = new BitcoinServiceImpl(new BitcoinProperties().setEndpoints(BitcoinEndpoints.MAINNET)
				// 获取锁, 8秒后自动释放
				.setLock(() -> redis.setIfAbsent(OMNI_REQUEST_LOCK, "", TimeUnit.SECONDS.toSeconds(8)))
				.setUnlock(() -> true));

		etherscan = new EtherscanServiceImpl(new EtherscanProperties().setEndpoints(EtherscanEndpoints.MAINNET)
				.setProjectId(properties.getEthKey()));

		tronscan = new TronscanServiceImpl(
				new TronscanProperties().setEndpoints(TronscanEndpoints.MAINNET).setApiKey(properties.getTrcKey()));
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

	public Optional<TransactionInfo> getTransaction(Pay pay) {
		try {
			return getService(pay.getChain()).getTransactionByHash(pay.getThirdPartTradeNo());
		}
		catch (Exception e) {
			log.error("获取交易信息时异常! tradeNo: {}", pay.getTradeNo());
			return Optional.empty();
		}
	}

	/**
	 * 校验地址可用性
	 *
	 * @author lingting 2021-06-09 09:40
	 */
	public boolean valid(VirtualAddress va) {
		if (va == null || va.getChain() == null || !StringUtils.hasText(va.getAddress())) {
			return false;
		}

		final PlatformService<?> service = getService(va.getChain());
		return service.validate(va.getAddress());
	}

	public BigDecimal getBalance(VirtualAddress address) throws Exception {
		final PlatformService<?> service = getService(address.getChain());
		Contract contract;

		switch (address.getChain()) {
		case OMNI:
			contract = OmniContract.USDT;
			break;
		case ETH:
			contract = EtherscanContract.USDT;
			break;
		default:
			contract = TronscanContract.USDT;
			break;
		}

		return service.getNumberByAddressAndContract(address.getAddress(), contract);
	}

}
