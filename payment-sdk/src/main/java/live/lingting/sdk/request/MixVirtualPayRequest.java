package live.lingting.sdk.request;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.util.StringUtils;
import live.lingting.sdk.constant.SdkConstants;
import live.lingting.sdk.exception.MixException;
import live.lingting.sdk.exception.MixRequestParamsValidException;
import live.lingting.sdk.model.MixVirtualPayModel;
import live.lingting.sdk.response.MixVirtualPayResponse;

/**
 * 虚拟货币 - 预下单
 *
 * @author lingting 2021/6/7 17:21
 */
@Getter
@Setter
@Accessors(chain = true)
public class MixVirtualPayRequest extends AbstractMixRequest<MixVirtualPayModel, MixVirtualPayResponse> {

	@Override
	public String getPath() {
		return SdkConstants.MIX_VIRTUAL_PAY_PATH;
	}

	@Override
	public void valid() throws MixException {
		validNotifyUrl();
		final MixVirtualPayModel model = getModel();

		String field;

		if (!StringUtils.hasText(model.getProjectTradeNo())) {
			field = "项目交易号";
		}
		else if (model.getContract() == null) {
			field = "合约";
		}
		else if (model.getChain() == null) {
			field = "链";
		}
		else {
			field = null;
		}

		if (StringUtils.hasText(field)) {
			throw new MixRequestParamsValidException(field + "不能为空");
		}

	}

}
