package live.lingting.payment.api.thread;

import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import live.lingting.payment.biz.config.PayConfig;
import live.lingting.payment.entity.Pay;
import live.lingting.payment.biz.service.PayService;
import live.lingting.payment.biz.virtual.VirtualManager;

/**
 * @author lingting 2021/6/9 13:58
 */
@Component
@RequiredArgsConstructor
public class VirtualSubmitTimeoutThread extends AbstractThread<Pay> {

	private final VirtualManager manager;

	private final PayService service;

	private final PayConfig config;

	@Override
	public List<Pay> listData() {
		return service.listVirtualTimeout(getMaxTime());
	}

	@Override
	public void handler(Pay pay) {
		manager.fail(pay, "超时未提交!", 0L);
	}

	/**
	 * 获取已超时支付信息的最大创建时间
	 * @author lingting 2021-06-09 14:06
	 */
	public LocalDateTime getMaxTime() {
		return LocalDateTime.now().minusMinutes(config.getVirtualSubmitTimeout());
	}

}
