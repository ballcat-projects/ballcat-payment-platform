package live.lingting.constant;

import lombok.experimental.UtilityClass;
import live.lingting.sdk.enums.Chain;

/**
 * @author lingting 2021/6/9 17:34
 */
@UtilityClass
public class PayConstants {

	static final String PROJECT_TRADE_NO_LOCK = "project:trade:no:lock:%s:%s";

	public static String getProjectTradeNoLock(Integer projectId, String projectTradeNo) {
		return String.format(PROJECT_TRADE_NO_LOCK, projectId, projectTradeNo);
	}

	static final String VIRTUAL_HASH_LOCK = "pay:virtual:hash:lock:%s:%s";

	public static String getVirtualHashLock(Chain chain, String hash) {
		return String.format(VIRTUAL_HASH_LOCK, chain, hash);
	}

}
