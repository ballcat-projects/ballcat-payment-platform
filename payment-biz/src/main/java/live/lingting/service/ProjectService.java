package live.lingting.service;

import com.hccake.ballcat.common.model.domain.PageResult;
import com.hccake.extend.mybatis.plus.service.ExtendService;
import live.lingting.Page;
import live.lingting.entity.Project;

/**
 * @author lingting 2021/6/4 13:36
 */
public interface ProjectService extends ExtendService<Project> {

	/**
	 * 搜索
	 * @param page 分页
	 * @param project 项目
	 * @return com.hccake.ballcat.common.model.domain.PageResult<live.lingting.entity.Project>
	 * @author lingting 2021-06-04 16:32
	 */
	PageResult<Project> list(Page<Project> page, Project project);

	/**
	 * 新增项目
	 * @param project 项目
	 * @author lingting 2021-06-04 17:15
	 */
	void create(Project project);

	/**
	 * 重置API
	 * @param id 项目 Id
	 * @author lingting 2021-06-04 17:19
	 */
	void resetApi(Integer id);

	/**
	 * 更新禁用信息
	 * @param id id
	 * @param disabled 是否禁用
	 * @author lingting 2021-06-04 17:26
	 */
	void disabled(Integer id, Boolean disabled);

}
