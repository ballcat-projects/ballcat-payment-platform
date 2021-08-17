package live.lingting.payment.biz.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import live.lingting.payment.Page;
import live.lingting.payment.biz.mapper.PayConfigMapper;
import live.lingting.payment.biz.mybatis.WrappersX;
import live.lingting.payment.biz.mybatis.conditions.LambdaQueryWrapperX;
import live.lingting.payment.biz.service.PayConfigService;
import live.lingting.payment.entity.PayConfig;
import live.lingting.payment.enums.ResponseCode;
import live.lingting.payment.exception.PaymentException;
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

	@Override
	public Page<PayConfig> list(Page<PayConfig> page, PayConfig qo) {
		return baseMapper.list(page, qo);
	}

	@Override
	public void create(PayConfig config) throws PaymentException {
		valid(config);
		save(config);
	}

	@Override
	public void edit(PayConfig config) throws PaymentException {
		PayConfig oldConfig = getById(config.getId());
		// 支付方式不允许修改
		config.setThirdPart(oldConfig.getThirdPart());

		valid(config);

		updateById(config);
	}

	@Override
	public void delete(Integer id) {
		removeById(id);
	}

	private void valid(PayConfig config) throws PaymentException {
		switch (config.getThirdPart()) {
		case WX:
			validWx(config);
			break;
		default:
			validAli(config);
		}
	}

	private void validWx(PayConfig config) throws PaymentException {
		if (!StringUtils.hasText(config.getWxAppId()) || !StringUtils.hasText(config.getWxMchId())
				|| !StringUtils.hasText(config.getWxMchKey())) {
			throw new PaymentException(ResponseCode.PAYMENT_CONFIG_ERROR);
		}
	}

	private void validAli(PayConfig config) throws PaymentException {
		if (!StringUtils.hasText(config.getAliAppId()) || !StringUtils.hasText(config.getAliPrivateKey())
				|| !StringUtils.hasText(config.getAliPayPublicKey())) {
			throw new PaymentException(ResponseCode.PAYMENT_CONFIG_ERROR);
		}
	}

}
