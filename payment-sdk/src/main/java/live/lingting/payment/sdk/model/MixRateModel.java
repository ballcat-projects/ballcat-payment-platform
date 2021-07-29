package live.lingting.payment.sdk.model;

import lombok.Getter;
import lombok.Setter;
import live.lingting.payment.sdk.enums.Currency;
import live.lingting.payment.sdk.exception.MixException;
import live.lingting.payment.sdk.exception.MixRequestParamsValidException;

/**
 * 汇率
 *
 * @author lingting 2021/6/7 17:17
 */
@Getter
@Setter
public class MixRateModel extends MixModel {

	private static final long serialVersionUID = 1L;

	private Currency currency;

	@Override
	public void valid() throws MixException {
		if (currency == null) {
			throw new MixRequestParamsValidException("货币不能为空!");
		}
	}

}
