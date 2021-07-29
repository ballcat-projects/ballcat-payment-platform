package live.lingting.payment.biz.service;

import com.hccake.ballcat.common.model.domain.PageResult;
import com.hccake.extend.mybatis.plus.service.ExtendService;
import live.lingting.payment.Page;
import live.lingting.payment.entity.ProjectHistory;

/**
 * @author lingting 2021/6/4 13:39
 */
public interface ProjectHistoryService extends ExtendService<ProjectHistory> {

	/**
	 * 查询指定项目历史记录
	 * @param page 分页
	 * @param id id
	 * @return com.hccake.ballcat.common.model.domain.PageResult<live.lingting.entity.ProjectHistory>
	 * @author lingting 2021-06-07 10:29
	 */
	PageResult<ProjectHistory> listByProject(Page<ProjectHistory> page, Integer id);

}
