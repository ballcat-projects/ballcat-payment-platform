package live.lingting.payment.biz.service.impl;

import com.hccake.ballcat.common.model.domain.PageResult;
import com.hccake.extend.mybatis.plus.service.impl.ExtendServiceImpl;
import org.springframework.stereotype.Service;
import live.lingting.payment.Page;
import live.lingting.payment.biz.mapper.ApiAccessLogMapper;
import live.lingting.payment.biz.service.ApiAccessLogService;
import live.lingting.payment.entity.ApiAccessLog;

/**
 * @author lingting 2021/6/25 20:14
 */
@Service
public class ApiAccessLogServiceImpl extends ExtendServiceImpl<ApiAccessLogMapper, ApiAccessLog>
		implements ApiAccessLogService {

	@Override
	public PageResult<ApiAccessLog> list(Page<ApiAccessLog> page, ApiAccessLog qo) {
		return baseMapper.list(page, qo);
	}

}
