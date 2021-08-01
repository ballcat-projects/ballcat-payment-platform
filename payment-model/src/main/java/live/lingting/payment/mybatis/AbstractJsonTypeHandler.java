package live.lingting.payment.mybatis;

import com.hccake.ballcat.common.util.JsonUtils;
import com.hccake.ballcat.common.util.json.TypeReference;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandler;
import org.springframework.util.StringUtils;

/**
 * @author lingting 2021/7/16 10:56
 */
public abstract class AbstractJsonTypeHandler<T> implements TypeHandler<T> {

	@Override
	public void setParameter(PreparedStatement ps, int i, T parameter, JdbcType jdbcType) throws SQLException {
		if (parameter == null) {
			ps.setString(i, getDefaultSqlVal());
		}
		else {
			try {
				ps.setString(i, JsonUtils.toJson(parameter));
			}
			catch (Exception e) {
				ps.setString(i, getDefaultSqlVal());
			}
		}
	}

	/**
	 * Java对象的值为空或者序列化出错时使用
	 *
	 * @author lingting 2021-07-16 11:05
	 */
	public String getDefaultSqlVal() {
		return null;
	}

	/**
	 * 数据的值为空或者序列化出错时使用
	 *
	 * @author lingting 2021-07-16 11:05
	 */
	public T getDefaultJavaVal() {
		return null;
	}

	@Override
	public T getResult(ResultSet rs, String columnName) throws SQLException {
		return resolve(rs.getString(columnName));
	}

	@Override
	public T getResult(ResultSet rs, int columnIndex) throws SQLException {
		return resolve(rs.getString(columnIndex));
	}

	@Override
	public T getResult(CallableStatement cs, int columnIndex) throws SQLException {
		return resolve(cs.getString(columnIndex));
	}

	T resolve(String s) {
		if (StringUtils.hasText(s)) {
			try {
				return JsonUtils.toObj(s, getTypeReference());
			}
			catch (Exception e) {
				return getDefaultJavaVal();
			}
		}
		return getDefaultJavaVal();
	}

	public TypeReference<T> getTypeReference() {
		Type superClass = getClass().getGenericSuperclass();
		if (superClass instanceof Class<?>) {
			throw new IllegalArgumentException(
					"Internal error: TypeReference constructed without actual type information");
		}

		return new TypeReference<T>() {
			@Override
			public Type getType() {
				return ((ParameterizedType) superClass).getActualTypeArguments()[0];
			}
		};
	}

}
