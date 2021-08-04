package live.lingting.payment.handler.thread;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import live.lingting.payment.biz.service.NotifyService;
import live.lingting.payment.biz.service.PayService;
import live.lingting.payment.entity.Pay;

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
