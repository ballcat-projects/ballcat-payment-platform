package live.lingting.service.impl;

import com.hccake.ballcat.common.core.exception.BusinessException;
import com.hccake.ballcat.common.model.domain.PageResult;
import com.hccake.extend.mybatis.plus.service.impl.ExtendServiceImpl;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import live.lingting.Page;
import live.lingting.entity.Project;
import live.lingting.entity.ProjectHistory;
import live.lingting.enums.ProjectMode;
import live.lingting.enums.ResponseCode;
import live.lingting.mapper.ProjectMapper;
import live.lingting.service.ProjectHistoryService;
import live.lingting.service.ProjectService;
import live.lingting.util.ApiUtils;

/**
 * @author lingting 2021/6/4 13:38
 */
@Service
@RequiredArgsConstructor
public class ProjectServiceImpl extends ExtendServiceImpl<ProjectMapper, Project> implements ProjectService {

	private final ProjectHistoryService historyService;

	@Override
	public PageResult<Project> list(Page<Project> page, Project project) {
		return baseMapper.list(page, project);
	}

	@Override
	public void create(Project project) {
		save(ApiUtils.fillApi(project));
	}

	@Override
	public void resetApi(Integer id) {
		final Project project = getById(id);
		if (project == null) {
			throw new BusinessException(ResponseCode.PROJECT_NOT_FOUND);
		}
		ProjectHistory history = ProjectHistory.of(project);
		update(history, ApiUtils.fillApi(project));
	}

	@Override
	public void disabled(Integer id, Boolean disabled) {
		final Project project = getById(id);
		if (project == null) {
			throw new BusinessException(ResponseCode.PROJECT_NOT_FOUND);
		}
		ProjectHistory history = ProjectHistory.of(project);
		update(history, project.setDisabled(disabled));
	}

	@Override
	public void mode(List<Integer> ids, ProjectMode mode) {
		baseMapper.mode(ids, mode);
	}

	@Override
	public Project getByApiKey(String key) {
		return baseMapper.selectOne(baseMapper.getWrapper(new Project().setApiKey(key)));
	}

	private void update(ProjectHistory history, Project project) {
		updateById(project);
		historyService.save(history);
	}

}
