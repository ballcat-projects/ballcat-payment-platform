package live.lingting.payment.biz.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import live.lingting.payment.biz.mapper.NotifyMapper;
import live.lingting.payment.biz.service.NotifyService;
import live.lingting.payment.biz.util.NotifyUtils;
import live.lingting.payment.entity.Notify;
import live.lingting.payment.entity.Pay;
import live.lingting.payment.rate.Rate;
import live.lingting.payment.sdk.enums.NotifyStatus;
import live.lingting.payment.sdk.enums.PayStatus;

/**
 * @author lingting 2021/6/10 16:33
 */
@Service
@RequiredArgsConstructor
public class NotifyServiceImpl extends ServiceImpl<NotifyMapper, Notify> implements NotifyService {

	private final PayServiceImpl payService;

	private final Rate rate;

	@Override
	@Transactional(rollbackFor = Exception.class)
	public boolean create(Pay pay) {
		if (payService.notifying(pay)) {
			final BigDecimal decimal = PayStatus.SUCCESS.equals(pay.getStatus()) ? rate.get(pay.getCurrency()) : null;

			final Notify notify = new Notify().setNotifyUrl(pay.getNotifyUrl()).setCount(0)
					.setNextTime(NotifyUtils.generateNextTime(0)).setProjectId(pay.getProjectId())
					.setTradeNo(pay.getTradeNo()).setStatus(NotifyStatus.WAIT).setRate(decimal);
			if (save(notify)) {
				// 创建成功. 立刻发起一次通知请求
				NotifyUtils.run(notify);
				return true;
			}
			return false;
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
