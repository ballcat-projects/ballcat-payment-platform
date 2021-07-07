package live.lingting.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import com.hccake.ballcat.common.model.domain.PageResult;
import com.hccake.extend.mybatis.plus.conditions.query.LambdaQueryWrapperX;
import com.hccake.extend.mybatis.plus.mapper.ExtendMapper;
import com.hccake.extend.mybatis.plus.toolkit.WrappersX;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Select;
import org.springframework.util.CollectionUtils;
import live.lingting.Page;
import live.lingting.entity.VirtualAddress;
import live.lingting.enums.ProjectMode;
import live.lingting.sdk.enums.Chain;

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

		final LambdaQueryWrapperX<VirtualAddress> wrapperX = WrappersX.<VirtualAddress>lambdaQueryX()
				// address
				.eqIfPresent(VirtualAddress::getAddress, va.getAddress())
				// chain
				.eqIfPresent(VirtualAddress::getChain, va.getChain())
				// disabled
				.eqIfPresent(VirtualAddress::getDisabled, va.getDisabled())
				// using
				.eqIfPresent(VirtualAddress::getUsing, va.getUsing())
				// mode
				.eqIfPresent(VirtualAddress::getMode, va.getMode());
		if (!CollectionUtils.isEmpty(va.getProjectIds())) {
			wrapperX.apply(String.format(" JSON_CONTAINS(project_ids, '%s') ", va.getProjectIds().get(0)));
		}

		return wrapperX;
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
	 * 加载允许指定项目使用的地址
	 * @param chain 链
	 * @param id 项目id
	 * @param mode 项目模式
	 * @return java.util.List<live.lingting.entity.VirtualAddress>
	 * @author lingting 2021-07-05 21:04
	 */
	@Select("SELECT\n * \nFROM\n `virtual_address` va \nWHERE\n va.`chain` = '${chain}' \n"
			+ " AND va.`using` = 0 \n AND va.disabled = 0\n"
			+ " \n AND (\n IF\n  (\n   '${p_mode}' = 'ALLOW',-- 可以获取所有可用的地址\n   (\n"
			+ "    ( va.`mode` = 'INCLUDE' AND JSON_CONTAINS( va.project_ids, '${p_id}' ) ) \n"
			+ "    OR ( va.`mode` = 'EXCLUDE' AND ( va.project_ids IS NULL OR NOT JSON_CONTAINS( va.project_ids, '${p_id}' ) ) ) \n"
			+ "   ),-- 只能获取仅限该项目使用地址\n"
			+ "   ( va.`mode` = 'INCLUDE' AND JSON_CONTAINS( va.project_ids, '${p_id}' ) ) \n  ) \n )")
	@ResultMap("mybatis-plus_VirtualAddress")
	List<VirtualAddress> load(@Param("chain") Chain chain, @Param("p_id") Integer id,
			@Param("p_mode") ProjectMode mode);

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
	 * 解锁指定地址
	 * @param address 地址
	 * @return boolean
	 * @author lingting 2021-06-09 15:41
	 */
	default boolean unlock(String address) {
		final LambdaUpdateWrapper<VirtualAddress> wrapper = Wrappers.<VirtualAddress>lambdaUpdate()
				// 限定地址
				.eq(VirtualAddress::getAddress, address)
				// 限定使用状态
				.eq(VirtualAddress::getUsing, true)
				// 改为未使用
				.set(VirtualAddress::getUsing, false);

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
