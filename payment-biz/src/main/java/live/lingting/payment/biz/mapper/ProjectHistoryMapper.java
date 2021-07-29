package live.lingting.payment.biz.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.hccake.ballcat.common.model.domain.PageResult;
import com.hccake.extend.mybatis.plus.mapper.ExtendMapper;
import live.lingting.payment.Page;
import live.lingting.payment.entity.ProjectHistory;

/**
 * @author lingting 2021/6/4 13:38
 */
public interface ProjectHistoryMapper extends ExtendMapper<ProjectHistory> {

	/**
	 * 查询指定项目历史记录
	 * @param page 分页
	 * @param id id
	 * @return com.hccake.ballcat.common.model.domain.PageResult<live.lingting.entity.ProjectHistory>
	 * @author lingting 2021-06-07 10:29
	 */
	default PageResult<ProjectHistory> listByProject(Page<ProjectHistory> page, Integer id) {
		final IPage<ProjectHistory> iPage = selectPage(page.toPage(),
				Wrappers.<ProjectHistory>lambdaQuery().eq(ProjectHistory::getProjectId, id));

		final PageResult<ProjectHistory> pageResult = new PageResult<>();
		pageResult.setRecords(iPage.getRecords());
		pageResult.setTotal(iPage.getTotal());
		return pageResult;
	}

}
