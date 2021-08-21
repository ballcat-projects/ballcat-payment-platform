package live.lingting.payment.biz.service;

import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;
import live.lingting.payment.Page;
import live.lingting.payment.dto.PayConfigCreateDTO;
import live.lingting.payment.dto.PayConfigUpdateDTO;
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
	 * 获取指定标识外的, 被逻辑删除的支付配置
	 * @param marks 指定标识
	 * @return java.util.List<live.lingting.payment.entity.PayConfig>
	 * @author lingting 2021-08-20 17:24
	 */
	List<PayConfig> listDeletedByIgnore(List<String> marks);

	/**
	 * 分页查询
	 * @param page 分页
	 * @param qo 参数
	 * @return live.lingting.payment.Page<live.lingting.payment.entity.PayConfig>
	 * @author lingting 2021-08-13 15:16
	 */
	Page<PayConfig> list(Page<PayConfig> page, PayConfig qo);

	/**
	 * 获取指定标识, 指定第三方的支付配置
	 * @param mark 标识
	 * @param third 第三方
	 * @return live.lingting.payment.entity.PayConfig
	 * @author lingting 2021-08-20 16:50
	 */
	PayConfig getByMarkAndThird(String mark, ThirdPart third);

	/**
	 * 新增支付配置
	 * @param config 配置详情
	 * @author lingting 2021-08-13 15:18
	 * @throws PaymentException 异常
	 */
	void create(PayConfigCreateDTO config) throws PaymentException;

	/**
	 * 编辑支付配置
	 * @param config 支付配置
	 * @author lingting 2021-08-17 10:26
	 * @throws PaymentException 异常
	 */
	void edit(PayConfigUpdateDTO config) throws PaymentException;

	/**
	 * 删除支付配置
	 * @param id 支付配置id
	 * @author lingting 2021-08-13 15:23
	 */
	void delete(Integer id);

}
