package live.lingting.payment.biz.service;

import com.baomidou.mybatisplus.extension.service.IService;
import java.time.LocalDateTime;
import java.util.List;
import live.lingting.payment.entity.Notify;
import live.lingting.payment.entity.Pay;
import live.lingting.payment.sdk.enums.NotifyStatus;

/**
 * @author lingting 2021/6/10 16:33
 */
public interface NotifyService extends IService<Notify> {

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
	 * @return java.util.List<live.lingting.payment.entity.Notify>
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
	 * @param notify 通知
	 * @param status 新状态
	 * @param nextTime 下一次通知时间
	 * @author lingting 2021-06-15 22:23
	 */
	void notifyComplete(Notify notify, NotifyStatus status, LocalDateTime nextTime);

}
