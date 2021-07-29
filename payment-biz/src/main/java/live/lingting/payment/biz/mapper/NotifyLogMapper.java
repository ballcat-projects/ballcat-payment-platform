package live.lingting.payment.biz.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.hccake.ballcat.common.model.domain.PageResult;
import com.hccake.extend.mybatis.plus.mapper.ExtendMapper;
import com.hccake.extend.mybatis.plus.toolkit.WrappersX;
import live.lingting.payment.Page;
import live.lingting.payment.entity.NotifyLog;

/**
 * @author lingting 2021/6/4 13:42
 */
public interface NotifyLogMapper extends ExtendMapper<NotifyLog> {

	/**
	 * 组装 sql
	 * @param log 条件
	 * @return com.baomidou.mybatisplus.core.conditions.Wrapper<live.lingting.entity.NotifyLog>
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
	 * @return com.hccake.ballcat.common.model.domain.PageResult<live.lingting.entity.NotifyLog>
	 * @author lingting 2021-06-07 14:07
	 */
	default PageResult<NotifyLog> list(Page<NotifyLog> page, NotifyLog log) {
		final IPage<NotifyLog> iPage = selectPage(page.toPage(), getWrapper(log));

		final PageResult<NotifyLog> pageResult = new PageResult<>();
		pageResult.setRecords(iPage.getRecords());
		pageResult.setTotal(iPage.getTotal());
		return pageResult;
	}

}
