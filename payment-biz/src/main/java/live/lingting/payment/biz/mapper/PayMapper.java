package live.lingting.payment.biz.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import java.time.LocalDateTime;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.util.StringUtils;
import live.lingting.payment.Page;
import live.lingting.payment.biz.mybatis.WrappersX;
import live.lingting.payment.biz.mybatis.conditions.LambdaQueryWrapperX;
import live.lingting.payment.entity.Pay;
import live.lingting.payment.sdk.enums.Currency;
import live.lingting.payment.sdk.enums.NotifyStatus;
import live.lingting.payment.sdk.enums.PayStatus;
import live.lingting.payment.vo.PayVO;

/**
 * @author lingting 2021/6/4 13:40
 */
public interface PayMapper extends BaseMapper<Pay> {

	/**
	 * 组装sql
	 * @param pay 条件
	 * @return com.baomidou.mybatisplus.core.conditions.Wrapper<live.lingting.payment.entity.Pay>
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
	 * @return live.lingting.payment.Page<live.lingting.payment.entity.Pay>
	 * @author lingting 2021-06-07 11:05
	 */
	default Page<Pay> list(Page<Pay> page, Pay pay) {
		final IPage<Pay> iPage = selectPage(page.toPage(), getWrapper(pay));

		final Page<Pay> rPage = new Page<>();
		rPage.setRecords(iPage.getRecords());
		rPage.setTotal(iPage.getTotal());
		return rPage;
	}

	/**
	 * 查询
	 * @param page 分页
	 * @param wrapper 条件
	 * @return live.lingting.payment.Page<live.lingting.payment.entity.Pay>
	 * @author lingting 2021-06-07 11:05
	 */
	@Select("select *, (select p.name from lingting_payment_project p where  p.id=lpp.project_id) as project_name from `lingting_payment_pay` lpp ")
	IPage<PayVO> listVo(IPage<Pay> page, @Param(Constants.WRAPPER) Wrapper<Pay> wrapper);

	/**
	 * 查询虚拟货币未提交hash的超时支付
	 * @param maxTime 支付信息最大创建时间
	 * @return java.util.List<live.lingting.payment.entity.Pay>
	 * @author lingting 2021-06-10 10:06
	 */
	default List<Pay> listVirtualTimeout(LocalDateTime maxTime) {

		final LambdaQueryWrapperX<Pay> wrapper = WrappersX.<Pay>lambdaQueryX()
				// thirdPartTradeNo
				.eq(Pay::getThirdPartTradeNo, "")
				// status
				.eq(Pay::getStatus, PayStatus.WAIT)
				// currency
				.in(Pay::getCurrency, Currency.VIRTUAL_LIST)
				// createTime
				.le(Pay::getCreateTime, maxTime);

		return selectList(wrapper);
	}

	/**
	 * 查询重试超时的支付信息
	 * @return java.util.List<live.lingting.payment.entity.Pay>
	 * @author lingting 2021-06-10 11:27
	 */
	default List<Pay> listVirtualRetryTimeout() {
		final LambdaQueryWrapperX<Pay> wrapper = WrappersX.<Pay>lambdaQueryX()
				// status
				.eq(Pay::getStatus, PayStatus.RETRY)
				// currency
				.eq(Pay::getCurrency, Currency.USDT)
				// 已超过重试时间
				.le(Pay::getRetryEndTime, LocalDateTime.now());

		return selectList(wrapper);
	}

	/**
	 * 获取所有需要通知的支付信息
	 * @return java.util.List<live.lingting.payment.entity.Pay>
	 * @author lingting 2021-06-10 17:10
	 */
	default List<Pay> listNotify() {
		final LambdaQueryWrapperX<Pay> wrapper = WrappersX.<Pay>lambdaQueryX()
				// 支付状态不为等待
				.ne(Pay::getStatus, PayStatus.WAIT)
				// 通知状态等待
				.eq(Pay::getNotifyStatus, NotifyStatus.WAIT);

		return selectList(wrapper);
	}

