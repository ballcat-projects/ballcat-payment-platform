package live.lingting.payment.biz.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import live.lingting.payment.Page;
import live.lingting.payment.biz.mapper.NotifyLogMapper;
import live.lingting.payment.biz.service.NotifyLogService;
import live.lingting.payment.entity.NotifyLog;

/**
 * @author lingting 2021/6/4 13:42
 */
@Service
public class NotifyLogServiceImpl extends ServiceImpl<NotifyLogMapper, NotifyLog> implements NotifyLogService {

	@Override
	public Page<NotifyLog> list(Page<NotifyLog> page, NotifyLog log) {
		return baseMapper.list(page, log);
	}

	@Override
	public Page<NotifyLog> listByTradeNo(Page<NotifyLog> page, String tradeNo) {
		return list(page, new NotifyLog().setTradeNo(tradeNo));
	}

}
