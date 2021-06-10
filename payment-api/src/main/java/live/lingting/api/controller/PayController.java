package live.lingting.api.controller;

import com.hccake.ballcat.common.model.result.R;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import live.lingting.api.util.SecurityUtils;
import live.lingting.sdk.exception.MixException;
import live.lingting.sdk.model.MixVirtualPayModel;
import live.lingting.sdk.model.MixVirtualRetryModel;
import live.lingting.sdk.model.MixVirtualSubmitModel;
import live.lingting.sdk.response.MixVirtualPayResponse;
import live.lingting.api.manager.VirtualManager;

/**
 * @author lingting 2021/6/7 17:05
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("pay")
public class PayController {

	private final VirtualManager manager;

	@SneakyThrows
	@PostMapping("virtual")
	public R<MixVirtualPayResponse.Data> virtual(@RequestBody MixVirtualPayModel model) {
		model.valid();
		return R.ok(manager.pay(model, SecurityUtils.getProject()));
	}

	@PostMapping("virtual/submit")
	public R<?> virtualSubmit(@RequestBody MixVirtualSubmitModel model) throws MixException {
		model.valid();
		manager.submit(model);
		return R.ok();
	}

	@PostMapping("virtual/retry")
	public R<?> virtualRetry(@RequestBody MixVirtualRetryModel model) throws MixException {
		model.valid();
		manager.retry(model);
		return R.ok();
	}

}
