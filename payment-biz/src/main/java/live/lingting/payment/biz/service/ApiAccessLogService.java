package live.lingting.payment.biz.service;

import com.hccake.ballcat.common.model.domain.PageResult;
import com.hccake.extend.mybatis.plus.service.ExtendService;
import live.lingting.payment.Page;
import live.lingting.payment.entity.ApiAccessLog;

/**
 * @author lingting 2021/6/25 20:14
 */
public interface ApiAccessLogService extends ExtendService<ApiAccessLog> {

	/**
	 * 查询
	 * @param page 分页
	 * @param qo 条件
	 * @return com.hccake.ballcat.common.model.domain.PageResult<live.lingting.entity.ApiAccessLog>
	 * @author lingting 2021-06-25 20:23
	 */
	PageResult<ApiAccessLog> list(Page<ApiAccessLog> page, ApiAccessLog qo);

}
