package live.lingting.service;

import com.hccake.ballcat.common.model.domain.PageResult;
import com.hccake.extend.mybatis.plus.service.ExtendService;
import java.time.LocalDateTime;
import java.util.List;
import live.lingting.Page;
import live.lingting.entity.Pay;

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
	 * 虚拟支付超时未提交
	 * @param tradeNo 交易号
	 * @param desc 描述
	 * @return boolean 执行结果
	 * @author lingting 2021-06-09 14:16
	 */
	default boolean fail(String tradeNo, String desc) {
		return fail(tradeNo, desc, LocalDateTime.now());
	}

	/**
	 * 虚拟支付超时未提交
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
