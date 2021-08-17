package live.lingting.payment.sample.service;

import com.hccake.extend.mybatis.plus.service.ExtendService;
import live.lingting.payment.Page;
import live.lingting.payment.sample.entity.ApiAccessLog;

/**
 * @author lingting 2021/6/25 20:14
 */
public interface ApiAccessLogService extends ExtendService<ApiAccessLog> {

	/**
	 * 查询
	 * @param page 分页
	 * @param qo 条件
	 * @return com.hccake.ballcat.common.model.domain.PageResult<live.lingting.payment.entity.ApiAccessLog>
	 * @author lingting 2021-06-25 20:23
	 */
	Page<ApiAccessLog> list(Page<ApiAccessLog> page, ApiAccessLog qo);

}
