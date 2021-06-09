package live.lingting.service;

import com.hccake.ballcat.common.model.domain.PageResult;
import com.hccake.extend.mybatis.plus.service.ExtendService;
import java.time.LocalDateTime;
import java.util.List;
import live.lingting.Page;
import live.lingting.entity.Pay;
import live.lingting.entity.Project;
import live.lingting.sdk.model.MixVirtualPayModel;

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
	 * @param tradeNo 交易号
	 * @param hash hash
	 * @return boolean
	 * @author lingting 2021-06-09 17:49
	 */
	boolean virtualSubmit(String tradeNo, String hash);

	/**
	 * 支付失败
	 * @param tradeNo 交易号
	 * @param desc 描述
	 * @return boolean 执行结果
	 * @author lingting 2021-06-09 14:16
	 */
	default boolean fail(String tradeNo, String desc) {
		return fail(tradeNo, desc, LocalDateTime.now());
	}

	/**
	 * 支付失败
	 * @param tradeNo 交易号
	 * @param desc 描述
	 * @param retryEndTime 重试截止时间
	 * @return boolean 执行结果
	 * @author lingting 2021-06-09 14:16
	 */
	boolean fail(String tradeNo, String desc, LocalDateTime retryEndTime);

	/**
	 * 已完成支付
	 * @param pay 支付信息
	 * @return boolean
	 * @author lingting 2021-06-09 15:33
	 */
	boolean success(Pay pay);

}
