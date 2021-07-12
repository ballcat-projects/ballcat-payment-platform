package live.lingting.sdk.request;

import com.hccake.ballcat.common.util.json.TypeReference;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import lombok.Getter;
import lombok.Setter;
import live.lingting.sdk.http.HttpProperties;
import live.lingting.sdk.exception.MixRequestParamsValidException;
import live.lingting.sdk.model.MixModel;
import live.lingting.sdk.response.MixResponse;
import live.lingting.sdk.util.JacksonUtils;

/**
 * @author lingting 2021/6/7 17:33
 */
@Getter
@Setter
public abstract class AbstractMixRequest<M extends MixModel, R extends MixResponse<?>> implements MixRequest<M, R> {

	private static final Map<Class<?>, Type> CACHE = new ConcurrentHashMap<>(16);

	private M model;

	@Override
	public Map<String, String> getParams() throws MixRequestParamsValidException {
		final M m = getModel();

		if (m == null) {
			throw new MixRequestParamsValidException("参数基础数据为空!");
		}

		return JacksonUtils.toObj(JacksonUtils.toJson(m), new TypeReference<Map<String, String>>() {
		});
	}

	@Override
	public HttpProperties getProperties() {
		return new HttpProperties();
	}

	@Override
	public R convert(String resStr) {
		final Class<?> tc = this.getClass();

		CACHE.computeIfAbsent(tc, c -> {
			final Type superclass = c.getGenericSuperclass();
			if (superclass instanceof Class) {
				throw new IllegalArgumentException(
						"Internal error: TypeReference constructed without actual type information");
			}
			return ((ParameterizedType) superclass).getActualTypeArguments()[1];
		});

		return JacksonUtils.toObj(resStr, new TypeReference<R>() {
			@Override
			public Type getType() {
				return CACHE.get(tc);
			}
		});
	}

}
