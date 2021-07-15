package live.lingting.sdk.enums;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author lingting 2021/6/4 11:26
 */
@Getter
@AllArgsConstructor
public enum Currency {

	/**
	 * CNY
	 */
	CNY(false),
	/**
	 * USDT
	 */
	USDT(true),

	;

	public static final List<Currency> REAL_LIST;

	public static final List<Currency> VIRTUAL_LIST;

	static {
		List<Currency> realList = new ArrayList<>(16);
		List<Currency> virtualList = new ArrayList<>(16);

		for (Currency value : values()) {
			if (value.getVirtual()) {
				virtualList.add(value);
			}
			else {
				realList.add(value);
			}
		}

		// 不可修改
		REAL_LIST = Collections.unmodifiableList(realList);
		VIRTUAL_LIST = Collections.unmodifiableList(virtualList);
	}

	private final Boolean virtual;

}
