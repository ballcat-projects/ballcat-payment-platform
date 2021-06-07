package live.lingting.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.hccake.ballcat.common.model.domain.PageResult;
import com.hccake.extend.mybatis.plus.mapper.ExtendMapper;
import com.hccake.extend.mybatis.plus.toolkit.WrappersX;
import live.lingting.Page;
import live.lingting.entity.Pay;

/**
 * @author lingting 2021/6/4 13:40
 */
public interface PayMapper extends ExtendMapper<Pay> {

	/**
	 * 组装sql
	  * @param pay 条件
	 * @return com.baomidou.mybatisplus.core.conditions.Wrapper<live.lingting.entity.Pay>
	 * @author lingting 2021-06-07 14:08
	 */
	default Wrapper<Pay> getWrapper(Pay pay) {
		return WrappersX.<Pay>lambdaQueryX()
				// tradeNo
				.eqIfPresent(Pay::getTradeNo, pay.getTradeNo())
				// projectId
				.eqIfPresent(Pay::getProjectId, pay.getProjectId())
				// projectTradeNo
				.eqIfPresent(Pay::getProjectTradeNo, pay.getProjectTradeNo())
				// thirdPartTradeNo
				.eqIfPresent(Pay::getThirdPartTradeNo, pay.getThirdPartTradeNo())
				// status
				.eqIfPresent(Pay::getStatus, pay.getStatus())
				// currency
				.eqIfPresent(Pay::getCurrency, pay.getCurrency())
				// chain
				.eqIfPresent(Pay::getChain, pay.getChain())
				// address
				.eqIfPresent(Pay::getAddress, pay.getAddress())
				// thirdPart
				.eqIfPresent(Pay::getThirdPart, pay.getThirdPart())
				// mode
				.eqIfPresent(Pay::getMode, pay.getMode())
				// notifyStatus
				.eqIfPresent(Pay::getNotifyStatus, pay.getNotifyStatus());
	}

	/**
	 * 查询
	 * @param page 分页
	 * @param pay 条件
	 * @return com.hccake.ballcat.common.model.domain.PageResult<live.lingting.entity.Pay>
	 * @author lingting 2021-06-07 11:05
	 */
	default PageResult<Pay> list(Page<Pay> page, Pay pay) {
		final IPage<Pay> iPage = selectPage(page.toPage(), getWrapper(pay));

		final PageResult<Pay> pageResult = new PageResult<>();
		pageResult.setRecords(iPage.getRecords());
		pageResult.setTotal(iPage.getTotal());
		return pageResult;
	}

}
