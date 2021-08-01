package live.lingting.payment.biz.service.impl;

import com.hccake.ballcat.common.core.exception.BusinessException;
import com.hccake.ballcat.common.model.domain.PageResult;
import com.hccake.extend.mybatis.plus.service.impl.ExtendServiceImpl;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import live.lingting.payment.Page;
import live.lingting.payment.biz.mapper.ProjectMapper;
import live.lingting.payment.biz.service.ProjectHistoryService;
import live.lingting.payment.biz.service.ProjectService;
import live.lingting.payment.entity.Project;
import live.lingting.payment.entity.ProjectHistory;
import live.lingting.payment.enums.ProjectMode;
import live.lingting.payment.enums.ProjectScope;
import live.lingting.payment.enums.ResponseCode;
import live.lingting.payment.util.ApiUtils;

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

	@Override
	public void scope(List<Integer> ids, List<ProjectScope> scopes) {
		baseMapper.scope(ids, scopes);
	}

	private void update(ProjectHistory history, Project project) {
		updateById(project);
		historyService.save(history);
	}

}
