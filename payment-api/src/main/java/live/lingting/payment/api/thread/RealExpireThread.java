package live.lingting.payment.api.thread;

import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import live.lingting.payment.biz.config.PayConfig;
import live.lingting.payment.biz.service.PayService;
import live.lingting.payment.entity.Pay;

/**
 * @author lingting 2021/7/14 17:11
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class RealExpireThread extends AbstractThread<Pay> {

	private final PayService service;

	private final PayConfig config;

	@Override
	public List<Pay> listData() {
		return service.listRealExpire(getMaxTime());
	}

	@Override
	public void handler(Pay pay) {
		service.fail(pay, "未在" + config.getRealExpireTimeout() + "分钟内付款!", null);
	}

	/**
	 * 获取过去真实货币支付信息的最大创建时间
	 *
	 * @author lingting 2021-06-09 14:06
	 */
	public LocalDateTime getMaxTime() {
		return LocalDateTime.now().minusMinutes(config.getRealExpireTimeout());
	}

}
