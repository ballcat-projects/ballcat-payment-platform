package live.lingting.api.thread;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;
import live.lingting.service.NotifyLogService;
import live.lingting.service.NotifyService;
import live.lingting.service.PayService;

/**
 * @author lingting 2021/6/10 16:54
 */
@Component
@RequiredArgsConstructor
public class NotifyThread implements InitializingBean {

	private final PayService payService;

	private final NotifyService service;

	private final NotifyLogService logService;

	@Override
	public void afterPropertiesSet() throws Exception {

	}

}
