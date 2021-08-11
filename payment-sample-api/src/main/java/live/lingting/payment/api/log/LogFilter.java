package live.lingting.payment.api.log;

import static live.lingting.payment.sdk.constant.SdkConstants.FIELD_KEY;

import com.fasterxml.jackson.core.type.TypeReference;
import com.hccake.ballcat.common.core.request.wrapper.RepeatBodyRequestWrapper;
import com.hccake.ballcat.common.log.constant.LogConstant;
import com.hccake.ballcat.common.util.IpUtils;
import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.MDC;
import org.springframework.http.HttpMethod;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingResponseWrapper;
import live.lingting.payment.api.util.SecurityUtils;
import live.lingting.payment.api.util.UriUtils;
import live.lingting.payment.entity.Project;
import live.lingting.payment.util.JacksonUtils;
import live.lingting.payment.sample.entity.ApiAccessLog;

/**
 * @author lingting 2021/6/25 15:23
 */
@RequiredArgsConstructor
public class LogFilter extends OncePerRequestFilter {

	public static final String WX = "wx";

	public static final String ALI = "ali";

	public static final String CALLBACK = "/callback/";

	private static final Set<String> IGNORE_URIS = new HashSet<>(16);

	static {
		IGNORE_URIS.add("/actuator");
		IGNORE_URIS.add("/actuator/");
		IGNORE_URIS.add("/test");
	}

	private final LogThread thread;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		if (!(request instanceof RepeatBodyRequestWrapper)) {
			request = new RepeatBodyRequestWrapper(request);
		}

		// 包装 response，便于重复获取 body
		if (!(response instanceof ContentCachingResponseWrapper)) {
			response = new ContentCachingResponseWrapper(response);
		}

		long start = System.currentTimeMillis();
		Throwable t = null;

		try {
			filterChain.doFilter(request, response);
		}
		catch (Throwable throwable) {
			t = throwable;
			throw throwable;
		}
		finally {
			// 运行时长. 毫秒
			long runTime = System.currentTimeMillis() - start;

			// 记录在doFilter里被程序处理过后的异常，可参考
			// http://www.runoob.com/servlet/servlet-exception-handling.html
			Throwable throwable = (Throwable) request.getAttribute("javax.servlet.error.exception");
			if (throwable != null) {
				t = throwable;
			}

			generate(request, response, runTime, t);
			// 重新写入数据到响应信息中
			((ContentCachingResponseWrapper) response).copyBodyToResponse();
		}

	}

	@Override
	protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
		return UriUtils.contains(IGNORE_URIS, request.getRequestURI());
	}

	private void generate(HttpServletRequest request, HttpServletResponse response, long runTime, Throwable t) {
		// @formatter:off
		ApiAccessLog al = new ApiAccessLog()
				.setTraceId(MDC.get(LogConstant.TRACE_ID))
				.setTime(runTime)
				.setIp(IpUtils.getIpAddr(request))
				.setMethod(request.getMethod())
				.setUa(request.getHeader("user-agent"))
				.setUri(request.getRequestURI())
				.setErrorMsg(Optional.ofNullable(t).map(Throwable::getMessage).orElse(""))
				.setHttpStatus(response.getStatus());
		// @formatter:on

		al.setReqParams(getParams(request));
		al.setReqBody(getBody(request));
		al.setResult(getResult(response));

		final Project project = SecurityUtils.getProject();
		if (project != null) {
			al.setProjectId(project.getId());
			al.setKey(project.getApiKey());
		}
		else {
			if (al.getUri().startsWith(CALLBACK)) {
				al.setProjectId(getProjectId(al.getUri()));
			}
			else {
				al.setProjectId(0);
				String key;
				try {
					final Map<String, String> map = JacksonUtils.toObj(al.getReqBody(),
							new TypeReference<Map<String, String>>() {
							});
					key = map.getOrDefault(FIELD_KEY, "");
				}
				catch (Exception e) {
					logger.error("[LogFilter] 获取key异常: ", e);
					key = "";
				}
				al.setKey(key);
			}

		}

		thread.put(al);
	}

	private Integer getProjectId(String uri) {
		if (uri.endsWith(WX)) {
			return -2;

		}
		else if (uri.endsWith(ALI)) {
			return -3;
		}
		return -1;
	}

	private String getResult(HttpServletResponse response) {
		try {
			// 获取响应数据
			byte[] contentAsByteArray = ((ContentCachingResponseWrapper) response).getContentAsByteArray();
			return new String(contentAsByteArray, StandardCharsets.UTF_8);
		}
		catch (Exception e) {
			logger.error("[LogFilter] 获取响应数据异常：", e);
		}
		return "";
	}

	private String getBody(HttpServletRequest request) {
		if (!request.getMethod().equals(HttpMethod.GET.name())) {
			try {
				BufferedReader reader = request.getReader();
				if (reader != null) {
					return reader.lines().collect(Collectors.joining(System.lineSeparator()));
				}
			}
			catch (Exception e) {
				logger.error("[LogFilter] Body读取异常：", e);
			}
		}
		return "";
	}

	private String getParams(HttpServletRequest request) {
		try {
			return JacksonUtils.toJson(request.getParameterMap());
		}
		catch (Exception e) {
			logger.error("[LogFilter] Params参数获取序列化异常", e);
			return "";
		}
	}

}
