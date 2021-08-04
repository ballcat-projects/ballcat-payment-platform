package live.lingting.payment.api.controller;

import com.hccake.ballcat.common.model.result.R;
import java.math.BigDecimal;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import live.lingting.payment.biz.rate.Rate;
import live.lingting.payment.biz.service.PayService;
import live.lingting.payment.entity.Pay;
import live.lingting.payment.exception.PaymentException;
import live.lingting.payment.sdk.exception.MixException;
import live.lingting.payment.sdk.model.MixForciblyFailModel;
import live.lingting.payment.sdk.model.MixForciblyRetryModel;
import live.lingting.payment.sdk.model.MixQueryModel;
import live.lingting.payment.sdk.model.MixRateModel;

/**
 * @author lingting 2021/6/10 12:55
 */
@RestController
@RequestMapping
@RequiredArgsConstructor
public class CommonController {

	private final PayService service;

	private final Rate rate;

	@PostMapping("query")
	public R<Pay> query(@RequestBody MixQueryModel model) throws MixException, PaymentException {
		model.valid();
		return R.ok(service.getByNo(model.getTradeNo(), model.getProjectTradeNo()));
	}

	@PostMapping("rate")
	public R<String> rate(@RequestBody MixRateModel model) throws MixException {
		model.valid();
		final BigDecimal decimal = rate.get(model.getCurrency());
		return R.ok(decimal == null ? null : decimal.toPlainString());
	}

	@PostMapping("forcibly/retry")
	public R<?> forciblyRetry(@RequestBody MixForciblyRetryModel model) throws MixException, PaymentException {
		model.valid();
		service.forciblyRetry(model.getTradeNo(), model.getProjectTradeNo());
		return R.ok();
	}

	@SneakyThrows
	@PostMapping("forcibly/fail")
	public R<?> forciblyFail(@RequestBody MixForciblyFailModel model) throws MixException {
		model.valid();
		service.forciblyFail(model.getTradeNo(), model.getProjectTradeNo());
		return R.ok();
	}

}
