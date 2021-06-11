package live.lingting.service.impl;

import com.hccake.extend.mybatis.plus.service.impl.ExtendServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import live.lingting.entity.Notify;
import live.lingting.entity.Pay;
import live.lingting.mapper.NotifyMapper;
import live.lingting.service.NotifyService;

/**
 * @author lingting 2021/6/10 16:33
 */
@Service
@RequiredArgsConstructor
public class NotifyServiceImpl extends ExtendServiceImpl<NotifyMapper, Notify> implements NotifyService {
	private final PayServiceImpl payService;

	@Override
	public boolean create(Pay pay) {

		return false;
	}

}
