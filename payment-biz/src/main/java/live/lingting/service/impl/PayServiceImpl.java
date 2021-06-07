package live.lingting.service.impl;

import com.hccake.ballcat.common.model.domain.PageResult;
import com.hccake.extend.mybatis.plus.service.impl.ExtendServiceImpl;
import org.springframework.stereotype.Service;
import live.lingting.Page;
import live.lingting.entity.Pay;
import live.lingting.mapper.PayMapper;
import live.lingting.service.PayService;

/**
 * @author lingting 2021/6/4 13:40
 */
@Service
public class PayServiceImpl extends ExtendServiceImpl<PayMapper, Pay> implements PayService {

	@Override
	public PageResult<Pay> list(Page<Pay> page, Pay pay) {
		return baseMapper.list(page, pay);
	}

}
