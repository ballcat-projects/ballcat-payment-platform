package live.lingting.payment.handler.thread;

import com.alipay.api.AlipayApiException;
import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import live.lingting.payment.biz.config.PaymentConfig;
import live.lingting.payment.biz.real.third.AliManager;
import live.lingting.payment.biz.real.third.WxManager;
import live.lingting.payment.biz.service.PayService;
import live.lingting.payment.entity.Pay;
import live.lingting.payment.handler.domain.PaymentQuery;
import live.lingting.payment.sdk.enums.ThirdPart;

/**
 * @author lingting 2021/7/14 15:38
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class RealTransferValidThread extends AbstractThread<Pay> {

	private final PayService service;

	private final AliManager aliManager;

	private final WxManager wxManager;

	private final PaymentConfig config;

	@Override
	public List<Pay> listData() {
		return service.listWaitTransfer();
	}

	@SneakyThrows
	@Override
	public void handler(Pay pay) {
		final boolean isAli = ThirdPart.ALI.equals(pay.getThirdPart());
		PaymentQuery query;

		try {
			if (isAli) {
				query = PaymentQuery.of(aliManager.getAll(pay.getConfigMark()), pay);
			}
			else {
				query = new PaymentQuery().setStatus(PaymentQuery.Status.UNKNOWN);
			}
		}
		catch (AlipayApiException e) {
			log.error("查询交易时发生异常! tradeNo: {}", pay.getTradeNo(), e);
			query = null;
		}

		if (query != null) {
			switch (query.getStatus()) {
			case SUCCESS:
				success(pay, query.getAmount());
				return;
			case CLOSED:
				fail(pay, "交易已关闭");
				return;
			case WAIT:
				// 不打印日志
				break;
			default:
				log.error("交易状态异常! tradeNo:{}, 返回信息: {}", pay.getTradeNo(), query);
			}
		}

		// 没有获取到交易信息, 或者用户未付款
		final Long timeout = config.getEmptyInfoTimeout();

		// 如果支付创建时间超出指定时间范围
		if (Duration.between(pay.getCreateTime(), LocalDateTime.now()).toMinutes() > timeout) {
			fail(pay, "未在" + timeout + "分钟内获取到交易信息!");
		}
	}

	private void success(Pay pay, BigDecimal amount) {
		pay.setAmount(amount);
		service.success(pay);
	}

	private void fail(Pay pay, String msg) {
		service.fail(pay, msg, null);
	}

}
