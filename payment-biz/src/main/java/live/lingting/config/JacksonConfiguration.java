package live.lingting.config;

import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import javax.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;

/**
 * @author lingting 2021/4/14 20:33
 */
@RequiredArgsConstructor
@Configuration(proxyBeanMethods = false)
public class JacksonConfiguration {

	private final ObjectMapper mapper;

	@PostConstruct
	public void jacksonObjectMapper() {
		// 单值元素可以被设置成 array, 防止处理 ["a"] 为 List<String> 时报错
		mapper.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);
		// 关闭 如果收到的参数中有未知属性则返回 400
		mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

		// 配置给 jackson 用的 om 对象
		JacksonTypeHandler.setObjectMapper(mapper);
	}

}
