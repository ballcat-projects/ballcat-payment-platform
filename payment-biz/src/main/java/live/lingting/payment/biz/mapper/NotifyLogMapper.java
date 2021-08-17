package live.lingting.payment.biz.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import live.lingting.payment.Page;
import live.lingting.payment.biz.mybatis.WrappersX;
import live.lingting.payment.entity.NotifyLog;

/**
 * @author lingting 2021/6/4 13:42
 */
public interface NotifyLogMapper extends BaseMapper<NotifyLog> {

	/**
	 * 组装 sql
	 * @param log 条件
	 * @return com.baomidou.mybatisplus.core.conditions.Wrapper<live.lingting.payment.entity.NotifyLog>
	 * @author lingting 2021-06-07 14:08
	 */
	default Wrapper<NotifyLog> getWrapper(NotifyLog log) {
		return WrappersX.<NotifyLog>lambdaQueryX()
				// tradeNo
				.eqIfPresent(NotifyLog::getTradeNo, log.getTradeNo())
				// projectId
				.eqIfPresent(NotifyLog::getProjectId, log.getProjectId())
				// status
				.eqIfPresent(NotifyLog::getStatus, log.getStatus())
				// httpStatus
				.eqIfPresent(NotifyLog::getHttpStatus, log.getHttpStatus());
	}

	/**
	 * 查询
	 * @param page 分页
	 * @param log 条件
	 * @return live.lingting.payment.Page<live.lingting.payment.entity.NotifyLog>
	 * @author lingting 2021-06-07 14:07
	 */
	default Page<NotifyLog> list(Page<NotifyLog> page, NotifyLog log) {
		final IPage<NotifyLog> iPage = selectPage(page.toPage(), getWrapper(log));

		final Page<NotifyLog> Page = new Page<>();
		Page.setRecords(iPage.getRecords());
		Page.setTotal(iPage.getTotal());
		return Page;
	}

}
