package live.lingting.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import com.hccake.ballcat.common.model.domain.PageResult;
import com.hccake.extend.mybatis.plus.mapper.ExtendMapper;
import com.hccake.extend.mybatis.plus.toolkit.WrappersX;
import live.lingting.Page;
import live.lingting.entity.VirtualAddress;

/**
 * @author lingting 2021/6/7 15:43
 */
public interface VirtualAddressMapper extends ExtendMapper<VirtualAddress> {

	/**
	 * 组装sql
	 * @param va 条件
	 * @return com.baomidou.mybatisplus.core.conditions.Wrapper<live.lingting.entity.Pay>
	 * @author lingting 2021-06-07 14:08
	 */
	default Wrapper<VirtualAddress> getWrapper(VirtualAddress va) {
		return WrappersX.<VirtualAddress>lambdaQueryX()
				// address
				.eqIfPresent(VirtualAddress::getAddress, va.getAddress())
				// chain
				.eqIfPresent(VirtualAddress::getChain, va.getChain())
				// disabled
				.eqIfPresent(VirtualAddress::getDisabled, va.getDisabled())
				// using
				.eqIfPresent(VirtualAddress::getUsing, va.getUsing());
	}

	/**
	 * 查询
	 * @param page 分页
	 * @param va 条件
	 * @return com.hccake.ballcat.common.model.domain.PageResult<live.lingting.entity.VirtualAddress>
	 * @author lingting 2021-06-07 11:05
	 */
	default PageResult<VirtualAddress> list(Page<VirtualAddress> page, VirtualAddress va) {
		final IPage<VirtualAddress> iPage = selectPage(page.toPage(), getWrapper(va));

		final PageResult<VirtualAddress> pageResult = new PageResult<>();
		pageResult.setRecords(iPage.getRecords());
		pageResult.setTotal(iPage.getTotal());
		return pageResult;
	}

	/**
	 * 上锁指定地址
	 * @param va 地址
	 * @return boolean
	 * @author lingting 2021-06-07 22:58
	 */
	default boolean lock(VirtualAddress va) {
		final LambdaUpdateWrapper<VirtualAddress> wrapper = Wrappers.<VirtualAddress>lambdaUpdate()
				// 限定地址
				.eq(VirtualAddress::getId, va.getId())
				// 限定使用状态
				.eq(VirtualAddress::getUsing, false)
				// 改为已使用
				.set(VirtualAddress::getUsing, true);

		return SqlHelper.retBool(update(null, wrapper));
	}

	/**
	 * 禁用指定地址
	 * @param id 地址id
	 * @param disabled 禁用
	 * @author lingting 2021-06-08 14:07
	 */
	default void disabled(Integer id, Boolean disabled) {
		final LambdaUpdateWrapper<VirtualAddress> wrapper = Wrappers.<VirtualAddress>lambdaUpdate()
				// 限定地址
				.eq(VirtualAddress::getId, id)
				// 设置禁用
				.set(VirtualAddress::getDisabled, disabled);

		update(null, wrapper);
	}

}
