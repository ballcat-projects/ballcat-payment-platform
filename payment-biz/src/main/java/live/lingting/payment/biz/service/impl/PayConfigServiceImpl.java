package live.lingting.payment.biz.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import live.lingting.payment.Page;
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
import live.lingting.payment.pay.ThirdPay;
import live.lingting.payment.sdk.enums.ThirdPart;

/**
 * @author lingting 2021/8/10 11:04
 */
@Service
public class PayConfigServiceImpl extends ServiceImpl<PayConfigMapper, PayConfig> implements PayConfigService {

	@Autowired
	protected List<AbstractThirdManager<? extends ThirdPay>> managers;

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
		save(config);
		reload(dto.getMark(), dto.getThirdPart());
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
		reload(config.getMark(), config.getThirdPart());
	}

	@Override
	public void delete(Integer id) {
		removeById(id);
	}

	private void reload(String mark, ThirdPart tp) {
		if (!CollectionUtils.isEmpty(managers)) {
			for (AbstractThirdManager<? extends ThirdPay> manager : managers) {
				// 仅更新指定第三方
				if (!tp.equals(manager.getThird())) {
					continue;
				}
				try {
					manager.reload(mark);
				}
				catch (Exception e) {
					log.error("重新加载支付配置异常! 标识: {}" + mark, e);
				}
			}
		}
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