	/**
	 * 虚拟货币支付 提交hash
	 * @param tradeNo 交易号
	 * @param hash hash
	 * @return boolean
	 * @author lingting 2021-06-09 17:49
	 */
	default boolean virtualSubmit(String tradeNo, String hash) {
		Wrapper<Pay> wrapper = Wrappers.<Pay>lambdaUpdate()
				// 限定支付信息
				.eq(Pay::getTradeNo, tradeNo)
				// 限定未提交hash
				.eq(Pay::getThirdPartTradeNo, "")
				// 限定状态
				.eq(Pay::getStatus, PayStatus.WAIT)
				// 更新hash
				.set(Pay::getThirdPartTradeNo, hash);

		return SqlHelper.retBool(update(null, wrapper));
	}

	/**
	 * 虚拟货币支付重试
	 * @param tradeNo 交易号
	 * @param hash 新hash
	 * @return boolean
	 * @author lingting 2021-06-10 10:56
	 */
	default boolean virtualRetry(String tradeNo, String hash) {
		Wrapper<Pay> wrapper = Wrappers.<Pay>lambdaUpdate()
				// 限定支付信息
				.eq(Pay::getTradeNo, tradeNo)
				// 限定已提交hash
				.ne(Pay::getThirdPartTradeNo, "")
				// 限定状态
				.eq(Pay::getStatus, PayStatus.RETRY)
				// 如果hash值不为空, 则更新hash
				.set(StringUtils.hasText(hash), Pay::getThirdPartTradeNo, hash)
				// 通知状态更新为等待通知
				.set(Pay::getNotifyStatus, NotifyStatus.WAIT)
				// 状态更新为等待支付
				.set(Pay::getStatus, PayStatus.WAIT);

		return SqlHelper.retBool(update(null, wrapper));
	}

	/**
	 * 对指定支付进行通知上锁
	 * @param pay 支付信息
	 * @return boolean
	 * @author lingting 2021-06-15 09:55
	 */
	default boolean notifying(Pay pay) {
		Wrapper<Pay> wrapper = Wrappers.<Pay>lambdaUpdate()
				// 限定支付信息
				.eq(Pay::getTradeNo, pay.getTradeNo())
				// 限定通知状态
				.eq(Pay::getNotifyStatus, NotifyStatus.WAIT)
				// 状态更新为等待支付
				.set(Pay::getNotifyStatus, NotifyStatus.ING);

		return SqlHelper.retBool(update(null, wrapper));
	}

	/**
	 * 虚拟支付失败处理
	 * @param pay 支付信息
	 * @param desc 描述
	 * @param retryEndTime 重试截止时间
	 * @return boolean 执行结果
	 * @author lingting 2021-06-09 14:16
	 */
	default boolean fail(Pay pay, String desc, LocalDateTime retryEndTime) {
		final boolean isRetry = PayStatus.RETRY.equals(pay.getStatus());
		LambdaUpdateWrapper<Pay> wrapper = Wrappers.<Pay>lambdaUpdate()
				// 限制交易信息
				.eq(Pay::getTradeNo, pay.getTradeNo())
				// 限制原状态 - 重试支付限制为重试, 其他支付限制为等待
				.eq(Pay::getStatus, isRetry ? PayStatus.RETRY : PayStatus.WAIT)
				// 设置目标状态
				.set(Pay::getStatus, retryEndTime == null ? PayStatus.FAIL : PayStatus.RETRY)
				// 设置描述
				.set(StringUtils.hasText(desc), Pay::getDesc, desc)
				// 更新第三方交易号
				.set(StringUtils.hasText(pay.getThirdPartTradeNo()), Pay::getThirdPartTradeNo,
						pay.getThirdPartTradeNo())
				// 完成时间
				.set(Pay::getCompleteTime, LocalDateTime.now());

		// 仅在 重试时间不为空 且 非重试支付才更新重试结束时间
		if (retryEndTime != null && !isRetry) {
			// 重试结束时间 - 如果已有重试结束时间, 不更新
			wrapper.setSql(
					String.format(" retry_end_time=IF(retry_end_time is NULL, '%s' , retry_end_time) ", retryEndTime));
		}

		return SqlHelper.retBool(update(null, wrapper));
	}

