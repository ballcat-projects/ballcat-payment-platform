package live.lingting.service;

import com.hccake.extend.mybatis.plus.service.ExtendService;
import live.lingting.entity.VirtualAddress;
import live.lingting.sdk.model.MixVirtualPayModel;

/**
 * @author lingting 2021/6/7 15:43
 */
public interface VirtualAddressService extends ExtendService<VirtualAddress> {

	/**
	 * 根据参数, 锁定地址
	 * @param model 参数
	 * @return live.lingting.entity.VirtualAddress
	 * @author lingting 2021-06-07 22:51
	 */
	VirtualAddress lock(MixVirtualPayModel model);

}
