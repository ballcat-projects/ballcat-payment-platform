package live.lingting.payment.biz.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.hccake.ballcat.common.model.domain.PageResult;
import com.hccake.extend.mybatis.plus.mapper.ExtendMapper;
import com.hccake.extend.mybatis.plus.toolkit.WrappersX;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;
import live.lingting.payment.Page;
import live.lingting.payment.entity.Project;
import live.lingting.payment.enums.ProjectMode;
import live.lingting.payment.enums.ProjectScope;

/**
 * @author lingting 2021/6/4 13:35
 */
public interface ProjectMapper extends ExtendMapper<Project> {

	/**
	 * 组装sql
	 * @param project 条件
	 * @return com.baomidou.mybatisplus.core.conditions.Wrapper<live.lingting.entity.Project>
	 * @author lingting 2021-06-07 14:08
	 */
	default Wrapper<Project> getWrapper(Project project) {
		return WrappersX.<Project>lambdaQueryX()
				// 名称
				.likeIfPresent(Project::getName, project.getName())
				// key
				.eqIfPresent(Project::getApiKey, project.getApiKey())
				// 禁用
				.eqIfPresent(Project::getDisabled, project.getDisabled())
				// 模式
				.eqIfPresent(Project::getMode, project.getMode());
	}

	/**
	 * 搜索
	 * @param page 分页
	 * @param project 项目
	 * @return com.hccake.ballcat.common.model.domain.PageResult<live.lingting.entity.Project>
	 * @author lingting 2021-06-04 16:32
	 */
	default PageResult<Project> list(Page<Project> page, Project project) {
		final IPage<Project> iPage = selectPage(page.toPage(), getWrapper(project));
		return new PageResult<>(iPage.getRecords(), iPage.getTotal());
	}

	/**
	 * 更新项目模式
	 * @param ids 项目
	 * @param mode 新模式
	 * @author lingting 2021-07-07 10:15
	 */
	default void mode(List<Integer> ids, ProjectMode mode) {
		final LambdaUpdateWrapper<Project> wrapper = Wrappers.<Project>lambdaUpdate().in(Project::getId, ids)
				.set(Project::getMode, mode);

		update(null, wrapper);
	}

	/**
	 * 更新项目权限
	 * @param ids id
	 * @param scopes 新权限
	 * @author lingting 2021-07-16 16:25
	 */
	@Update("UPDATE project p SET p.scope=#{scopes,typeHandler=live.lingting.entity.Project$ScopeTypeHandler} WHERE p"
			+ ".id IN (${@cn.hutool.core.util.StrUtil@join(\",\", ids.toArray())}) ")
	void scope(@Param("ids") List<Integer> ids, @Param("scopes") List<ProjectScope> scopes);

}
