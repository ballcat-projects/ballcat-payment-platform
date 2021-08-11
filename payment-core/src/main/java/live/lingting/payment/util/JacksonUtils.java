package live.lingting.payment.util;

import com.fasterxml.jackson.core.json.JsonReadFeature;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.lang.reflect.Type;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import live.lingting.payment.jackson.JavaTimeModule;

/**
 * @author lingting 2021/6/9 14:28
 */
@UtilityClass
public class JacksonUtils {

	static final ObjectMapper MAPPER = new ObjectMapper();

	static final String JSON_READ_FEATURE_CLASS = "com.fasterxml.jackson.core.json.JsonReadFeature";

	static {
		// 序列化时忽略未知属性
		MAPPER.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
		// 单值元素可以被设置成 array, 防止处理 ["a"] 为 List<String> 时报错
		MAPPER.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);
		// 时间解析器
		MAPPER.registerModule(new JavaTimeModule());
		if (ClassUtils.isPresent(JSON_READ_FEATURE_CLASS, JacksonUtils.class.getClassLoader())) {
			// 有特殊需要转义字符, 不报错
			MAPPER.enable(JsonReadFeature.ALLOW_UNESCAPED_CONTROL_CHARS.mappedFeature());
		}
	}

	@SneakyThrows
	public static String toJson(Object obj) {
		return MAPPER.writeValueAsString(obj);
	}

	@SneakyThrows
	public static <T> T toObj(String json, Class<T> r) {
		return MAPPER.readValue(json, r);
	}

	@SneakyThrows
	public static <T> T toObj(String json, Type t) {
		return MAPPER.readValue(json, MAPPER.constructType(t));
	}

	@SneakyThrows
	public static <T> T toObj(String json, TypeReference<T> t) {
		return MAPPER.readValue(json, new TypeReference<T>() {
			@Override
			public Type getType() {
				return t.getType();
			}
		});
	}

}
