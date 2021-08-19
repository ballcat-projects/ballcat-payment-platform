package live.lingting.payment.handler.thread;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import live.lingting.payment.ali.AliPay;
import live.lingting.payment.ali.domain.AliPayClose;
import live.lingting.payment.ali.domain.AliPayQuery;
import live.lingting.payment.ali.enums.TradeStatus;
import live.lingting.payment.biz.Redis;
import live.lingting.payment.biz.config.PaymentConfig;
import live.lingting.payment.biz.real.third.AliManager;
import live.lingting.payment.biz.real.third.WxManager;
import live.lingting.payment.biz.service.PayService;
import live.lingting.payment.entity.Pay;
import live.lingting.payment.handler.domain.PaymentQuery;
import live.lingting.payment.sdk.enums.ThirdPart;
import live.lingting.payment.wx.WxPay;
import live.lingting.payment.wx.response.WxPayOrderCloseResponse;
import live.lingting.payment.wx.response.WxPayOrderQueryResponse;

/**
 * @author lingting 2021/7/14 17:11
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class RealExpireThread extends AbstractThread<Pay> {

	private static final PaymentQuery EMPTY_QUERY = new PaymentQuery().setAmount(BigDecimal.ZERO).setSuccess(false);

	private final PayService service;

	private final PaymentConfig config;

	private final WxManager wxManager;

	private final AliManager aliManager;

	private final Redis redis;

	@Override
	public List<Pay> listData() {
		return service.listRealExpire(getMaxTime());
	}

	@Override
	public void handler(Pay pay) {
		PaymentQuery query = valid(pay);
		pay.setThirdPartTradeNo(query.getThirdTradeNo());
		if (query.getSuccess()) {
			// 进入支付成功流程
			service.success(pay);
		}
		else {
			// 发起交易关闭 - 如果关闭失败(已支付, 等待下一次重新处理即可进入正常流程)
			if (fail(pay)) {
				// 成功关闭, 进入支付失败流程
				service.fail(pay, "未在" + config.getRealExpireTimeout() + "分钟内付款!", null);
			}
		}
	}

	/**
	 * 获取过去真实货币支付信息的最大创建时间
	 *
	 * @author lingting 2021-06-09 14:06
	 */
	public LocalDateTime getMaxTime() {
		return LocalDateTime.now().minusMinutes(config.getRealExpireTimeout());
	}

	@SneakyThrows
	public PaymentQuery valid(Pay pay) {
		// 目前全是二维码支付. 金额在下单的时候就保存了. 不需要记录金额
		PaymentQuery query = EMPTY_QUERY;
		if (pay.getThirdPart().equals(ThirdPart.WX)) {
			WxPay wxPay = wxManager.get(pay.getConfigMark());
			WxPayOrderQueryResponse response = wxPay.query(pay.getTradeNo(), "");
			query = new PaymentQuery().setSuccess(response.isSuccess()).setThirdTradeNo(response.getTransactionId());
		}
		else if (pay.getThirdPart().equals(ThirdPart.ALI)) {
			AliPay aliPay = aliManager.get(pay.getConfigMark());
			AliPayQuery aliPayQuery = aliPay.query(pay.getTradeNo());
			query = new PaymentQuery().setSuccess(aliPayQuery.getTradeStatus().equals(TradeStatus.SUCCESS))
					.setThirdTradeNo(aliPayQuery.getTradeNo());
		}

		return query;
	}

	/**
	 * 发起交易失败
	 * @author lingting 2021-08-19 11:35
	 */
	@SneakyThrows
	public boolean fail(Pay pay) {
		if (pay.getThirdPart().equals(ThirdPart.WX)) {
			WxPay wxPay = wxManager.get(pay.getConfigMark());
			WxPayOrderCloseResponse response = wxPay.close(pay.getTradeNo());

			return
			// 关闭成功
			response.isSuccess()
					// 关闭失败, 但是错误信息为-已关闭
					|| WxPayOrderCloseResponse.ErrCode.of(response.getErrCode())
							.equals(WxPayOrderCloseResponse.ErrCode.ORDER_CLOSED);
		}
		else if (pay.getThirdPart().equals(ThirdPart.ALI)) {
			AliPay aliPay = aliManager.get(pay.getConfigMark());
			AliPayClose close = AliPayClose.of(aliPay.close(pay.getTradeNo(), ""));
			// 是否执行成功
			return close.isSuccess();
		}

		// 默认关闭成功
		return true;
	}

}
