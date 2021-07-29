package live.lingting.payment.sdk.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.util.StringUtils;
import live.lingting.payment.sdk.enums.Chain;
import live.lingting.payment.sdk.enums.SdkContract;
import live.lingting.payment.sdk.exception.MixException;
import live.lingting.payment.sdk.exception.MixRequestParamsValidException;

/**
 * 虚拟货币 - 预下单
 *
 * @author lingting 2021/6/7 17:17
 */
@Getter
@Setter
public class MixVirtualPayModel extends MixModel {

	private static final long serialVersionUID = 1L;

	private SdkContract contract;

	private Chain chain;

	@Override
	public void valid() throws MixException {
		validNotifyUrl();
		String field;

		if (!StringUtils.hasText(getProjectTradeNo())) {
			field = "项目交易号";
		}
		else if (getContract() == null) {
			field = "合约";
		}
		else if (getChain() == null) {
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
