package live.lingting.payment.biz.real;

import com.alipay.api.AlipayApiException;
import com.alipay.api.response.AlipayTradePrecreateResponse;
import java.math.BigDecimal;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import live.lingting.payment.ali.AliPay;
import live.lingting.payment.ali.constants.AliPayConstant;
import live.lingting.payment.biz.real.third.AliManager;
import live.lingting.payment.biz.real.third.WxManager;
import live.lingting.payment.biz.service.PayService;
import live.lingting.payment.entity.Pay;
import live.lingting.payment.entity.Project;
import live.lingting.payment.enums.ResponseCode;
import live.lingting.payment.exception.PaymentException;
import live.lingting.payment.sdk.enums.Mode;
import live.lingting.payment.sdk.enums.NotifyStatus;
import live.lingting.payment.sdk.enums.PayStatus;
import live.lingting.payment.sdk.model.MixRealPayModel;
import live.lingting.payment.sdk.response.MixRealPayResponse;
import live.lingting.payment.wx.WxPay;
import live.lingting.payment.wx.response.WxPayResponse;

/**
 * @author lingting 2021/7/14 14:48
 */
@Component
@RequiredArgsConstructor
public class RealManager {

	private final PayService service;

	private final AliManager aliManager;

	private final WxManager wxManager;

	@Transactional(rollbackFor = Exception.class)
	public MixRealPayResponse.Data pay(Project project, MixRealPayModel model)
			throws AlipayApiException, PaymentException {
		Pay pay = new Pay().setProjectId(project.getId()).setProjectTradeNo(model.getProjectTradeNo())
				.setThirdPartTradeNo(model.getMode().equals(Mode.TRANSFER) ? model.getThirdPartTradeNo() : "")
				.setStatus(PayStatus.WAIT)
				.setAmount(model.getMode().equals(Mode.TRANSFER) ? BigDecimal.ZERO : model.getAmount())
				.setCurrency(model.getCurrency()).setThirdPart(model.getThirdPart()).setMode(model.getMode())
				.setNotifyUrl(model.getNotifyUrl()).setNotifyStatus(NotifyStatus.WAIT);

		// 转账需要校验交易号
		if (pay.getMode().equals(Mode.TRANSFER)) {
			service.validateThirdTradeNo(pay, pay.getThirdPartTradeNo());
		}
		else {
			if (StringUtils.hasText(project.getMark())) {
				model.setSubject(project.getMark() + " " + model.getSubject());
			}
		}

		service.save(pay);
		switch (model.getThirdPart()) {
		case ALI:
			return aliPay(pay, model);
		case WX:
			return wxPay(pay, model);
		default:
			throw new PaymentException(ResponseCode.UNKNOWN_THIRD_PARTY);
		}
	}

	private MixRealPayResponse.Data aliPay(Pay pay, MixRealPayModel model) throws AlipayApiException, PaymentException {
		MixRealPayResponse.Data data = new MixRealPayResponse.Data();
		data.setTradeNo(pay.getTradeNo());
		if (model.getMode().equals(Mode.QR)) {
			final AlipayTradePrecreateResponse qrPay = getAliPay(model).qrPay(pay.getTradeNo(), pay.getAmount(),
					model.getSubject());
			if (qrPay.getCode().equals(AliPayConstant.CODE_SUCCESS)) {
				data.setQr(qrPay.getQrCode());
			}
			else {
				String errMsg = qrPay.getMsg();
				if (StringUtils.hasText(qrPay.getSubCode())) {
					errMsg = qrPay.getSubMsg();
				}

				throw new PaymentException(ResponseCode.ABNORMAL_PAYMENT_GENERATED, errMsg);
			}
		}

		return data;
	}

	private MixRealPayResponse.Data wxPay(Pay pay, MixRealPayModel model) throws PaymentException {
		MixRealPayResponse.Data data = new MixRealPayResponse.Data();
		data.setTradeNo(pay.getTradeNo());
		if (model.getMode().equals(Mode.QR)) {
			final WxPayResponse nativePay = getWxPay(model).nativePay(pay.getTradeNo(), model.getAmount(),
					model.getSubject());
			if (nativePay.getReturnCode().equals(live.lingting.payment.wx.enums.ResponseCode.SUCCESS)
					&& nativePay.getResultCode().equals(live.lingting.payment.wx.enums.ResponseCode.SUCCESS)) {
				data.setQr(nativePay.getCodeUrl());
			}
			else {
				String errMsg = nativePay.getReturnMsg();
				if (nativePay.getResultCode() != null
						&& !live.lingting.payment.wx.enums.ResponseCode.SUCCESS.equals(nativePay.getResultCode())) {
					errMsg = nativePay.getErrCodeDes();
				}

				throw new PaymentException(ResponseCode.ABNORMAL_PAYMENT_GENERATED, errMsg);
			}
		}
		else {
			throw new PaymentException(ResponseCode.THIRD_PARTY_NOT_SUPPORT);
		}

		return data;
	}

	private AliPay getAliPay(MixRealPayModel model) throws PaymentException {
		return aliManager.get(model.getMark());
	}

	private WxPay getWxPay(MixRealPayModel model) throws PaymentException {
		return wxManager.get(model.getMark());
	}

}
