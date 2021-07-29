package live.lingting.payment.api.thread;

import cn.hutool.core.thread.ThreadUtil;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;

/**
 * @author lingting 2021/6/10 17:23
 */
public abstract class AbstractThread<E> extends Thread implements InitializingBean {

	@Override
	public void run() {
		final Logger log = LoggerFactory.getLogger(this.getClass());
		while (!isInterrupted()) {
			if (isContinue()) {
				List<E> data;

				try {
					data = listData();
				}
				catch (Exception de) {
					log.error("获取数据出错!", de);
					data = Collections.emptyList();
				}

				handlerList(data);
			}

			ThreadUtil.sleep(getSleepTime());
		}
	}

	/**
	 * 休眠时长, 单位: 分钟
	 * @author lingting 2021-06-10 17:27
	 */
	public Long getSleepTime() {
		return TimeUnit.MINUTES.toMillis(1);
	}

	/**
	 * 获取数据
	 * @return java.util.List<E>
	 * @author lingting 2021-06-10 17:25
	 */
	public abstract List<E> listData();

	/**
	 * 处理所有数据
	 * @author lingting 2021-06-21 19:00
	 */
	protected void handlerList(List<E> data) {
		final Logger log = LoggerFactory.getLogger(this.getClass());
		for (E e : data) {
			if (isInterrupted() || !isContinue()) {
				break;
			}
			try {
				handler(e);
			}
			catch (Exception he) {
				log.error("处理数据出错!", he);
			}
		}
	}

	/**
	 * 处理数据
	 * @param e 数据
	 * @author lingting 2021-06-10 17:25
	 */
	public abstract void handler(E e);

	/**
	 * 是否继续运行
	 * @author lingting 2021-06-15 10:07
	 */
	public boolean isContinue() {
		return true;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		setName(getClass().getSimpleName());
		if (!isAlive()) {
			start();
		}
	}

}
