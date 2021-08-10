package live.lingting.payment.biz.service;

import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;
import live.lingting.payment.entity.PayConfig;
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

}
