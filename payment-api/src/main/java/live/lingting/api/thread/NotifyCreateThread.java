package live.lingting.api.thread;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import live.lingting.entity.Pay;
import live.lingting.service.NotifyService;
import live.lingting.service.PayService;

/**
 * @author lingting 2021/6/10 16:54
 */
@Component
@RequiredArgsConstructor
public class NotifyCreateThread extends AbstractThread<Pay> {

	private final PayService payService;

	private final NotifyService service;

	@Override
	public List<Pay> listData() {
		return payService.listNotify();
	}

	@Override
	public void handler(Pay pay) {
		service.create(pay);
	}

}
