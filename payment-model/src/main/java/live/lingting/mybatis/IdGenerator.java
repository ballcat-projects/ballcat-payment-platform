package live.lingting.mybatis;

import cn.hutool.core.annotation.AnnotationUtil;
import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.core.incrementer.IdentifierGenerator;
import com.hccake.ballcat.common.util.ClassUtils;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 自定义主键生成
 *
 * @author lingting 2021/4/7 15:25
 */
@Slf4j
@Component
public class IdGenerator implements IdentifierGenerator {

	/**
	 * 长度小于等于多少就转为long
	 */
	private static final Integer LONG_LENGTH = 19;

	private static final Snowflake SNOWFLAKE = IdUtil.createSnowflake(1, 1);

	/**
	 * 缓存结果
	 */
	private static final Map<Class<?>, String> MAP = new HashMap<>(64);

	static {
		put("live.lingting.entity.Pay", "11");
	}

	private static void put(Class<?> c, String prefix) {
		MAP.put(c, prefix);
	}

	@SneakyThrows
	private static void put(String className, String prefix) {
		if (ClassUtils.isPresent(className, IdGenerator.class.getClassLoader())) {
			put(Class.forName(className), prefix);
		}
		else {
			log.error("初始化类: {} 的id前缀异常!", className);
		}
	}

	/**
	 * 自定义 要求使用雪花算法生成的id
	 * @author lingting 2021-04-07 15:44
	 */
	@Override
	public Number nextId(Object entity) {
		final String uuid = nextUUID(entity);

		if (uuid == null) {
			return null;
		}

		if (uuid.length() <= LONG_LENGTH) {
			return Long.parseLong(uuid);
		}
		return new BigInteger(uuid, 10);
	}

	@Override
	public String nextUUID(Object entity) {
		// 表名 hash 后取 LENGTH 位
		final String hash = getTableHash(entity);

		return hash
				// 雪花算法 生成
				+ SNOWFLAKE.nextId();
	}

	private String getTableHash(Object entity) {
		if (MAP.containsKey(entity.getClass())) {
			return MAP.get(entity.getClass());
		}
		TableName tableName = AnnotationUtil.getAnnotation(entity.getClass(), TableName.class);
		int hashCode = tableName.value().hashCode();
		// 初始
		String hash = String.valueOf(hashCode).substring(0, 1);
		MAP.put(entity.getClass(), hash);
		return hash;

	}

}
