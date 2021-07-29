package live.lingting.payment.biz.service;

import com.hccake.ballcat.common.model.domain.PageResult;
import com.hccake.extend.mybatis.plus.service.ExtendService;
import live.lingting.payment.Page;
import live.lingting.payment.entity.NotifyLog;

/**
 * @author lingting 2021/6/4 13:42
 */
public interface NotifyLogService extends ExtendService<NotifyLog> {

	/**
	 * 查询
	 * @param page 分页
	 * @param log 条件
	 * @return com.hccake.ballcat.common.model.domain.PageResult<live.lingting.entity.NotifyLog>
	 * @author lingting 2021-06-07 14:07
	 */
	PageResult<NotifyLog> list(Page<NotifyLog> page, NotifyLog log);

	/**
	 * 获取指定支付信息的通知记录
	 * @param page 分页
	 * @param tradeNo 交易号
	 * @return com.hccake.ballcat.common.model.domain.PageResult<live.lingting.entity.NotifyLog>
	 * @author lingting 2021-06-07 14:05
	 */
	PageResult<NotifyLog> listByTradeNo(Page<NotifyLog> page, String tradeNo);

}
