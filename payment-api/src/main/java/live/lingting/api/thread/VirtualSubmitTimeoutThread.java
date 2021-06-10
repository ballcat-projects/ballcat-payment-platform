package live.lingting.api.thread;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import live.lingting.api.manager.VirtualManager;
import live.lingting.entity.Pay;
import live.lingting.service.PayService;
import live.lingting.util.SpringUtils;

/**
 * @author lingting 2021/6/9 13:58
 */
@Component
@RequiredArgsConstructor
public class VirtualSubmitTimeoutThread extends AbstractThread<Pay> {

	/**
	 * 超时时间, 单位: 分钟
	 */
	private static final Long TIMEOUT = TimeUnit.HOURS.toMinutes(2);

	private final VirtualManager manager;

	private final PayService service;

	@Override
	public List<Pay> listData() {
		return service.listVirtualTimeout(getMaxTime());
	}

	@Override
	public void handler(Pay pay) {
		manager.fail(pay, "超时未提交!", LocalDateTime.now());
	}

	/**
	 * 获取已超时支付信息的最大创建时间
	 * @author lingting 2021-06-09 14:06
	 */
	public LocalDateTime getMaxTime() {
		if (!SpringUtils.isProd()) {
			// 测试服, 一分钟未提交hash就超时
			return LocalDateTime.now().minusMinutes(1);
		}
		return LocalDateTime.now().minusMinutes(TIMEOUT);
	}

}
