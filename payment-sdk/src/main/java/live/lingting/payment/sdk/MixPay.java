package live.lingting.payment.sdk;

import java.math.BigDecimal;
import lombok.Getter;
import live.lingting.payment.sdk.client.DefaultMixClient;
import live.lingting.payment.sdk.client.MixClient;
import live.lingting.payment.sdk.enums.Chain;
import live.lingting.payment.sdk.enums.Currency;
import live.lingting.payment.sdk.enums.Mode;
import live.lingting.payment.sdk.enums.SdkContract;
import live.lingting.payment.sdk.enums.ThirdPart;
import live.lingting.payment.sdk.exception.MixException;
import live.lingting.payment.sdk.model.MixBankModel;
import live.lingting.payment.sdk.model.MixForciblyFailModel;
import live.lingting.payment.sdk.model.MixForciblyRetryModel;
import live.lingting.payment.sdk.model.MixQueryModel;
import live.lingting.payment.sdk.model.MixRateModel;
import live.lingting.payment.sdk.model.MixRealPayModel;
import live.lingting.payment.sdk.model.MixVirtualPayModel;
import live.lingting.payment.sdk.model.MixVirtualRetryModel;
import live.lingting.payment.sdk.model.MixVirtualSubmitModel;
import live.lingting.payment.sdk.request.MixBankRequest;
import live.lingting.payment.sdk.request.MixForciblyFailRequest;
import live.lingting.payment.sdk.request.MixForciblyRetryRequest;
import live.lingting.payment.sdk.request.MixQueryRequest;
import live.lingting.payment.sdk.request.MixRateRequest;
import live.lingting.payment.sdk.request.MixRealPayRequest;
import live.lingting.payment.sdk.request.MixVirtualPayRequest;
import live.lingting.payment.sdk.request.MixVirtualRetryRequest;
import live.lingting.payment.sdk.request.MixVirtualSubmitRequest;
import live.lingting.payment.sdk.response.MixBankResponse;
import live.lingting.payment.sdk.response.MixForciblyFailResponse;
import live.lingting.payment.sdk.response.MixForciblyRetryResponse;
import live.lingting.payment.sdk.response.MixQueryResponse;
import live.lingting.payment.sdk.response.MixRateResponse;
import live.lingting.payment.sdk.response.MixRealPayResponse;
import live.lingting.payment.sdk.response.MixVirtualPayResponse;
import live.lingting.payment.sdk.response.MixVirtualRetryResponse;
import live.lingting.payment.sdk.response.MixVirtualSubmitResponse;

/**
 * 混合支付
 *
 * @author lingting 2021/6/4 13:34
 */
@Getter
public class MixPay {

	private final String serverUrl;

	private final String apiKey;

	private final String apiSecurity;

	private final String notifyUrl;

	private final MixClient client;

	public MixPay(String serverUrl, String apiKey, String apiSecurity, String notifyUrl) {
		this(serverUrl, apiKey, apiSecurity, notifyUrl, new DefaultMixClient(serverUrl, apiKey, apiSecurity));
	}

	public MixPay(String serverUrl, String apiKey, String apiSecurity, String notifyUrl, MixClient client) {
		this.serverUrl = serverUrl;
		this.apiKey = apiKey;
		this.apiSecurity = apiSecurity;
		this.notifyUrl = notifyUrl;
		this.client = client;
	}

	/**
	 * 真实货币 - 下单 - 二维码
	 *
	 * @author lingting 2021-07-14 17:29
	 */
	public MixRealPayResponse realQrPay(String projectTradeNo, BigDecimal amount, ThirdPart thirdPart,
			Currency currency, String mark, String subject) throws MixException {
		MixRealPayModel model = new MixRealPayModel();
		model.setProjectTradeNo(projectTradeNo);
		model.setAmount(amount);
		model.setThirdPart(thirdPart);
		model.setCurrency(currency);
		model.setSubject(subject);
		model.setMode(Mode.QR);
		model.setMark(mark);
		model.setNotifyUrl(getNotifyUrl());
		return realPay(model);
	}

	/**
	 * 真实货币 - 下单 - 转账
	 *
	 * @author lingting 2021-07-14 17:29
	 */
	public MixRealPayResponse realTransferPay(String projectTradeNo, String thirdPartTradeNo, ThirdPart thirdPart,
			Currency currency, String mark) throws MixException {
		MixRealPayModel model = new MixRealPayModel();
		model.setProjectTradeNo(projectTradeNo);
		model.setThirdPartTradeNo(thirdPartTradeNo);
		model.setThirdPart(thirdPart);
		model.setCurrency(currency);
		model.setMode(Mode.TRANSFER);
		model.setMark(mark);
		model.setNotifyUrl(getNotifyUrl());
		return realPay(model);
	}

	/**
	 * 真实货币 - 下单
	 *
	 * @author lingting 2021-07-14 17:34
	 */
	public MixRealPayResponse realPay(MixRealPayModel model) throws MixException {
		MixRealPayRequest request = new MixRealPayRequest();
		request.setModel(model);
		return getClient().execute(request);
	}

