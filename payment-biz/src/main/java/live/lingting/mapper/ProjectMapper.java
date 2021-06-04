package live.lingting.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.hccake.ballcat.common.model.domain.PageResult;
import com.hccake.extend.mybatis.plus.mapper.ExtendMapper;
import com.hccake.extend.mybatis.plus.toolkit.WrappersX;
import live.lingting.Page;
import live.lingting.entity.Project;

/**
 * @author lingting 2021/6/4 13:35
 */
public interface ProjectMapper extends ExtendMapper<Project> {

	default Wrapper<Project> getWrapper(Project project) {
		return WrappersX.<Project>lambdaQueryX()
				// 名称
				.likeIfPresent(Project::getName, project.getName())
				// key
				.eqIfPresent(Project::getApiKey, project.getApiKey())
				// 禁用
				.eqIfPresent(Project::getDisabled, project.getDisabled());
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

}
