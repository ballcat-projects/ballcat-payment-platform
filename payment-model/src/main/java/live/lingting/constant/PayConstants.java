package live.lingting.constant;

import lombok.experimental.UtilityClass;

/**
 * @author lingting 2021/6/9 17:34
 */
@UtilityClass
public class PayConstants {

	static final String PROJECT_TRADE_NO_LOCK = "project:trade:no:lock:%s:%s";

	public static String getProjectTradeNoLock(Integer projectId,String projectTradeNo) {
		return String.format(PROJECT_TRADE_NO_LOCK, projectId, projectTradeNo);
	}

}
