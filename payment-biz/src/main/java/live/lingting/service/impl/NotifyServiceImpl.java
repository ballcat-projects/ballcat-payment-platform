package live.lingting.service.impl;

import com.hccake.extend.mybatis.plus.service.impl.ExtendServiceImpl;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import live.lingting.entity.Notify;
import live.lingting.entity.Pay;
import live.lingting.mapper.NotifyMapper;
import live.lingting.rate.Rate;
import live.lingting.sdk.enums.NotifyStatus;
import live.lingting.service.NotifyService;
import live.lingting.util.NotifyUtils;

/**
 * @author lingting 2021/6/10 16:33
 */
@Service
@RequiredArgsConstructor
public class NotifyServiceImpl extends ExtendServiceImpl<NotifyMapper, Notify> implements NotifyService {

	private final PayServiceImpl payService;

	private final Rate rate;

	@Override
	public boolean create(Pay pay) {
		if (payService.notifying(pay)) {
			final Notify notify = new Notify().setNotifyUrl(pay.getNotifyUrl()).setCount(0)
					.setNextTime(NotifyUtils.generateNextTime(0)).setProjectId(pay.getProjectId())
					.setTradeNo(pay.getTradeNo()).setStatus(NotifyStatus.WAIT).setRate(rate.get(pay.getCurrency()));
			return save(notify);
		}
		return false;
	}

	@Override
	public List<Notify> listNotifying(Integer size) {
		return baseMapper.listNotifying(size);
	}

	@Override
	public boolean lock(Notify notify) {
		return baseMapper.lock(notify);
	}

	@Override
	public boolean unlock(Notify notify) {
		return baseMapper.unlock(notify);
	}

	@Override
	public void notifyComplete(Notify notify, NotifyStatus status, LocalDateTime nextTime) {
		baseMapper.notifyComplete(notify, status, nextTime);
	}

}
