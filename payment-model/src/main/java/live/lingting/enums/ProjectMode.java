package live.lingting.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 项目模式
 * @author lingting 2021/7/5 18:49
 */
@Getter
@AllArgsConstructor
public enum ProjectMode {

	/**
	 * 可以获取所有可用的地址
	 */
	ALLOW,
	/**
	 * 只能获取仅限该项目使用的地址
	 */
	INCLUDE,

	;

}
