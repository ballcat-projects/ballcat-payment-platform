package live.lingting.payment.api.controller;

import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import live.lingting.payment.ali.AliPay;
import live.lingting.payment.ali.domain.AliPayCallback;
import live.lingting.payment.ali.enums.TradeStatus;
import live.lingting.payment.api.log.LogFilter;
import live.lingting.payment.biz.service.PayService;
import live.lingting.payment.entity.Pay;
import live.lingting.payment.util.JacksonUtils;
import live.lingting.payment.sdk.enums.PayStatus;
import live.lingting.payment.wx.WxPay;
import live.lingting.payment.wx.constants.WxPayConstant;
import live.lingting.payment.wx.enums.ResponseCode;
import live.lingting.payment.wx.response.WxPayCallback;
import live.lingting.payment.wx.utils.WxPayUtil;

/**
 * @author lingting 2021/7/14 19:01
 */
@Slf4j
@RestController
@RequestMapping("callback")
@RequiredArgsConstructor
public class CallbackController {

	private final PayService service;

	private final AliPay aliPay;

	private final WxPay wxPay;

	@PostMapping(LogFilter.ALI)
	public String ali(@RequestParam Map<String, String> callbackMap) {
		AliPayCallback callback;
		final String json = JacksonUtils.toJson(callbackMap);
		try {
			callback = AliPayCallback.of(callbackMap);
		}
		catch (Exception e) {
			log.error("支付宝回调解析异常, 回调内容: {}", json, e);
			return "resolve error";
		}
		log.info("支付宝回调: {}", json);

		if (callback.checkSign(aliPay)) {
			final Pay pay = service.getById(callback.getOutTradeNo());
			if (pay == null) {
				log.error("未知交易的支付宝回调! tradeNo: {}", callback.getOutTradeNo());
			}
			else if (pay.getStatus() != PayStatus.WAIT) {
				log.error("重复的支付宝回调! tradeNo: {}", callback.getOutTradeNo());
			}
			else {
				pay.setThirdPartTradeNo(callback.getTradeNo());

				if (callback.getTradeStatus().equals(TradeStatus.SUCCESS)
						|| callback.getTradeStatus().equals(TradeStatus.FINISHED)) {
					service.success(pay);
				}
				else {
					service.fail(pay, "回调失败", null);
				}
			}
			return "success";
		}
		return "sign fail";
	}

	@PostMapping(LogFilter.WX)
	public String callback(@RequestBody String callbackStr) {
		callbackStr = callbackStr.replace("\n", "").replace("\r", "");
		WxPayCallback callback;
		try {
			callback = WxPayCallback.of(WxPayUtil.xmlToMap(callbackStr));
		}
		catch (Exception e) {
			log.error("微信回调解析异常, 回调内容: {}", callbackStr, e);
			return "<xml>\n  <return_code><![CDATA[FAIL]]></return_code>\n  "
					+ "<return_msg><![CDATA[解析异常]]></return_msg>\n</xml>";
		}
		log.info("微信回调: {}", callbackStr);
		if (callback.checkSign(wxPay)) {
			final Pay pay = service.getById(callback.getOutTradeNo());
			if (pay == null) {
				log.error("未知交易的微信回调! tradeNo: {}", callback.getOutTradeNo());
			}
			else if (pay.getStatus() != PayStatus.WAIT) {
				log.error("重复的微信回调! tradeNo: {}", callback.getOutTradeNo());
			}
			else {
				pay.setThirdPartTradeNo(callback.getTransactionId());

				if (callback.getReturnCode() == ResponseCode.SUCCESS
						&& callback.getResultCode() == ResponseCode.SUCCESS) {
					service.success(pay);
				}
				else {
					service.fail(pay, "回调失败", null);
				}
			}

			return WxPayConstant.CALLBACK_SUCCESS;
		}

		return WxPayConstant.CALLBACK_SIGN_ERROR;
	}

}
