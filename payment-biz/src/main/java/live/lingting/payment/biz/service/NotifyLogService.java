package live.lingting.payment.biz.service;

import com.baomidou.mybatisplus.extension.service.IService;
import live.lingting.payment.Page;
import live.lingting.payment.entity.NotifyLog;

/**
 * @author lingting 2021/6/4 13:42
 */
public interface NotifyLogService extends IService<NotifyLog> {

	/**
	 * 查询
	 * @param page 分页
	 * @param log 条件
	 * @return live.lingting.payment.Page<live.lingting.payment.entity.NotifyLog>
	 * @author lingting 2021-06-07 14:07
	 */
	Page<NotifyLog> list(Page<NotifyLog> page, NotifyLog log);

	/**
	 * 获取指定支付信息的通知记录
	 * @param page 分页
	 * @param tradeNo 交易号
	 * @return live.lingting.payment.Page<live.lingting.payment.entity.NotifyLog>
	 * @author lingting 2021-06-07 14:05
	 */
	Page<NotifyLog> listByTradeNo(Page<NotifyLog> page, String tradeNo);

}
