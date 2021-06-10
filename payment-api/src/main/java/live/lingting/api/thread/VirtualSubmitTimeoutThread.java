package live.lingting.api.thread;

import cn.hutool.core.thread.ThreadUtil;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.InitializingBean;
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
public class VirtualSubmitTimeoutThread extends Thread implements InitializingBean {

	/**
	 * 超时时间, 单位: 分钟
	 */
	private static final Long TIMEOUT = TimeUnit.HOURS.toMinutes(2);

	private final VirtualManager manager;

	private final PayService service;

	@Override
	public void run() {
		while (!isInterrupted()) {
			final List<Pay> list = service.listVirtualTimeout(getMaxTime());
			for (Pay pay : list) {
				manager.fail(pay, "超时未提交!", LocalDateTime.now());
			}

			ThreadUtil.sleep(TimeUnit.MINUTES.toMillis(1));
		}
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

	@Override
	public void afterPropertiesSet() throws Exception {
		setName("virtual-submit-timeout");
		this.start();
	}

}
