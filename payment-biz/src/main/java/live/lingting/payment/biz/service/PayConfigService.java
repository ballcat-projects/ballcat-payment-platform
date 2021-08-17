package live.lingting.payment.biz.service;

import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;
import live.lingting.payment.Page;
import live.lingting.payment.entity.PayConfig;
import live.lingting.payment.exception.PaymentException;
import live.lingting.payment.sdk.enums.ThirdPart;

/**
 * @author lingting 2021-08-10 11:03
 */
public interface PayConfigService extends IService<PayConfig> {

	/**
	 * 获取所有指定第三方的支付配置
	 * @param third 第三方
	 * @return java.util.List<live.lingting.payment.entity.PayConfig>
	 * @author lingting 2021-08-10 11:15
	 */
	List<PayConfig> listByThird(ThirdPart third);

	/**
	 * 分页查询
	 * @param page 分页
	 * @param qo 参数
	 * @return live.lingting.payment.Page<live.lingting.payment.entity.PayConfig>
	 * @author lingting 2021-08-13 15:16
	 */
	Page<PayConfig> list(Page<PayConfig> page, PayConfig qo);

	/**
	 * 新增支付配置
	 * @param config 配置详情
	 * @author lingting 2021-08-13 15:18
	 * @throws PaymentException 异常
	 */
	void create(PayConfig config) throws PaymentException;

	/**
	 * 编辑支付配置
	 * @param config 支付配置
	 * @author lingting 2021-08-17 10:26
	 * @throws PaymentException 异常
	 */
	void edit(PayConfig config) throws PaymentException;

	/**
	 * 删除支付配置
	 * @param id 支付配置id
	 * @author lingting 2021-08-13 15:23
	 */
	void delete(Integer id);

}
