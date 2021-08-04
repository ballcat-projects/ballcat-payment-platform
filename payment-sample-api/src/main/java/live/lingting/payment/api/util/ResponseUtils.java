package live.lingting.payment.api.util;

import com.hccake.ballcat.common.model.result.R;
import com.hccake.ballcat.common.model.result.ResultCode;
import live.lingting.payment.http.utils.JacksonUtils;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

/**
 * @author lingting 2021/4/29 18:14
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ResponseUtils {

	public static final String CONTENT_TYPE = "Content-Type";

	/**
	 * 写入返回值
	 *
	 * @author lingting 2021-04-30 10:09
	 */
	public static void write(HttpServletResponse response, HttpStatus status, ResultCode code) throws IOException {
		response.setHeader(CONTENT_TYPE, MediaType.APPLICATION_JSON.toString());
		write(response, status, JacksonUtils.toJson(R.failed(code)));
	}

	public static void write(HttpServletResponse response, HttpStatus status, String msg) throws IOException {
		response.setStatus(status.value());
		write(response, msg);
	}

	public static void write(HttpServletResponse response, String msg) throws IOException {
		response.setCharacterEncoding(StandardCharsets.UTF_8.toString());
		response.getWriter().write(msg);
	}

	/**
	 * 返回404信息
	 *
	 * @author lingting 2021-04-30 10:15
	 */
	public static void notFound(HttpServletRequest request, HttpServletResponse response) throws IOException {
		// 不允许的请求, 返回404
		response.setHeader(CONTENT_TYPE, MediaType.APPLICATION_JSON.toString());
		write(response, HttpStatus.NOT_FOUND, JacksonUtils.toJson(NotFount.of(request)));
	}

	@Data
	@Accessors(chain = true)
	private static class NotFount {

		private String timestamp;

		private Integer status = HttpStatus.NOT_FOUND.value();

		private String error = "Not Found";

		private String message = "";

		private String path;

		public static NotFount of(HttpServletRequest request) {
			final NotFount notFount = new NotFount();
			notFount.setPath(request.getRequestURI());
			notFount.setTimestamp(DateTimeFormatter.ISO_OFFSET_DATE_TIME.format(ZonedDateTime.now()));
			return notFount;
		}

	}

}
