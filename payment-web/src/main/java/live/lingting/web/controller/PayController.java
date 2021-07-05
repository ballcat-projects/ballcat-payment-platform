package live.lingting.web.controller;

import com.hccake.ballcat.common.model.domain.PageResult;
import com.hccake.ballcat.common.model.result.R;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import live.lingting.Page;
import live.lingting.entity.NotifyLog;
import live.lingting.entity.Pay;
import live.lingting.service.NotifyLogService;
import live.lingting.service.PayService;

/**
 * @author lingting 2021/6/7 10:54
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("pay")
public class PayController {

	private final PayService service;

	private final NotifyLogService logService;

	@GetMapping
	@PreAuthorize("@per.hasPermission('pay:read')")
	public R<PageResult<Pay>> list(Page<Pay> page, Pay pay) {
		return R.ok(service.list(page, pay));
	}

	@GetMapping("logs/{tradeNo}")
	@PreAuthorize("@per.hasPermission('pay:read')")
	public R<PageResult<NotifyLog>> logs(Page<NotifyLog> page, @PathVariable String tradeNo) {
		return R.ok(logService.listByTradeNo(page, tradeNo));
	}

	@PostMapping("forcibly/retry")
	@PreAuthorize("@per.hasPermission('pay:forcibly:retry')")
	public R<?> forciblyRetry(@RequestBody Pay pay) {
		service.forciblyRetry(pay.getTradeNo(), null);
		return R.ok();
	}

	@PostMapping("forcibly/fail")
	@PreAuthorize("@per.hasPermission('pay:forcibly:fail')")
	public R<?> forciblyFail(@RequestBody Pay pay) {
		service.forciblyFail(pay.getTradeNo(), null);
		return R.ok();
	}

}
