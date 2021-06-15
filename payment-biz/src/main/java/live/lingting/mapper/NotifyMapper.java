package live.lingting.mapper;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import com.hccake.extend.mybatis.plus.conditions.query.LambdaQueryWrapperX;
import com.hccake.extend.mybatis.plus.mapper.ExtendMapper;
import com.hccake.extend.mybatis.plus.toolkit.WrappersX;
import java.time.LocalDateTime;
import java.util.List;
import live.lingting.entity.Notify;
import live.lingting.sdk.enums.NotifyStatus;

/**
 * @author lingting 2021/6/10 16:33
 */
public interface NotifyMapper extends ExtendMapper<Notify> {

	/**
	 * 获取sql
	 * @param notify 条件
	 * @return com.baomidou.mybatisplus.core.conditions.Wrapper<live.lingting.entity.Notify>
	 * @author lingting 2021-06-15 09:59
	 */
	default LambdaQueryWrapperX<Notify> getWrapper(Notify notify) {
		return WrappersX.<Notify>lambdaQueryX()
				// status
				.eqIfPresent(Notify::getStatus, notify.getStatus())
				// 下次通知时间小于等于
				.leIfPresent(Notify::getNextTime, notify.getNextTime());
	}

	/**
	 * 获取通知中数据
	 * @param size 数据量
	 * @return java.util.List<live.lingting.entity.Notify>
	 * @author lingting 2021-06-15 09:58
	 */
	default List<Notify> listNotifying(Integer size) {
		final LambdaQueryWrapperX<Notify> wrapper = getWrapper(
				new Notify().setStatus(NotifyStatus.WAIT).setNextTime(LocalDateTime.now()));
		wrapper.last(String.format(" limit %d ", size));

		return selectList(wrapper);
	}

	/**
	 * 上锁
	 * @param notify 通知
	 * @return boolean
	 * @author lingting 2021-06-15 10:47
	 */
	default boolean lock(Notify notify) {
		final LambdaUpdateWrapper<Notify> wrapper = Wrappers.<Notify>lambdaUpdate()
				// 限定
				.eq(Notify::getId, notify.getId())
				// 限定
				.eq(Notify::getStatus, NotifyStatus.WAIT)
				// 设置
				.set(Notify::getStatus, NotifyStatus.ING);

		return SqlHelper.retBool(update(null, wrapper));
	}

	/**
	 * 解锁
	 * @param notify 通知
	 * @return boolean
	 * @author lingting 2021-06-15 10:47
	 */
	default boolean unlock(Notify notify) {
		final LambdaUpdateWrapper<Notify> wrapper = Wrappers.<Notify>lambdaUpdate()
				// 限定
				.eq(Notify::getId, notify.getId())
				// 限定
				.eq(Notify::getStatus, NotifyStatus.ING)
				// 设置
				.set(Notify::getStatus, NotifyStatus.WAIT);

		return SqlHelper.retBool(update(null, wrapper));
	}

	/**
	 *
	 * 通知完成
	 * @param notify 通知
	 * @param status 新状态
	 * @param nextTime 下一次通知时间
	 * @author lingting 2021-06-15 22:23
	 */
	default void notifyComplete(Notify notify, NotifyStatus status, LocalDateTime nextTime) {
		final LambdaUpdateWrapper<Notify> wrapper = Wrappers.<Notify>lambdaUpdate()
				// 限定
				.eq(Notify::getId, notify.getId())
				// 限定
				.eq(Notify::getStatus, NotifyStatus.ING)
				// 设置
				.set(Notify::getStatus, status)
				// 次数
				.setSql(" count=count+1 ")
				// 下一次. 下次通知时间不为null时设置
				.set(nextTime != null, Notify::getNextTime, nextTime);

		update(null, wrapper);
	}

}
