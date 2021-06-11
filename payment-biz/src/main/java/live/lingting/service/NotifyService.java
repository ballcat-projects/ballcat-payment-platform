package live.lingting.service;

import com.hccake.extend.mybatis.plus.service.ExtendService;
import live.lingting.entity.Notify;
import live.lingting.entity.Pay;

/**
 * @author lingting 2021/6/10 16:33
 */
public interface NotifyService extends ExtendService<Notify> {

	/**
	 * 添加通知
	  * @param pay 支付信息
	 * @return boolean
	 * @author lingting 2021-06-10 17:18
	 */
	boolean create(Pay pay);

}