	/**
	 * 已完成支付
	 * @param pay 支付信息
	 * @return boolean
	 * @author lingting 2021-06-09 15:33
	 */
	default boolean success(Pay pay) {
		Wrapper<Pay> wrapper = Wrappers.<Pay>lambdaUpdate()
				// 限制交易信息
				.eq(Pay::getTradeNo, pay.getTradeNo())
				// 限制原状态
				.eq(Pay::getStatus, PayStatus.WAIT)
				// 设置目标状态
				.set(Pay::getStatus, PayStatus.SUCCESS)
				// 设置金额
				.set(Pay::getAmount, pay.getAmount())
				// 更新第三方交易号
				.set(StringUtils.hasText(pay.getThirdPartTradeNo()), Pay::getThirdPartTradeNo,
						pay.getThirdPartTradeNo())
				// 设置描述
				.set(Pay::getDesc, "支付成功")
				// 完成时间
				.set(Pay::getCompleteTime, LocalDateTime.now());

		return SqlHelper.retBool(update(null, wrapper));
	}

	/**
	 * 通知完成
	 * @param pay 支付信息
	 * @param status 新状态
	 * @author lingting 2021-06-15 22:26
	 */
	default void notifyComplete(Pay pay, NotifyStatus status) {
		Wrapper<Pay> wrapper = Wrappers.<Pay>lambdaUpdate()
				// 限制交易信息
				.eq(Pay::getTradeNo, pay.getTradeNo())
				// 限制原通知状态
				.eq(Pay::getNotifyStatus, NotifyStatus.ING)
				// 设置目标通知状态
				.set(Pay::getNotifyStatus, status);

		update(null, wrapper);
	}

	/**
	 * 通知转为等待
	 * @param pay 支付信息
	 * @author lingting 2021-06-17 15:29
	 */
	default void notifyWait(Pay pay) {
		Wrapper<Pay> wrapper = Wrappers.<Pay>lambdaUpdate()
				// 限制交易信息
				.eq(Pay::getTradeNo, pay.getTradeNo())
				// 设置目标通知状态
				.set(Pay::getNotifyStatus, NotifyStatus.WAIT);

		update(null, wrapper);
	}

	/**
	 * 强制重试
	 * @param tradeNo 交易号
	 * @param minutes 重试时长, 单位: 分钟
	 * @return boolean
	 * @author lingting 2021-06-24 21:13
	 */
	default boolean forciblyRetry(String tradeNo, long minutes) {
		Wrapper<Pay> wrapper = Wrappers.<Pay>lambdaUpdate()
				// 限制交易信息
				.eq(Pay::getTradeNo, tradeNo)
				// 限制原状态
				.eq(Pay::getStatus, PayStatus.WAIT)
				// 限制货币为USDT
				.eq(Pay::getCurrency, Currency.USDT)
				// 限制hash不为空
				.ne(Pay::getThirdPartTradeNo, "")
				// 限制描述
				.ne(Pay::getDesc, "强制重试!")
				// 新状态
				.set(Pay::getStatus, PayStatus.RETRY)
				// 描述
				.set(Pay::getDesc, "强制重试!")
				// 重试结束时间
				.set(Pay::getRetryEndTime, LocalDateTime.now().plusMinutes(minutes));

		return SqlHelper.retBool(update(null, wrapper));
	}

	/**
	 * 强制失败
	 * @param tradeNo 交易号
	 * @return boolean
	 * @author lingting 2021-06-24 21:13
	 */
	default boolean forciblyFail(String tradeNo) {
		Wrapper<Pay> wrapper = Wrappers.<Pay>lambdaUpdate()
				// 限制交易信息
				.eq(Pay::getTradeNo, tradeNo)
				// 限制原状态
				.in(Pay::getStatus, PayStatus.WAIT, PayStatus.RETRY)
				// 限制描述
				.ne(Pay::getDesc, "强制失败!")
				// 新状态 - 虚拟货币支付为重试, 其他为失败
				.setSql(" status=IF(currency='USDT', 'RETRY', 'FAIL' )")
				// 描述
				.set(Pay::getDesc, "强制失败!")
				// 重试结束时间
				.set(Pay::getRetryEndTime, LocalDateTime.now());

		return SqlHelper.retBool(update(null, wrapper));
	}

}
