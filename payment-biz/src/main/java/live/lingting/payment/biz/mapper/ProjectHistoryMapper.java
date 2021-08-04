package live.lingting.payment.biz.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import live.lingting.payment.Page;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import live.lingting.payment.Page;
import live.lingting.payment.entity.ProjectHistory;

/**
 * @author lingting 2021/6/4 13:38
 */
public interface ProjectHistoryMapper extends BaseMapper<ProjectHistory> {

	/**
	 * 查询指定项目历史记录
	 * @param page 分页
	 * @param id id
	 * @return live.lingting.payment.Page<live.lingting.entity.ProjectHistory>
	 * @author lingting 2021-06-07 10:29
	 */
	default Page<ProjectHistory> listByProject(Page<ProjectHistory> page, Integer id) {
		final IPage<ProjectHistory> iPage = selectPage(page.toPage(),
				Wrappers.<ProjectHistory>lambdaQuery().eq(ProjectHistory::getProjectId, id));

		final Page<ProjectHistory> Page = new Page<>();
		Page.setRecords(iPage.getRecords());
		Page.setTotal(iPage.getTotal());
		return Page;
	}

}
