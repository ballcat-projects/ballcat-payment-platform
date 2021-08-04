package live.lingting.payment.biz.service;

import live.lingting.payment.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import live.lingting.payment.Page;
import live.lingting.payment.entity.ProjectHistory;

/**
 * @author lingting 2021/6/4 13:39
 */
public interface ProjectHistoryService extends IService<ProjectHistory> {

	/**
	 * 查询指定项目历史记录
	 * @param page 分页
	 * @param id id
	 * @return live.lingting.payment.Page<live.lingting.entity.ProjectHistory>
	 * @author lingting 2021-06-07 10:29
	 */
	Page<ProjectHistory> listByProject(Page<ProjectHistory> page, Integer id);

}
