package live.lingting.service;

import com.hccake.extend.mybatis.plus.service.ExtendService;
import java.util.List;
import live.lingting.entity.Notify;
import live.lingting.entity.NotifyLog;
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

	/**
	 * 获取通知中数据
	 * @param size 数据量
	 * @return java.util.List<live.lingting.entity.Notify>
	 * @author lingting 2021-06-15 09:58
	 */
	List<Notify> listNotifying(Integer size);

	/**
	 * 上锁
	 * @param notify 通知
	 * @return boolean
	 * @author lingting 2021-06-15 10:47
	 */
	boolean lock(Notify notify);

	/**
	 * 解锁
	 * @param notify 通知
	 * @return boolean
	 * @author lingting 2021-06-15 10:47
	 */
	boolean unlock(Notify notify);

	/**
	 * 通知完成
	 *
	 * @param notify 通知
	 * @param nl 通知日志
	 * @author lingting 2021-06-15 11:20
	 */
	void notifyComplete(Notify notify, NotifyLog nl);

}
