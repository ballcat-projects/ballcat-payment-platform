package live.lingting.payment.biz.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import live.lingting.payment.Page;
import live.lingting.payment.biz.Redis;
import live.lingting.payment.biz.mapper.PayConfigMapper;
import live.lingting.payment.biz.mybatis.WrappersX;
import live.lingting.payment.biz.mybatis.conditions.LambdaQueryWrapperX;
import live.lingting.payment.biz.real.third.AbstractThirdManager;
import live.lingting.payment.biz.service.PayConfigService;
import live.lingting.payment.dto.PayConfigCreateDTO;
import live.lingting.payment.dto.PayConfigUpdateDTO;
import live.lingting.payment.entity.PayConfig;
import live.lingting.payment.enums.ResponseCode;
import live.lingting.payment.exception.PaymentException;
import live.lingting.payment.sdk.enums.ThirdPart;

import static live.lingting.payment.enums.ResponseCode.UNKNOWN_THIRD_PARTY;

/**
 * @author lingting 2021/8/10 11:04
 */
@Service
@RequiredArgsConstructor
public class PayConfigServiceImpl extends ServiceImpl<PayConfigMapper, PayConfig> implements PayConfigService {

	private final Redis redis;

	@Override
	public List<PayConfig> listByThird(ThirdPart third) {
		LambdaQueryWrapperX<PayConfig> wrapper = WrappersX.<PayConfig>lambdaQueryX().eq(PayConfig::getThirdPart, third);
		return list(wrapper);
	}

	@Override
	public List<PayConfig> listDeletedByIgnore(List<String> marks) {
		LambdaQueryWrapperX<PayConfig> wrapper = WrappersX.<PayConfig>lambdaQueryX()
				// 不在指定标识内
				.notInIfPresent(PayConfig::getMark, marks)
				// 被删除
				.gt(PayConfig::getDeleted, 0)

				;
		return list(wrapper);
	}

	@Override
	public Page<PayConfig> list(Page<PayConfig> page, PayConfig qo) {
		return baseMapper.list(page, qo);
	}

	@Override
	public PayConfig getByMarkAndThird(String mark, ThirdPart third) {
		return baseMapper.selectOne(baseMapper.getWrapper(new PayConfig().setMark(mark).setThirdPart(third)));
	}

	@Override
	public void create(PayConfigCreateDTO dto) throws PaymentException {
		PayConfig config = dto.toEntity();
		valid(config);
		// 重复校验
		if (getByMarkAndThird(dto.getMark(), dto.getThirdPart()) != null) {
			throw new PaymentException(ResponseCode.PAYMENT_CONFIG_EXIST);
		}

		save(config);
		reload(dto.getThirdPart());
	}

	@Override
	public void edit(PayConfigUpdateDTO dto) throws PaymentException {
		PayConfig oldConfig = getById(dto.getId());
		PayConfig config = dto.toEntity();
		// 支付方式不允许修改
		config.setThirdPart(oldConfig.getThirdPart());
		// 支付标识不允许修改
		config.setMark(oldConfig.getMark());
		valid(config);
		updateById(config);
		reload(config.getThirdPart());
	}

	@Override
	public void delete(Integer id) {
		removeById(id);
	}

	private void reload(ThirdPart tp) {
		redis.set(AbstractThirdManager.getConfigUpdateKey(tp), Long.toString(System.currentTimeMillis()));
	}

	private void valid(PayConfig config) throws PaymentException {
		switch (config.getThirdPart()) {
			case WX:
				validWx(config);
				break;
			case ALI:
				validAli(config);
				break;
			case BC_UNKNOWN:
				// TODO 银行卡校验
				break;
			default:
				throw new PaymentException(UNKNOWN_THIRD_PARTY);
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
