package live.lingting.payment.biz.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import java.util.List;
import org.springframework.stereotype.Service;
import live.lingting.payment.biz.mapper.PayConfigMapper;
import live.lingting.payment.biz.mybatis.WrappersX;
import live.lingting.payment.biz.mybatis.conditions.LambdaQueryWrapperX;
import live.lingting.payment.biz.service.PayConfigService;
import live.lingting.payment.entity.PayConfig;
import live.lingting.payment.sdk.enums.ThirdPart;

/**
 * @author lingting 2021/8/10 11:04
 */
@Service
public class PayConfigServiceImpl extends ServiceImpl<PayConfigMapper, PayConfig> implements PayConfigService {

	@Override
	public List<PayConfig> listByThird(ThirdPart third) {
		LambdaQueryWrapperX<PayConfig> wrapper = WrappersX.<PayConfig>lambdaQueryX().eq(PayConfig::getThirdPart, third);
		return list(wrapper);
	}

}
