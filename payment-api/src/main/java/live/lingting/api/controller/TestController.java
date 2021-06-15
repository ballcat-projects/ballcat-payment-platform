package live.lingting.api.controller;

import cn.hutool.core.util.RandomUtil;
import com.hccake.ballcat.common.util.JsonUtils;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import live.lingting.api.util.SecurityUtils;
import live.lingting.entity.Project;
import live.lingting.sdk.constant.SdkConstants;
import live.lingting.sdk.util.MixUtils;

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
	public String test(@RequestBody Map<String, String> map) {
		Project project = SecurityUtils.getProject();

		final String res = RandomUtil.randomInt(20) % 2 == 0 ? SdkConstants.SUCCESS_BODY : RandomUtil.randomString(6);
		log.info("收到请求: 参数: {}", JsonUtils.toJson(map));
		log.info("收到请求: 验签: {}", MixUtils.verifySign(project.getApiSecurity(), map));
		log.info("收到请求: 结果: {}", res);

		return res;
	}

}
