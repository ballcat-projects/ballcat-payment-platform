package live.lingting.web.controller;

import com.hccake.ballcat.common.model.domain.PageResult;
import com.hccake.ballcat.common.model.result.R;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import live.lingting.Page;
import live.lingting.entity.Project;
import live.lingting.entity.ProjectHistory;
import live.lingting.service.ProjectHistoryService;
import live.lingting.service.ProjectService;

/**
 * @author lingting 2021/6/4 16:19
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("project")
public class ProjectController {

	private final ProjectService service;

	private final ProjectHistoryService historyService;

	@GetMapping
	@PreAuthorize("@per.hasPermission('project:read')")
	public R<PageResult<Project>> list(Page<Project> page, Project project) {
		return R.ok(service.list(page, project));
	}

	@PostMapping
	@PreAuthorize("@per.hasPermission('project:add')")
	public R<?> create(@Validated @RequestBody Project project) {
		service.create(project);
		return R.ok();
	}

	@PatchMapping("reset/api/{id}")
	@PreAuthorize("@per.hasPermission('project:edit')")
	public R<?> resetApi(@PathVariable Integer id) {
		service.resetApi(id);
		return R.ok();
	}

	@PatchMapping("disabled/{id}/{disabled}")
	@PreAuthorize("@per.hasPermission('project:edit')")
	public R<?> disabled(@PathVariable Integer id, @PathVariable Boolean disabled) {
		service.disabled(id, disabled);
		return R.ok();
	}

	@GetMapping("history/{id}")
	@PreAuthorize("@per.hasPermission('project:read')")
	public R<PageResult<ProjectHistory>> history(Page<ProjectHistory> page, @PathVariable Integer id) {
		return R.ok(historyService.listByProject(page, id));
	}

}
