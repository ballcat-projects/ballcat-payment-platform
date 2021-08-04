package live.lingting.payment.sample.service.impl;

import com.hccake.extend.mybatis.plus.service.impl.ExtendServiceImpl;
import org.springframework.stereotype.Service;
import live.lingting.payment.Page;
import live.lingting.payment.sample.entity.ApiAccessLog;
import live.lingting.payment.sample.mapper.ApiAccessLogMapper;
import live.lingting.payment.sample.service.ApiAccessLogService;

/**
 * @author lingting 2021/6/25 20:14
 */
@Service
public class ApiAccessLogServiceImpl extends ExtendServiceImpl<ApiAccessLogMapper, ApiAccessLog>
		implements ApiAccessLogService {

	@Override
	public Page<ApiAccessLog> list(Page<ApiAccessLog> page, ApiAccessLog qo) {
		return baseMapper.list(page, qo);
	}

}
