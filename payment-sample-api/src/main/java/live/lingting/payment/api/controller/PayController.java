package live.lingting.payment.api.controller;

import com.alipay.api.AlipayApiException;
import com.hccake.ballcat.common.core.exception.BusinessException;
import com.hccake.ballcat.common.model.result.R;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import live.lingting.payment.api.util.SecurityUtils;
import live.lingting.payment.biz.real.RealManager;
import live.lingting.payment.biz.virtual.VirtualManager;
import live.lingting.payment.entity.Project;
import live.lingting.payment.enums.ProjectScope;
import live.lingting.payment.enums.ResponseCode;
import live.lingting.payment.exception.PaymentException;
import live.lingting.payment.sdk.exception.MixException;
import live.lingting.payment.sdk.model.MixRealPayModel;
import live.lingting.payment.sdk.model.MixVirtualPayModel;
import live.lingting.payment.sdk.model.MixVirtualRetryModel;
import live.lingting.payment.sdk.model.MixVirtualSubmitModel;
import live.lingting.payment.sdk.response.MixRealPayResponse;
import live.lingting.payment.sdk.response.MixVirtualPayResponse;
import live.lingting.payment.sdk.response.MixVirtualRetryResponse;

/**
 * @author lingting 2021/6/7 17:05
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("pay")
public class PayController {

	private final VirtualManager virtualManager;

	private final RealManager realManager;

	@SneakyThrows
	@PostMapping
	public R<MixRealPayResponse.Data> real(@RequestBody MixRealPayModel model) throws MixException, AlipayApiException {
		model.valid();
		final Project project = SecurityUtils.getProject();
		if (project == null || !project.getScope().contains(ProjectScope.get(model))) {
			ResponseCode code = ResponseCode.PROHIBIT_PAYMENT_METHOD;
			throw new BusinessException(code.getCode(), code.getMessage());
		}
		return R.ok(realManager.pay(project, model));
	}

	@PostMapping("virtual")
	public R<MixVirtualPayResponse.Data> virtual(@RequestBody MixVirtualPayModel model)
			throws MixException, PaymentException {
		model.valid();
		final Project project = SecurityUtils.getProject();
		if (project == null || !project.getScope().contains(ProjectScope.get(model))) {
			ResponseCode code = ResponseCode.PROHIBIT_PAYMENT_METHOD;
			throw new BusinessException(code.getCode(), code.getMessage());
		}
		return R.ok(virtualManager.pay(model, project));
	}

	@PostMapping("virtual/submit")
	public R<?> virtualSubmit(@RequestBody MixVirtualSubmitModel model) throws MixException, PaymentException {
		model.valid();
		virtualManager.submit(model);
		return R.ok();
	}

	@PostMapping("virtual/retry")
	public R<MixVirtualRetryResponse.Data> virtualRetry(@RequestBody MixVirtualRetryModel model)
			throws MixException, PaymentException {
		model.valid();
		return R.ok(virtualManager.retry(model));
	}

}
