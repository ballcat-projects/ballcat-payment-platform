package live.lingting.payment.handler.thread;

import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import live.lingting.payment.biz.config.PaymentConfig;
import live.lingting.payment.biz.service.PayService;
import live.lingting.payment.biz.virtual.VirtualHandler;
import live.lingting.payment.biz.virtual.VirtualManager;
import live.lingting.payment.entity.Pay;
import live.lingting.payment.sdk.enums.Currency;
import live.lingting.payment.sdk.enums.PayStatus;
import live.lingting.virtual.currency.bitcoin.contract.OmniContract;
import live.lingting.virtual.currency.core.Contract;
import live.lingting.virtual.currency.core.enums.TransactionStatus;
import live.lingting.virtual.currency.core.model.TransactionInfo;
import live.lingting.virtual.currency.etherscan.contract.EtherscanContract;
import live.lingting.virtual.currency.tronscan.contract.TronscanContract;

/**
 * @author lingting 2021/6/9 14:39
 */
@Component
@RequiredArgsConstructor
public class VirtualValidThread extends AbstractThread<Pay> {

	private final PayService service;

	private final VirtualManager manager;

	private final VirtualHandler handler;

	private final PaymentConfig config;

	private final LambdaQueryWrapper<Pay> wrapper = Wrappers.<Pay>lambdaQuery()
			// 限制 hash 不为空
			.ne(Pay::getThirdPartTradeNo, "")
			// 状态为等待
			.eq(Pay::getStatus, PayStatus.WAIT)
			// 链不为空
			.ne(Pay::getChain, "")
			// 收款地址不为空
			.ne(Pay::getAddress, "")
			// 第三方为空
			.eq(Pay::getThirdPart, "")
			// 支付模式为空
			.eq(Pay::getMode, "");

	@Override
	public List<Pay> listData() {
		return service.getBaseMapper().selectList(wrapper);
	}

	@Override
	public void handler(Pay pay) {
		if (config.isTest()) {
			if (RandomUtil.randomInt(10) % 2 == 0) {
				fail(pay, "测试失败!");
			}
			success(pay, new TransactionInfo().setValue(new BigDecimal(RandomUtil.randomInt(0, 5000))));
			return;
		}

		final Optional<TransactionInfo> optional = handler.getTransaction(pay);
		// 订单已创建时长
		final long minutes = Duration.between(pay.getCreateTime(), LocalDateTime.now()).toMinutes();

		// 没有获取到信息
		if (!optional.isPresent()) {
			// 获取允许的超时时间
			final Long timeout = config.getEmptyInfoTimeout();
			// 超时
			if (minutes >= timeout) {
				fail(pay, timeout + "分钟内未获取到交易信息!");
			}
			return;
		}

		final TransactionInfo info = optional.get();
		// 失败, 且创建时长超过 FAIL_TIME
		if (TransactionStatus.FAIL.equals(info.getStatus()) && minutes >= config.getFailTimeout()) {
			fail(pay, "支付失败!");
		}
		// 成功交易
		else if (TransactionStatus.SUCCESS.equals(info.getStatus())) {
			successHandler(pay, info);
		}
	}

	/**
	 * 支付成功处理
	 *
	 * @author lingting 2021-06-09 15:34
	 */
	private void successHandler(Pay pay, TransactionInfo info) {
		Long timeout;
		// 合约不匹配
		if (!info.getContract().equals(getContract(pay))) {
			fail(pay, "支付货币不匹配!");
		}
		// 收款账户不匹配
		else if (!info.getTo().equalsIgnoreCase(pay.getAddress())) {
			fail(pay, "收款账户不匹配!");
		}
		// 支付时间比支付信息创建时间早 SUCCESS_TIMEOUT
		else if (Duration.between(info.getTime(), pay.getCreateTime())
				.toMinutes() >= (timeout = config.getInfoCreateTimeout())) {
			fail(pay, "支付时间比支付信息创建时间早" + timeout + "分钟以上");
		}
		else {
			success(pay, info);
		}
	}

	private void success(Pay pay, TransactionInfo info) {
		pay.setAmount(info.getValue());
		// 成功
		manager.success(pay);
	}

	private Contract getContract(Pay pay) {
		if (Currency.USDT.equals(pay.getCurrency())) {
			switch (pay.getChain()) {
			case OMNI:
				return OmniContract.USDT;
			case ETH:
				return EtherscanContract.USDT;
			default:
				return TronscanContract.USDT;
			}
		}

		return null;
	}

	private void fail(Pay pay, String desc) {
		manager.fail(pay, desc, config.getRetryTimeout());
	}

}
