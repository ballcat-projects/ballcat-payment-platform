package live.lingting.payment.biz.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;
import live.lingting.payment.Page;
import live.lingting.payment.biz.mybatis.WrappersX;
import live.lingting.payment.entity.Project;
import live.lingting.payment.enums.ProjectScope;

/**
 * @author lingting 2021/6/4 13:35
 */
public interface ProjectMapper extends BaseMapper<Project> {

	/**
	 * 组装sql
	 * @param project 条件
	 * @return com.baomidou.mybatisplus.core.conditions.Wrapper<live.lingting.payment.entity.Project>
	 * @author lingting 2021-06-07 14:08
	 */
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
	 * @return live.lingting.payment.Page<live.lingting.payment.entity.Project>
	 * @author lingting 2021-06-04 16:32
	 */
	default Page<Project> list(Page<Project> page, Project project) {
		final IPage<Project> iPage = selectPage(page.toPage(), getWrapper(project));
		return new Page<>(iPage.getRecords(), iPage.getTotal());
	}

	/**
	 * 更新项目权限
	 * @param ids id
	 * @param scopes 新权限
	 * @author lingting 2021-07-16 16:25
	 */
	@Update("UPDATE lingting_payment_project p SET p.scope=#{scopes,typeHandler=live.lingting.payment.entity.Project$ScopeTypeHandler} WHERE p"
			+ ".id IN (${@cn.hutool.core.util.StrUtil@join(\",\", ids.toArray())}) ")
	void scope(@Param("ids") List<Integer> ids, @Param("scopes") List<ProjectScope> scopes);

}
