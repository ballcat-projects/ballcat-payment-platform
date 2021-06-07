package live.lingting.sdk.model;

import lombok.Getter;
import lombok.Setter;
import live.lingting.sdk.enums.Chain;
import live.lingting.sdk.enums.SdkContract;

/**
 * 虚拟货币 - 预下单
 * @author lingting 2021/6/7 17:17
 */
@Getter
@Setter
public class MixVirtualPayModel extends MixModel{
	private static final long serialVersionUID = 1L;


	private SdkContract contract;

	private Chain chain;
}
