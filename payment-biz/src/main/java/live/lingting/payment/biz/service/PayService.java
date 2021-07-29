package live.lingting.payment.biz.service;

import com.hccake.ballcat.common.model.domain.PageResult;
import com.hccake.extend.mybatis.plus.service.ExtendService;
import java.time.LocalDateTime;
import java.util.List;
import live.lingting.payment.Page;
import live.lingting.payment.entity.Pay;
import live.lingting.payment.entity.Project;
import live.lingting.payment.sdk.enums.NotifyStatus;
import live.lingting.payment.sdk.model.MixVirtualPayModel;

/**
 * @author lingting 2021/6/4 13:40
 */
public interface PayService extends ExtendService<Pay> {

	/**
	 * 查询
	 * @param page 分页
	 * @param pay 条件
	 * @return com.hccake.ballcat.common.model.domain.PageResult<live.lingting.entity.Pay>
	 * @author lingting 2021-06-07 11:05
	 */
	PageResult<Pay> list(Page<Pay> page, Pay pay);

	/**
	 * 查询所有满足条件的支付信息
	 * @param pay 条件
	 * @return java.util.List<live.lingting.entity.Pay>
	 * @author lingting 2021-06-09 14:03
	 */
	List<Pay> list(Pay pay);

	/**
	 * 查询虚拟货币未提交hash的超时支付
	 * @param maxTime 支付信息最大创建时间
	 * @return java.util.List<live.lingting.entity.Pay>
	 * @author lingting 2021-06-10 10:06
	 */
	List<Pay> listVirtualTimeout(LocalDateTime maxTime);

	/**
	 * 查询重试超时的支付信息
	 * @return java.util.List<live.lingting.entity.Pay>
	 * @author lingting 2021-06-10 11:27
	 */
	List<Pay> listVirtualRetryTimeout();

	/**
	 * 获取所有需要通知的支付信息
	 * @return java.util.List<live.lingting.entity.Pay>
	 * @author lingting 2021-06-10 17:10
	 */
	List<Pay> listNotify();

	/**
	 * 根据交易号或者项目交易获取信息
	 * @param tradeNo 交易号
	 * @param projectTradeNo 项目交易号
	 * @return live.lingting.entity.Pay
	 * @author lingting 2021-06-09 17:29
	 */
	Pay getByNo(String tradeNo, String projectTradeNo);

	/**
	 * 查询数量
	 * @param pay 条件
	 * @return long
	 * @author lingting 2021-06-09 17:42
	 */
	long count(Pay pay);

	/**
	 * 虚拟货币支付 创建支付
	 * @param model 基础参数
	 * @param project 所属
	 * @return live.lingting.entity.Pay
	 * @author lingting 2021-06-09 17:55
	 */
	Pay virtualCreate(MixVirtualPayModel model, Project project);

	/**
	 * 虚拟货币支付 提交hash
	 * @param pay 支付信息
	 * @param hash hash
	 * @return boolean
	 * @author lingting 2021-06-09 17:49
	 */
	boolean virtualSubmit(Pay pay, String hash);

	/**
	 * 虚拟货币支付重试
	 * @param pay 支付信息
	 * @param hash 新hash
	 * @return boolean
	 * @author lingting 2021-06-10 10:56
	 */
	boolean virtualRetry(Pay pay, String hash);

	/**
	 * 对指定支付进行通知上锁
	 * @param pay 支付信息
	 * @return boolean
	 * @author lingting 2021-06-15 09:55
	 */
	boolean notifying(Pay pay);

	/**
	 * 校验第三方交易号是否可用
	 * @param pay 支付信息
	 * @param thirdTradeNo 第三方交易号
	 * @author lingting 2021-07-14 16:48
	 */
	void validateThirdTradeNo(Pay pay, String thirdTradeNo);

	/**
	 * 支付失败
	 * @param pay 支付信息
	 * @param desc 描述
	 * @param retryEndTime 重试截止时间
	 * @return boolean 执行结果
	 * @author lingting 2021-06-09 14:16
	 */
	boolean fail(Pay pay, String desc, LocalDateTime retryEndTime);

	/**
	 * 已完成支付
	 * @param pay 支付信息
	 * @return boolean
	 * @author lingting 2021-06-09 15:33
	 */
	boolean success(Pay pay);

	/**
	 * 通知完成
	 * @param pay 支付信息
	 * @param status 新状态
	 * @author lingting 2021-06-15 22:26
	 */
	void notifyComplete(Pay pay, NotifyStatus status);

	/**
	 * 通知转为等待
	 * @param pay 支付信息
	 * @author lingting 2021-06-17 15:29
	 */
	void notifyWait(Pay pay);

	/**
	 * 强制重试
	 * @param tradeNo 交易号
	 * @param projectTradeNo 项目交易号
	 * @author lingting 2021-06-24 21:13
	 */
	void forciblyRetry(String tradeNo, String projectTradeNo);

	/**
	 * 强制失败
	 * @param tradeNo 交易号
	 * @param projectTradeNo 项目交易号
	 * @author lingting 2021-06-24 21:13
	 */
	void forciblyFail(String tradeNo, String projectTradeNo);

	/**
	 * 查询所有等待的转账支付
	 * @return java.util.List<live.lingting.entity.Pay>
	 * @author lingting 2021-07-14 15:47
	 */
	List<Pay> listWaitTransfer();

	/**
	 * 查询所有已过期真实货币支付信息
	 * @param maxTime 最大创建时间
	 * @return java.util.List<live.lingting.entity.Pay>
	 * @author lingting 2021-07-14 17:14
	 */
	List<Pay> listRealExpire(LocalDateTime maxTime);

}
