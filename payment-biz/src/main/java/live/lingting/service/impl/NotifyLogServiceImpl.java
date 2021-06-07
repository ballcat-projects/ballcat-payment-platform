package live.lingting.service.impl;

import com.hccake.ballcat.common.model.domain.PageResult;
import com.hccake.extend.mybatis.plus.service.impl.ExtendServiceImpl;
import org.springframework.stereotype.Service;
import live.lingting.Page;
import live.lingting.entity.NotifyLog;
import live.lingting.mapper.NotifyLogMapper;
import live.lingting.service.NotifyLogService;

/**
 * @author lingting 2021/6/4 13:42
 */
@Service
public class NotifyLogServiceImpl extends ExtendServiceImpl<NotifyLogMapper, NotifyLog> implements NotifyLogService {

	@Override
	public PageResult<NotifyLog> list(Page<NotifyLog> page, NotifyLog log) {
		return baseMapper.list(page, log);
	}

	@Override
	public PageResult<NotifyLog> listByTradeNo(Page<NotifyLog> page, String tradeNo) {
		return list(page, new NotifyLog().setTradeNo(tradeNo));
	}

}
