package live.lingting.service;

import com.hccake.ballcat.common.model.domain.PageResult;
import com.hccake.extend.mybatis.plus.service.ExtendService;
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

}
