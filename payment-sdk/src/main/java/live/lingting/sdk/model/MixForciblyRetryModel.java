package live.lingting.sdk.model;

import lombok.Getter;
import lombok.Setter;
import live.lingting.sdk.exception.MixException;

/**
 * 强制重试
 *
 * @author lingting 2021/6/7 17:17
 */
@Getter
@Setter
public class MixForciblyRetryModel extends MixModel {

	private static final long serialVersionUID = 1L;

	@Override
	public void valid() throws MixException {
		validNo();
	}

}
