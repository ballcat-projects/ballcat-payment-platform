package live.lingting.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import com.hccake.ballcat.common.model.domain.PageResult;
import com.hccake.extend.mybatis.plus.mapper.ExtendMapper;
import com.hccake.extend.mybatis.plus.toolkit.WrappersX;
import java.time.LocalDateTime;
import live.lingting.Page;
import live.lingting.entity.Pay;
import live.lingting.sdk.enums.PayStatus;

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
				.eqIfPresent(Pay::getNotifyStatus, pay.getNotifyStatus())
				// createTime
				.leIfPresent(Pay::getCreateTime, pay.getCreateTime());
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

	/**
	 * 虚拟支付超时未提交
	 * @param tradeNo 交易号
	 * @param desc 描述
	 * @param retryEndTime 重试截止时间
	 * @return boolean 执行结果
	 * @author lingting 2021-06-09 14:16
	 */
	default boolean fail(String tradeNo, String desc, LocalDateTime retryEndTime) {
		Wrapper<Pay> wrapper = Wrappers.<Pay>lambdaUpdate()
				// 限制交易信息
				.eq(Pay::getTradeNo, tradeNo)
				// 限制原状态
				.eq(Pay::getStatus, PayStatus.WAIT)
				// 设置目标状态
				.set(Pay::getStatus, PayStatus.FAIL)
				// 设置描述
				.set(Pay::getDesc, desc)
				// 时间
				.set(Pay::getRetryEndTime, retryEndTime)
				// 完成时间
				.set(Pay::getCompleteTime, LocalDateTime.now());

		return SqlHelper.retBool(update(null, wrapper));
	}

	/**
	 * 已完成支付
	 * @param tradeNo 交易号
	 * @return boolean
	 * @author lingting 2021-06-09 15:33
	 */
	default boolean success(String tradeNo) {
		Wrapper<Pay> wrapper = Wrappers.<Pay>lambdaUpdate()
				// 限制交易信息
				.eq(Pay::getTradeNo, tradeNo)
				// 限制原状态
				.eq(Pay::getStatus, PayStatus.WAIT)
				// 设置目标状态
				.set(Pay::getStatus, PayStatus.SUCCESS)
				// 设置描述
				.set(Pay::getDesc, "支付成功")
				// 完成时间
				.set(Pay::getCompleteTime, LocalDateTime.now());

		return SqlHelper.retBool(update(null, wrapper));
	}

}
