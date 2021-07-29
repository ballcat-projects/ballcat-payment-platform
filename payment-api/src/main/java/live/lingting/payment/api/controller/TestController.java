package live.lingting.payment.api.controller;

import cn.hutool.core.util.RandomUtil;
import com.hccake.ballcat.common.util.JsonUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import live.lingting.payment.api.util.SecurityUtils;
import live.lingting.payment.entity.Project;
import live.lingting.payment.sdk.constant.SdkConstants;
import live.lingting.payment.sdk.domain.MixCallback;
import live.lingting.payment.sdk.util.MixUtils;

/**
 * @author lingting 2021/6/15 19:30
 */
@Slf4j
@Profile("dev")
@RestController
@RequiredArgsConstructor
@RequestMapping("test")
public class TestController {

	@RequestMapping
	public String test(@RequestBody MixCallback callback) {
		Project project = SecurityUtils.getProject();

		final String res = RandomUtil.randomInt(20) % 2 == 0 ? SdkConstants.SUCCESS_BODY : RandomUtil.randomString(6);
		log.info("收到请求: 参数: {}", JsonUtils.toJson(callback));
		log.info("收到请求: 验签: {}", MixUtils.verifySign(project.getApiSecurity(), callback));
		log.info("收到请求: 结果: {}", res);

		return res;
	}

}
