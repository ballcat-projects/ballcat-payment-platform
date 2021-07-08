package live.lingting.service;

import com.hccake.ballcat.common.model.domain.PageResult;
import com.hccake.extend.mybatis.plus.service.ExtendService;
import java.util.List;
import live.lingting.Page;
import live.lingting.dto.VirtualAddressCreateDTO;
import live.lingting.entity.Project;
import live.lingting.entity.VirtualAddress;
import live.lingting.enums.VirtualAddressMode;
import live.lingting.sdk.model.MixVirtualPayModel;

/**
 * @author lingting 2021/6/7 15:43
 */
public interface VirtualAddressService extends ExtendService<VirtualAddress> {

	/**
	 * 根据参数, 锁定地址
	 * @param model 参数
	 * @param project 项目
	 * @return live.lingting.entity.VirtualAddress
	 * @author lingting 2021-06-07 22:51
	 */
	VirtualAddress lock(MixVirtualPayModel model, Project project);

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
	 * @param ids 地址id
	 * @param disabled 禁用
	 * @author lingting 2021-06-08 14:07
	 */
	void disabled(List<Integer> ids, Boolean disabled);

	/**
	 * 添加地址
	 * @param dto 要新增的地址
	 * @return java.util.List<live.lingting.dto.VirtualAddressCreateDTO>
	 * @author lingting 2021-06-08 14:41
	 */
	VirtualAddressCreateDTO create(VirtualAddressCreateDTO dto);

	/**
	 * 更新模式
	 * @param ids 地址ID
	 * @param mode 新模式
	 * @author lingting 2021-07-08 11:04
	 */
	void mode(List<Integer> ids, VirtualAddressMode mode);

	/**
	 * 更新项目id
	 * @param ids 地址id
	 * @param projectIds 新项目id
	 * @author lingting 2021-07-08 11:05
	 */
	void project(List<Integer> ids, List<Integer> projectIds);

}
