package live.lingting.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 地址模式
 * @author lingting 2021/7/5 18:49
 */
@Getter
@AllArgsConstructor
public enum VirtualAddressMode {

	/**
	 * 禁止指定项目获取
	 */
	EXCLUDE,
	/**
	 * 仅允许指定项目获取
	 */
	INCLUDE,

	;

}
