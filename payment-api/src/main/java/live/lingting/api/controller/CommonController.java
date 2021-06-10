package live.lingting.api.controller;

import com.hccake.ballcat.common.model.result.R;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import live.lingting.Rate;
import live.lingting.entity.Pay;
import live.lingting.sdk.exception.MixException;
import live.lingting.sdk.model.MixQueryModel;
import live.lingting.sdk.model.MixRateModel;
import live.lingting.service.PayService;

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
	public R<Pay> query(@RequestBody MixQueryModel model) throws MixException {
		model.valid();
		return R.ok(service.getByNo(model.getTradeNo(), model.getProjectTradeNo()));
	}

	@PostMapping("rate")
	public R<String> rate(@RequestBody MixRateModel model) throws MixException {
		model.valid();
		return R.ok(rate.get(model.getCurrency()).toPlainString());
	}

}
