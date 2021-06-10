package live.lingting.sdk.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.util.StringUtils;
import live.lingting.sdk.exception.MixException;
import live.lingting.sdk.exception.MixRequestParamsValidException;
import live.lingting.sdk.util.MixUtils;

/**
 * 虚拟货币 - 提交 hash
 *
 * @author lingting 2021/6/7 17:17
 */
@Getter
@Setter
public class MixVirtualRetryModel extends MixModel {

	private static final long serialVersionUID = 1L;

	private String hash;

	@Override
	public void valid() throws MixException {
		validNo();
		if (StringUtils.hasText(getHash())) {
			setHash(MixUtils.clearHash(getHash()));
			if (!MixUtils.validHash(getHash())) {
				throw new MixRequestParamsValidException("请输入正确的hash!");
			}
		}
	}

}
