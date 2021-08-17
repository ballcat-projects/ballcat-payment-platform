package live.lingting.payment.biz.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import live.lingting.payment.Page;
import live.lingting.payment.biz.mybatis.WrappersX;
import live.lingting.payment.entity.PayConfig;

/**
 * @author lingting 2021/8/10 11:02
 */
public interface PayConfigMapper extends BaseMapper<PayConfig> {

	/**
	 * 组装sql
	 * @param project 条件
	 * @return com.baomidou.mybatisplus.core.conditions.Wrapper<live.lingting.payment.entity.Project>
	 * @author lingting 2021-06-07 14:08
	 */
	default Wrapper<PayConfig> getWrapper(PayConfig project) {
		return WrappersX.<PayConfig>lambdaQueryX()
				// 标识
				.likeIfPresent(PayConfig::getMark, project.getMark())
				// 第三方
				.eqIfPresent(PayConfig::getThirdPart, project.getThirdPart());
	}

	/**
	 * 分页查询
	 * @param page 分页
	 * @param qo 参数
	 * @return live.lingting.payment.Page<live.lingting.payment.entity.PayConfig>
	 * @author lingting 2021-08-13 15:16
	 */
	default Page<PayConfig> list(Page<PayConfig> page, PayConfig qo) {
		final IPage<PayConfig> iPage = selectPage(page.toPage(), getWrapper(qo));
		return new Page<>(iPage.getRecords(), iPage.getTotal());
	}

}
