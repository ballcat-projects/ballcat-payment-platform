package live.lingting.payment.biz.service.impl;

import live.lingting.payment.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import live.lingting.payment.Page;
import live.lingting.payment.biz.mapper.ProjectHistoryMapper;
import live.lingting.payment.biz.service.ProjectHistoryService;
import live.lingting.payment.entity.ProjectHistory;

/**
 * @author lingting 2021/6/4 13:39
 */
@Service
public class ProjectHistoryServiceImpl extends ServiceImpl<ProjectHistoryMapper, ProjectHistory>
		implements ProjectHistoryService {

	@Override
	public Page<ProjectHistory> listByProject(Page<ProjectHistory> page, Integer id) {
		return baseMapper.listByProject(page, id);
	}

}