	/**
	 * 虚拟货币 - 下单
	 *
	 * @author lingting 2021-06-10 09:49
	 */
	public MixVirtualPayResponse virtualPay(String projectTradeNo, SdkContract contract, Chain chain)
			throws MixException {
		MixVirtualPayModel model = new MixVirtualPayModel();
		model.setChain(chain);
		model.setContract(contract);
		model.setProjectTradeNo(projectTradeNo);
		model.setNotifyUrl(getNotifyUrl());
		return virtualPay(model);
	}

	public MixVirtualPayResponse virtualPay(MixVirtualPayModel model) throws MixException {
		MixVirtualPayRequest request = new MixVirtualPayRequest();
		request.setModel(model);
		return getClient().execute(request);
	}

	/**
	 * 虚拟货币 - 提交hash
	 *
	 * @author lingting 2021-06-10 09:49
	 */
	public MixVirtualSubmitResponse virtualSubmit(String tradeNo, String projectTradeNo, String hash)
			throws MixException {
		MixVirtualSubmitModel model = new MixVirtualSubmitModel();
		model.setTradeNo(tradeNo);
		model.setProjectTradeNo(projectTradeNo);
		model.setHash(hash);
		return virtualSubmit(model);
	}

	public MixVirtualSubmitResponse virtualSubmit(MixVirtualSubmitModel model) throws MixException {
		MixVirtualSubmitRequest request = new MixVirtualSubmitRequest();
		request.setModel(model);
		return getClient().execute(request);
	}

	/**
	 * 虚拟货币 - 重试
	 *
	 * @author lingting 2021-06-10 09:49
	 */
	public MixVirtualRetryResponse virtualRetry(String tradeNo, String projectTradeNo, String hash)
			throws MixException {
		MixVirtualRetryModel model = new MixVirtualRetryModel();
		model.setTradeNo(tradeNo);
		model.setProjectTradeNo(projectTradeNo);
		model.setHash(hash);
		return virtualRetry(model);
	}

	public MixVirtualRetryResponse virtualRetry(MixVirtualRetryModel model) throws MixException {
		MixVirtualRetryRequest request = new MixVirtualRetryRequest();
		request.setModel(model);
		return getClient().execute(request);
	}

	public MixBankResponse bank(String mark, ThirdPart tp) throws MixException {
		MixBankModel model = new MixBankModel();
		model.setMark(mark);
		model.setTp(tp);
		return bank(model);
	}

	public MixBankResponse bank(MixBankModel model) throws MixException {
		MixBankRequest request = new MixBankRequest();
		request.setModel(model);
		return getClient().execute(request);
	}

	/**
	 * 强制重试
	 *
	 * @author lingting 2021-06-10 13:40
	 */
	public MixForciblyRetryResponse forciblyRetry(String tradeNo, String projectTradeNo) throws MixException {
		MixForciblyRetryModel model = new MixForciblyRetryModel();
		model.setTradeNo(tradeNo);
		model.setProjectTradeNo(projectTradeNo);
		return forciblyRetry(model);
	}

	public MixForciblyRetryResponse forciblyRetry(MixForciblyRetryModel model) throws MixException {
		MixForciblyRetryRequest request = new MixForciblyRetryRequest();
		request.setModel(model);
		return getClient().execute(request);
	}

	/**
	 * 强制失败
	 *
	 * @author lingting 2021-06-10 13:40
	 */
	public MixForciblyFailResponse forciblyFail(String tradeNo, String projectTradeNo) throws MixException {
		MixForciblyFailModel model = new MixForciblyFailModel();
		model.setTradeNo(tradeNo);
		model.setProjectTradeNo(projectTradeNo);
		return forciblyFail(model);
	}

	public MixForciblyFailResponse forciblyFail(MixForciblyFailModel model) throws MixException {
		MixForciblyFailRequest request = new MixForciblyFailRequest();
		request.setModel(model);
		return getClient().execute(request);
	}

	/**
	 * 查询
	 *
	 * @author lingting 2021-06-10 13:40
	 */
	public MixQueryResponse query(String tradeNo, String projectTradeNo) throws MixException {
		MixQueryModel model = new MixQueryModel();
		model.setTradeNo(tradeNo);
		model.setProjectTradeNo(projectTradeNo);
		return query(model);
	}

	public MixQueryResponse query(MixQueryModel model) throws MixException {
		MixQueryRequest request = new MixQueryRequest();
		request.setModel(model);
		return getClient().execute(request);
	}

	/**
	 * 汇率
	 *
	 * @author lingting 2021-06-10 14:28
	 */
	public MixRateResponse rate(Currency currency) throws MixException {
		MixRateModel model = new MixRateModel();
		model.setCurrency(currency);
		return rate(model);
	}

	public MixRateResponse rate(MixRateModel model) throws MixException {
		MixRateRequest request = new MixRateRequest();
		request.setModel(model);
		return getClient().execute(request);
	}

}
