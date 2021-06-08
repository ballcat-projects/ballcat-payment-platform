package live.lingting.api.filter;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.StrUtil;
import com.hccake.ballcat.common.core.request.wrapper.RepeatBodyRequestWrapper;
import com.hccake.ballcat.common.util.JsonUtils;
import com.hccake.ballcat.common.util.json.TypeReference;
import java.io.IOException;
import java.util.Base64;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.token.store.redis.JdkSerializationStrategy;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStoreSerializationStrategy;
import org.springframework.web.filter.OncePerRequestFilter;
import live.lingting.Redis;
import live.lingting.api.enums.ApiResponseCode;
import live.lingting.api.util.HttpUtils;
import live.lingting.api.util.UriUtils;
import live.lingting.entity.Project;
import live.lingting.sdk.MixPay;
import live.lingting.sdk.constant.SdkConstants;
import live.lingting.sdk.util.StreamUtils;
import live.lingting.service.ProjectService;

/**
 * 过滤url
 *
 * @author lingting 2021/4/29 15:30
 */
public class SignFilter extends OncePerRequestFilter {

	/**
	 * 指定时长内, 相同 uri 相同key 相同 nonce 参数的请求会被判定为重复请求, 直接拒绝
	 */
	public static final Long LOCK_TIME = TimeUnit.SECONDS.toSeconds(5);

	public static final String LOCK_PREFIX = "api_request_lock";

	/**
	 * 缓存指定时间的oauth信息
	 */
	public static final Long OAUTH_TIME = TimeUnit.HOURS.toSeconds(1);

	public static final String OAUTH_PREFIX = "api_request_oauth";

	private final Set<String> allowUris = new HashSet<>(16);

	private final ProjectService projectService;

	private final Redis redis;

	private final RedisTokenStoreSerializationStrategy tokenSerialization = new JdkSerializationStrategy();

	@SneakyThrows
	public SignFilter(ProjectService projectService, Redis redis) {
		this.projectService = projectService;
		this.redis = redis;
		allowUris.add("/actuator");
		allowUris.add("/actuator/");
	}

	@SneakyThrows
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		if (!(request instanceof RepeatBodyRequestWrapper)) {
			request = new RepeatBodyRequestWrapper(request);
		}

		Map<String, String> params = getParams(request);
		// 没有 sign 参数 或 key 参数 或 nonce 参数, 直接判定签名错误
		if (!params.containsKey(SdkConstants.FIELD_SIGN) || !params.containsKey(SdkConstants.FIELD_KEY)
				|| !params.containsKey(SdkConstants.FIELD_NONCE)) {
			signFail(response);
			return;
		}

		final String key = Convert.toStr(params.get(SdkConstants.FIELD_KEY));
		final String nonce = Convert.toStr(params.get(SdkConstants.FIELD_NONCE));

		// TIME 时间内, 相同 uri 相同key 相同 nonce 参数的请求会被判定为重复请求, 直接拒绝
		if (!lock(request.getRequestURI(), key, nonce)) {
			// 上锁失败, 拒绝
			lockFail(response);
			return;
		}

		Project project = projectService.getByApiKey(key);

		if (project.getDisabled()) {
			// 用户状态异常
			HttpUtils.write(response, HttpStatus.BAD_REQUEST, ApiResponseCode.DISABLED);
			return;
		}

		final boolean verify = MixPay.verifySign(project.getApiSecurity(), params);

		if (verify) {
			// 生成要 注入 oauth 的信息
			Authentication authentication = generate(request, project);
			// 注入 oauth 信息
			SecurityContextHolder.getContext().setAuthentication(authentication);
			// 验签通过
			filterChain.doFilter(request, response);
		}
		else {
			if (logger.isDebugEnabled()) {
				logger.debug(StrUtil.format("验签失败参数: {}", JsonUtils.toJson(params)));
			}
			// 未通过
			signFail(response);
		}
	}

	/**
	 * 生成 UserDetails
	 * @author lingting 2021-04-30 10:42
	 */
	@SneakyThrows
	private Authentication generate(HttpServletRequest request, Project project) {
		final String key = StrUtil.format("{}_{}", OAUTH_PREFIX, project.getApiKey());

		if (redis.hasKey(key)) {
			return deserialize(key, redis.get(key));
		}

		// 生成用于注入的数据
		final Authentication authentication = toAuthentication(key, project);
		// 鉴权信息序列化
		String val = serialize(project);
		// 缓存info, 一小时后过期
		redis.set(key, val, OAUTH_TIME);
		return authentication;
	}

	private Authentication toAuthentication(String key, Object obj) {
		return new AnonymousAuthenticationToken(key, obj,
				Collections.singleton(new SimpleGrantedAuthority("ROLE_ADMIN")));
	}

	private Authentication deserialize(String key, String serializeStr) {
		byte[] serialize = Base64.getDecoder().decode(serializeStr);
		return toAuthentication(key, tokenSerialization.deserialize(serialize, Project.class));
	}

	private String serialize(Object obj) {
		final byte[] serialize = tokenSerialization.serialize(obj);
		return Base64.getEncoder().encodeToString(serialize);
	}

	/**
	 * 从http请求中获取参数信息
	 * @author lingting 2021-04-30 10:29
	 */
	private Map<String, String> getParams(HttpServletRequest request) throws IOException {
		// 获取参数
		Map<String, String> params = new HashMap<>(16);
		final String contentType = request.getHeader("Content-Type");

		if (StrUtil.isNotBlank(contentType) && contentType.startsWith(MediaType.APPLICATION_JSON_VALUE)) {
			params = JsonUtils.toObj(StreamUtils.toString(request.getInputStream()),
					new TypeReference<Map<String, String>>() {
					});
		}
		else {
			final Enumeration<String> names = request.getParameterNames();
			while (names.hasMoreElements()) {
				final String element = names.nextElement();
				params.put(element, request.getParameter(element));
			}
		}
		return params;
	}

	@Override
	protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
		return UriUtils.contains(allowUris, request.getRequestURI());
	}

	private boolean lock(String uri, String key, String nonce) {
		return redis.setIfAbsent(StrUtil.format("{}_{}_{}_{}", LOCK_PREFIX, uri, key, nonce), StrUtil.EMPTY, LOCK_TIME);
	}

	private void lockFail(HttpServletResponse response) throws IOException {
		HttpUtils.write(response, HttpStatus.NOT_ACCEPTABLE, ApiResponseCode.REPEAT_REQUEST);
	}

	private void signFail(HttpServletResponse response) throws IOException {
		HttpUtils.write(response, HttpStatus.UNAUTHORIZED, ApiResponseCode.SIGN_ERROR);
	}

}
