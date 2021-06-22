package live.lingting.util;

import java.time.LocalDateTime;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import lombok.experimental.UtilityClass;

/**
 * @author lingting 2021/6/15 9:49
 */
@UtilityClass
public class NotifyUtils {

	/**
	 * 记录 与次数对应的通知时间, 单位: 分组
	 */
	static final Integer[] TIMES;

	static ThreadPoolExecutor executor;

	static {
		TIMES = new Integer[] { 10, 20, 30, 60, 120, 180, 360, 720 };

		BlockingQueue<Runnable> queue = new LinkedBlockingQueue<>();

		final ThreadFactory factory = new ThreadFactory() {
			final AtomicInteger threadNumber = new AtomicInteger(1);

			@Override
			public Thread newThread(Runnable runnable) {
				final Thread thread = new Thread(null, runnable, "notify-" + threadNumber.incrementAndGet(), 0);
				if (thread.isDaemon()) {
					thread.setDaemon(false);

				}
				if (thread.getPriority() != Thread.NORM_PRIORITY) {
					thread.setPriority(Thread.NORM_PRIORITY);
				}
				return thread;
			}
		};
		executor = new ThreadPoolExecutor(100, Integer.MAX_VALUE, 2, TimeUnit.HOURS, queue, factory);
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

	public static void run(Runnable runnable) {
		executor.execute(runnable);
	}

}
