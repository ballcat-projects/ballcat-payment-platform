package live.lingting.payment.sdk.model;

import java.util.HashSet;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;
import org.springframework.util.StringUtils;
import live.lingting.payment.sdk.enums.ThirdPart;
import live.lingting.payment.sdk.exception.MixException;
import live.lingting.payment.sdk.exception.MixRequestParamsValidException;

/**
 * @author lingting 2021/9/1 14:10
 */
@Getter
@Setter
public class MixBankModel extends MixModel {

	private static final Set<ThirdPart> ALLOW_TP;

	static {
		ALLOW_TP = new HashSet<>(16);
		ALLOW_TP.add(ThirdPart.BC_UNKNOWN);
	}

	private String mark;

	private ThirdPart tp;

	@Override
	public void valid() throws MixException {
		String msg = null;
		if (!StringUtils.hasText(getMark())) {
			msg = "支付配置不能为空!";
		}
		else if (!tpValid()) {
			msg = "该第三方不在允许范围内, 允许的第三方: " + ALLOW_TP;
		}

		if (StringUtils.hasText(msg)) {
			throw new MixRequestParamsValidException(msg);
		}
	}

	public boolean tpValid() {
		if (getTp() == null) {
			return false;
		}
		return ALLOW_TP.contains(getTp());
	}

}
