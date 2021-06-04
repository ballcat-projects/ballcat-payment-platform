package live.lingting.web.config.type;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import java.io.IOException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * boolean 类型 jackson 配置
 *
 * @author lingting 2020-08-10 10:39
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BooleanConfig {

	public static class Serialize extends JsonSerializer<Boolean> {

		@Override
		public void serialize(Boolean value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
			if (value == null) {
				gen.writeNull();
			}
			else {
				boolean res = value;
				// 布尔类型给前端返回 1 或者 0， 而不是 true 或者 false
				gen.writeNumber(res ? 1 : 0);
			}
		}

	}

	public static class Deserialize extends JsonDeserializer<Boolean> {

		@Override
		public Boolean deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
			String text = p.getText();
			switch (text) {
			case "true":
			case "1":
				return true;
			case "false":
			case "0":
				return false;
			default:
			}
			throw new InvalidFormatException(p, "无法将值 " + text + " 转为布尔类型", text, Boolean.class);
		}

	}

}
