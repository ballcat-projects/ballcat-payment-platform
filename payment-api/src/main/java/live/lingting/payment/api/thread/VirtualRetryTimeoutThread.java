package live.lingting.payment.api.thread;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import live.lingting.payment.entity.Pay;
import live.lingting.payment.biz.service.PayService;
import live.lingting.payment.biz.virtual.VirtualManager;

/**
 * @author lingting 2021/6/9 13:58
 */
@Component
@RequiredArgsConstructor
public class VirtualRetryTimeoutThread extends AbstractThread<Pay> {

	private final VirtualManager manager;

	private final PayService service;

	@Override
	public List<Pay> listData() {
		return service.listVirtualRetryTimeout();
	}

	@Override
	public void handler(Pay pay) {
		manager.fail(pay, null, null);
		service.notifyWait(pay);
	}

}
