package live.lingting.web.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import javax.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import live.lingting.web.config.type.BooleanConfig;

/**
 * @author lingting 2021/4/14 20:33
 */
@RequiredArgsConstructor
@Configuration(proxyBeanMethods = false)
public class WebJacksonConfiguration {

	private final ObjectMapper mapper;

	@PostConstruct
	public void jacksonObjectMapper() {
		SimpleModule module = new SimpleModule()
				// 自定义布尔类型返回
				.addSerializer(Boolean.class, new BooleanConfig.Serialize())
				// 自定义布尔类型数据接收
				.addDeserializer(Boolean.class, new BooleanConfig.Deserialize());
		mapper.registerModule(module);
	}

}
