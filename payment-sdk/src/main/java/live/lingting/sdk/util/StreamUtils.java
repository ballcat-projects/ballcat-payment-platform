package live.lingting.sdk.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * @author lingting 2021/4/21 17:45
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class StreamUtils {

	/**
	 * 流转字符串
	 * @author lingting 2021-04-29 18:07
	 */
	public static String toString(InputStream stream) throws IOException {
		BufferedReader reader = new BufferedReader(new InputStreamReader(stream, StandardCharsets.UTF_8));
		StringBuilder stringBuilder = new StringBuilder();
		String line;
		while ((line = reader.readLine()) != null) {
			stringBuilder.append(line);
		}
		return stringBuilder.toString();
	}

}
