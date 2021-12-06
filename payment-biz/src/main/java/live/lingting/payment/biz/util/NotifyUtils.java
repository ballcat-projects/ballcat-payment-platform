package live.lingting.payment.biz.util;

import java.time.LocalDateTime;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import lombok.Getter;
import lombok.experimental.UtilityClass;
import live.lingting.payment.biz.runnable.NotifyRunnable;
import live.lingting.payment.entity.Notify;

/**
 * @author lingting 2021/6/15 9:49
 */
@UtilityClass
public class NotifyUtils {

	/**
	 * 记录 与次数对应的通知时间, 单位: 分组
	 */
	static final Integer[] TIMES;

	@Getter
	static final ThreadPoolExecutor POOL_EXECUTOR;

	static {
		TIMES = new Integer[] { 10, 20, 30, 60, 120, 180, 360, 720 };

		POOL_EXECUTOR = new ThreadPoolExecutor(
				// 核心线程数大小. 不论是否空闲都存在的线程 - 根据平均充值量来确定
				5,
				// 最大线程数 - 10万个
				100000,
				// 存活时间. 非核心线程数如果空闲指定时间. 就回收
				// 存活时间不宜过长. 避免任务量遇到尖峰情况时. 大量空闲线程占用资源
				10,
				// 存活时间的单位
				TimeUnit.SECONDS,
				// 等待任务存放队列 - 队列最大值
				// 这样配置. 当积压任务数量为 队列最大值 时. 会创建新线程来执行任务. 直到线程总数达到 最大线程数
				// 需要及时处理. 队列设置为1.
				new LinkedBlockingQueue<>(1),
				// 新线程创建工厂 - LinkedBlockingQueue 不支持线程优先级. 所以直接新增线程就可以了
				runnable -> {
					if (runnable instanceof NotifyRunnable) {
						// 线程名展示当前处理通知id
						return new Thread(null, runnable, "Notify-" + ((NotifyRunnable) runnable).getId());
					}
					return null;
				},
				// 拒绝策略 - 放弃.
				// 数据存放在数据库中. 不会丢失. 等待线程池可以处理的会重新拿出来出来
				new ThreadPoolExecutor.AbortPolicy());
	}

	public static LocalDateTime generateNextTime(int count) {
		if (count > TIMES.length) {
			return null;
		}
		final LocalDateTime now = LocalDateTime.now();
		if (count == 0) {
			return now;
		}

		return now.plusMinutes(TIMES[count - 1]);
	}

	public static void run(Notify notify) {
		POOL_EXECUTOR.execute(new NotifyRunnable(notify));
	}

}
