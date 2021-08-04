package live.lingting.payment.api.log;

import com.hccake.ballcat.common.core.thread.AbstractBlockingQueueThread;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import live.lingting.payment.sample.entity.ApiAccessLog;
import live.lingting.payment.sample.service.ApiAccessLogService;

/**
 * @author lingting 2021/6/25 15:26
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class LogThread extends AbstractBlockingQueueThread<ApiAccessLog> {

	private final ApiAccessLogService service;

	@Override
	public void process(List<ApiAccessLog> list) throws Exception {
		service.saveBatchSomeColumn(list);
	}

	@Override
	public void error(Throwable throwable, List<ApiAccessLog> list) {
		log.error("线程异常, 数据: {}", list.toString());
	}

}
