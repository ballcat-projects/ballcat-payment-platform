package live.lingting.service.impl;

import com.hccake.ballcat.common.model.domain.PageResult;
import com.hccake.extend.mybatis.plus.service.impl.ExtendServiceImpl;
import org.springframework.stereotype.Service;
import live.lingting.Page;
import live.lingting.entity.ProjectHistory;
import live.lingting.mapper.ProjectHistoryMapper;
import live.lingting.service.ProjectHistoryService;

/**
 * @author lingting 2021/6/4 13:39
 */
@Service
public class ProjectHistoryServiceImpl extends ExtendServiceImpl<ProjectHistoryMapper, ProjectHistory>
		implements ProjectHistoryService {

	@Override
	public PageResult<ProjectHistory> listByProject(Page<ProjectHistory> page, Integer id) {
		return baseMapper.listByProject(page, id);
	}

}
