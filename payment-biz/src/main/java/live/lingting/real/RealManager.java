package live.lingting.real;

import com.alipay.api.AlipayApiException;
import com.alipay.api.response.AlipayTradePrecreateResponse;
import com.hccake.ballcat.common.core.exception.BusinessException;
import com.hccake.extend.pay.ali.AliPay;
import com.hccake.extend.pay.ali.constants.AliPayConstant;
import com.hccake.extend.pay.wx.WxPay;
import com.hccake.extend.pay.wx.response.WxPayResponse;
import java.math.BigDecimal;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import live.lingting.entity.Pay;
import live.lingting.entity.Project;
import live.lingting.enums.ResponseCode;
import live.lingting.sdk.enums.Mode;
import live.lingting.sdk.enums.NotifyStatus;
import live.lingting.sdk.enums.PayStatus;
import live.lingting.sdk.model.MixRealPayModel;
import live.lingting.sdk.response.MixRealPayResponse;
import live.lingting.service.PayService;

/**
 * @author lingting 2021/7/14 14:48
 */
@Component
@RequiredArgsConstructor
public class RealManager {

	private final PayService service;

	private final AliPay aliPay;

	private final WxPay wxPay;

	@Transactional(rollbackFor = Exception.class)
	public MixRealPayResponse.Data pay(Project project, MixRealPayModel model) throws AlipayApiException {
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
			model.setSubject(project.getMark() + " " + model.getSubject());
		}

		service.save(pay);
		switch (model.getThirdPart()) {
		case ALI:
			return aliPay(pay, model);
		case WX:
			return wxPay(pay, model);
		default:
			throw new BusinessException(ResponseCode.UNKNOWN_THIRD_PARTY);
		}
	}

	private MixRealPayResponse.Data aliPay(Pay pay, MixRealPayModel model) throws AlipayApiException {
		MixRealPayResponse.Data data = new MixRealPayResponse.Data();
		data.setTradeNo(pay.getTradeNo());
		if (model.getMode().equals(Mode.QR)) {
			final AlipayTradePrecreateResponse qrPay = aliPay.qrPay(pay.getTradeNo(), pay.getAmount(),
					model.getSubject());
			if (qrPay.getCode().equals(AliPayConstant.CODE_SUCCESS)) {
				data.setQr(qrPay.getQrCode());
			}
			else {
				String errMsg = qrPay.getMsg();
				if (StringUtils.hasText(qrPay.getSubCode())) {
					errMsg = qrPay.getSubMsg();
				}

				throw new BusinessException(ResponseCode.ABNORMAL_PAYMENT_GENERATED, errMsg);
			}
		}

		return data;
	}

	private MixRealPayResponse.Data wxPay(Pay pay, MixRealPayModel model) {
		MixRealPayResponse.Data data = new MixRealPayResponse.Data();
		data.setTradeNo(pay.getTradeNo());
		if (model.getMode().equals(Mode.QR)) {
			final WxPayResponse nativePay = wxPay.nativePay(pay.getTradeNo(), model.getAmount(), model.getSubject());
			if (nativePay.getReturnCode().equals(com.hccake.extend.pay.wx.enums.ResponseCode.SUCCESS)
					&& nativePay.getResultCode().equals(com.hccake.extend.pay.wx.enums.ResponseCode.SUCCESS)) {
				data.setQr(nativePay.getCodeUrl());
			}
			else {
				String errMsg = nativePay.getReturnMsg();
				if (nativePay.getResultCode() != null
						&& !com.hccake.extend.pay.wx.enums.ResponseCode.SUCCESS.equals(nativePay.getResultCode())) {
					errMsg = nativePay.getErrCodeDes();
				}

				throw new BusinessException(ResponseCode.ABNORMAL_PAYMENT_GENERATED, errMsg);
			}
		}
		else {
			throw new BusinessException(ResponseCode.THIRD_PARTY_NOT_SUPPORT);
		}

		return data;
	}

}
