package live.lingting.api.thread;

import cn.hutool.core.thread.ThreadUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;
import live.lingting.api.manager.VirtualManager;
import live.lingting.entity.Pay;
import live.lingting.sdk.enums.Currency;
import live.lingting.sdk.enums.PayStatus;
import live.lingting.service.PayService;
import live.lingting.util.SpringUtils;
import live.lingting.virtual.VirtualHandler;
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
public class VirtualValidThread extends Thread implements InitializingBean {

	/**
	 * 未获取到支付状态超时时间, 单位: 分钟
	 */
	private static final Long EMPTY_TIMEOUT = TimeUnit.HOURS.toMinutes(6);

	/**
	 * 重试时长, 失败允许重试的时长, 单位: 分钟
	 */
	private static final Long RETRY_TIME = TimeUnit.HOURS.toMinutes(2);

	/**
	 * 失败延时, 多少分组内失败的订单不算失败. 单位: 分组
	 */
	private static final Long FAIL_TIME = TimeUnit.MINUTES.toMinutes(10);

	/**
	 * 成功的支付, 如果支付时间比支付信息创建时间早 SUCCESS_TIMEOUT, 表示支付失败
	 */
	private static final Long SUCCESS_TIMEOUT = TimeUnit.HOURS.toMinutes(12);

	private final PayService service;

	private final VirtualManager manager;

	private final VirtualHandler handler;

	@Override
	public void run() {
		final BaseMapper<Pay> mapper = service.getBaseMapper();
		final LambdaQueryWrapper<Pay> wrapper = Wrappers.<Pay>lambdaQuery()
				// 限制 hash 不为空
				.ne(Pay::getThirdPartTradeNo, "")
				// 状态为等待
				.eq(Pay::getStatus, PayStatus.WAIT);
		while (!isInterrupted()) {
			final List<Pay> list = mapper.selectList(wrapper);

			for (Pay pay : list) {
				final Optional<TransactionInfo> optional = handler.getTransaction(pay);
				// 订单已创建时长
				final long minutes = Duration.between(pay.getCreateTime(), LocalDateTime.now()).toMinutes();

				// 没有获取到信息
				if (!optional.isPresent()) {
					// 获取允许的超时时间
					final Long timeout = getEmptyTimeout();
					// 超时
					if (minutes >= timeout) {
						fail(pay, timeout + "分钟内未获取到交易信息!");
					}
					continue;
				}

				final TransactionInfo info = optional.get();
				// 失败, 且创建时长超过 FAIL_TIME
				if (TransactionStatus.FAIL.equals(info.getStatus()) && minutes >= FAIL_TIME) {
					fail(pay, "支付失败!");
				}
				// 成功交易
				else if (TransactionStatus.SUCCESS.equals(info.getStatus())) {
					successHandler(pay, info);
				}
			}

			ThreadUtil.sleep(TimeUnit.MINUTES.toMillis(1));
		}
	}

	/**
	 * 支付成功处理
	 * @author lingting 2021-06-09 15:34
	 */
	private void successHandler(Pay pay, TransactionInfo info) {
		// 合约不匹配
		if (!info.getContract().equals(getContract(pay))) {
			fail(pay, "支付货币不匹配!");
		}
		// 收款账户不匹配
		else if (!info.getTo().equals(pay.getAddress())) {
			fail(pay, "收款账户不匹配!");
		}
		// 支付时间比支付信息创建时间早 SUCCESS_TIMEOUT
		else if (Duration.between(info.getTime(), pay.getCreateTime()).toMillis() >= SUCCESS_TIMEOUT) {
			fail(pay, "支付时间比支付信息创建时间早" + SUCCESS_TIMEOUT + "分钟以上");
		}
		else {
			// 成功
			manager.success(pay);
		}
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

	public static Long getEmptyTimeout() {
		if (!SpringUtils.isProd()) {
			// 测试服 2分钟
			return TimeUnit.MINUTES.toMinutes(1);
		}
		return EMPTY_TIMEOUT;
	}

	public static Long getRetryTime() {
		if (!SpringUtils.isProd()) {
			// 测试服1分钟
			return TimeUnit.MINUTES.toMinutes(1);
		}
		return RETRY_TIME;
	}

	private void fail(Pay pay, String desc) {
		manager.fail(pay, desc, LocalDateTime.now().plusMinutes(getRetryTime()));
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		setName("virtual-valid");
		this.start();
	}

}
