package live.lingting.payment.handler.thread;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;
import live.lingting.payment.biz.service.NotifyService;
import live.lingting.payment.biz.util.NotifyUtils;
import live.lingting.payment.entity.Notify;

/**
 * @author lingting 2021/6/10 16:54
 */
@Component
@RequiredArgsConstructor
@DependsOn("objectMapper")
public class NotifyThread extends AbstractThread<Notify> {

	/**
	 * 获取数据大小
	 */
	private static final Integer SIZE = 100;

	private final NotifyService service;

	@Override
	public List<Notify> listData() {
		return service.listNotifying(SIZE);
	}

	@Override
	public void handler(Notify notify) {
		NotifyUtils.run(notify);
	}

}
