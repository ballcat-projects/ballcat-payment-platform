package live.lingting.api.controller;

import com.alipay.api.AlipayApiException;
import com.hccake.ballcat.common.model.result.R;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import live.lingting.api.util.SecurityUtils;
import live.lingting.real.RealManager;
import live.lingting.sdk.exception.MixException;
import live.lingting.sdk.model.MixRealPayModel;
import live.lingting.sdk.model.MixVirtualPayModel;
import live.lingting.sdk.model.MixVirtualRetryModel;
import live.lingting.sdk.model.MixVirtualSubmitModel;
import live.lingting.sdk.response.MixRealPayResponse;
import live.lingting.sdk.response.MixVirtualPayResponse;
import live.lingting.sdk.response.MixVirtualRetryResponse;
import live.lingting.virtual.VirtualManager;

/**
 * @author lingting 2021/6/7 17:05
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("pay")
public class PayController {

	private final VirtualManager virtualManager;

	private final RealManager realManager;

	@PostMapping
	public R<MixRealPayResponse.Data> real(@RequestBody MixRealPayModel model) throws MixException, AlipayApiException {
		model.valid();
		return R.ok(realManager.pay(SecurityUtils.getProject(), model));
	}

	@PostMapping("virtual")
	public R<MixVirtualPayResponse.Data> virtual(@RequestBody MixVirtualPayModel model) throws MixException {
		model.valid();
		return R.ok(virtualManager.pay(model, SecurityUtils.getProject()));
	}

	@PostMapping("virtual/submit")
	public R<?> virtualSubmit(@RequestBody MixVirtualSubmitModel model) throws MixException {
		model.valid();
		virtualManager.submit(model);
		return R.ok();
	}

	@PostMapping("virtual/retry")
	public R<MixVirtualRetryResponse.Data> virtualRetry(@RequestBody MixVirtualRetryModel model) throws MixException {
		model.valid();
		return R.ok(virtualManager.retry(model));
	}

}
