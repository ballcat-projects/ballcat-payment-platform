package live.lingting.service;

import com.hccake.ballcat.common.model.domain.PageResult;
import com.hccake.extend.mybatis.plus.service.ExtendService;
import java.util.List;
import live.lingting.Page;
import live.lingting.dto.VirtualAddressCreateDTO;
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

	/**
	 * 解锁指定地址
	 * @param address 指定地址
	 * @return boolean
	 * @author lingting 2021-06-09 15:40
	 */
	boolean unlock(String address);

	/**
	 * 查询
	 * @param page 分页
	 * @param qo 条件
	 * @return com.hccake.ballcat.common.model.domain.PageResult<live.lingting.entity.VirtualAddress>
	 * @author lingting 2021-06-07 11:05
	 */
	PageResult<VirtualAddress> list(Page<VirtualAddress> page, VirtualAddress qo);

	/**
	 * 禁用指定地址
	 * @param id 地址id
	 * @param disabled 禁用
	 * @author lingting 2021-06-08 14:07
	 */
	void disabled(Integer id, Boolean disabled);

	/**
	 * 添加地址
	 * @param list 要新增的地址
	 * @return java.util.List<live.lingting.dto.VirtualAddressCreateDTO>
	 * @author lingting 2021-06-08 14:41
	 */
	List<VirtualAddressCreateDTO> create(List<VirtualAddressCreateDTO> list);

}
