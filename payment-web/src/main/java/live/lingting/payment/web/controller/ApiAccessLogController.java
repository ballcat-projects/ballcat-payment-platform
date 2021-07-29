package live.lingting.payment.web.controller;

import com.hccake.ballcat.common.model.domain.PageResult;
import com.hccake.ballcat.common.model.result.R;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import live.lingting.payment.Page;
import live.lingting.payment.entity.ApiAccessLog;
import live.lingting.payment.biz.service.ApiAccessLogService;

/**
 * @author lingting 2021/6/25 20:20
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("api/access/log")
public class ApiAccessLogController {

	private final ApiAccessLogService service;

	@GetMapping
	@PreAuthorize("@per.hasPermission('api:access:log:read')")
	public R<PageResult<ApiAccessLog>> list(Page<ApiAccessLog> page, ApiAccessLog qo) {
		return R.ok(service.list(page, qo));
	}

}
