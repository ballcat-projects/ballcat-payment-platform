package live.lingting.payment.sdk.request;

import live.lingting.payment.http.HttpMethod;
import live.lingting.payment.sdk.constant.SdkConstants;
import live.lingting.payment.sdk.model.MixBankModel;
import live.lingting.payment.sdk.response.MixBankResponse;

/**
 * @author lingting 2021/9/1 14:18
 */
public class MixBankRequest extends AbstractMixRequest<MixBankModel, MixBankResponse> {

	@Override
	public String getPath() {
		return SdkConstants.MIX_BANK;
	}

	@Override
	public HttpMethod getMethod() {
		return HttpMethod.GET;
	}

}
