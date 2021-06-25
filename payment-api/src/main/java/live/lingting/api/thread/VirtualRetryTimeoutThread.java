package live.lingting.api.thread;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import live.lingting.virtual.VirtualManager;
import live.lingting.entity.Pay;
import live.lingting.service.PayService;

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
