package live.lingting.constant;

import lombok.experimental.UtilityClass;
import live.lingting.entity.Pay;

/**
 * @author lingting 2021/6/9 17:34
 */
@UtilityClass
public class PayConstants {

	static final String PROJECT_TRADE_NO_LOCK = "project:trade:no:lock:%s:%s";

	static final String PAY_THIRD_TRADE_NO_LOCK = "pay:third:trade:no:lock:%s:%s";

	public static String getProjectTradeNoLock(Integer projectId, String projectTradeNo) {
		return String.format(PROJECT_TRADE_NO_LOCK, projectId, projectTradeNo);
	}

	public static String getPayThirdTradeNoLock(Pay pay, String thirdTradeNo) {
		String mark = pay.getChain() == null ? pay.getThirdPart().toString() : pay.getChain().toString();
		return String.format(PAY_THIRD_TRADE_NO_LOCK, mark, thirdTradeNo);
	}

}
