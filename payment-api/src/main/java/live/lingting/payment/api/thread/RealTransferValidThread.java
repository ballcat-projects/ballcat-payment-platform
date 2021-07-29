package live.lingting.payment.api.thread;

import com.alipay.api.AlipayApiException;
import com.hccake.extend.pay.ali.AliPay;
import com.hccake.extend.pay.ali.domain.AliPayQuery;
import com.hccake.extend.pay.ali.enums.TradeStatus;
import com.hccake.extend.pay.wx.WxPay;
import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import live.lingting.payment.biz.config.PayConfig;
import live.lingting.payment.entity.Pay;
import live.lingting.payment.sdk.enums.ThirdPart;
import live.lingting.payment.biz.service.PayService;

/**
 * @author lingting 2021/7/14 15:38
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class RealTransferValidThread extends AbstractThread<Pay> {

	private final PayService service;

	private final AliPay aliPay;

	private final WxPay wxPay;

	private final PayConfig config;

	@Override
	public List<Pay> listData() {
		return service.listWaitTransfer();
	}

	@Override
	public void handler(Pay pay) {
		final boolean isAli = ThirdPart.ALI.equals(pay.getThirdPart());
		if (isAli) {
			AliPayQuery query;
			try {
				query = aliPay.query(null, pay.getThirdPartTradeNo());
			}
			catch (AlipayApiException e) {
				log.error("查询交易时发生异常! tradeNo: {}", pay.getTradeNo(), e);
				query = null;
			}

			if (query != null && !query.getTradeStatus().equals(TradeStatus.WAIT)) {
				if (query.getTradeStatus().equals(TradeStatus.SUCCESS)
						|| query.getTradeStatus().equals(TradeStatus.FINISHED)) {
					success(pay, query.getAmount());
					return;
				}
				else if (query.getTradeStatus().equals(TradeStatus.CLOSED)) {
					fail(pay, "交易已关闭");
					return;
				}
				else {
					log.error("交易查询返回异常! tradeNo:{}, 返回信息: {}", pay.getTradeNo(), query);
				}
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
