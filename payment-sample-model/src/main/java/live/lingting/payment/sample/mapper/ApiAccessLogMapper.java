package live.lingting.payment.sample.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.hccake.extend.mybatis.plus.mapper.ExtendMapper;
import com.hccake.extend.mybatis.plus.toolkit.WrappersX;
import live.lingting.payment.Page;
import live.lingting.payment.sample.entity.ApiAccessLog;

/**
 * @author lingting 2021/6/25 20:14
 */
public interface ApiAccessLogMapper extends ExtendMapper<ApiAccessLog> {

	/**
	 * 查询
	 * @param page 分页
	 * @param qo 条件
	 * @return com.hccake.ballcat.common.model.domain.PageResult<live.lingting.entity.ApiAccessLog>
	 * @author lingting 2021-06-25 20:23
	 */
	default Page<ApiAccessLog> list(Page<ApiAccessLog> page, ApiAccessLog qo) {
		final IPage<ApiAccessLog> iPage = page.toPage();

		final Wrapper<ApiAccessLog> wrapper = WrappersX.<ApiAccessLog>lambdaQueryX()

				.eqIfPresent(ApiAccessLog::getTraceId, qo.getTraceId())

				.eqIfPresent(ApiAccessLog::getHttpStatus, qo.getHttpStatus())

				.eqIfPresent(ApiAccessLog::getIp, qo.getIp())

				.eqIfPresent(ApiAccessLog::getUri, qo.getUri())

				.eqIfPresent(ApiAccessLog::getKey, qo.getKey())

				.eqIfPresent(ApiAccessLog::getProjectId, qo.getProjectId());

		final IPage<ApiAccessLog> selectPage = this.selectPage(iPage, wrapper);
		return new Page<>(selectPage.getRecords(), selectPage.getTotal());

	}

}