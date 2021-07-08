package live.lingting.mybatis;

import com.hccake.ballcat.common.util.JsonUtils;
import com.hccake.ballcat.common.util.json.TypeReference;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.TypeHandler;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

/**
 * @author lingting 2021/7/5 20:16
 */
@MappedJdbcTypes(JdbcType.VARCHAR)
public class ListIntegerToJsonTypeHandler implements TypeHandler<List<Integer>> {

	@Override
	public void setParameter(PreparedStatement ps, int i, List<Integer> parameter, JdbcType jdbcType)
			throws SQLException {
		String val = "[]";
		try {
			if (!CollectionUtils.isEmpty(parameter)) {
				val = JsonUtils.toJson(parameter);
			}
		}
		catch (Exception e) {
			val = "[]";
		}
		ps.setString(i, val);
	}

	@Override
	public List<Integer> getResult(ResultSet rs, String columnName) throws SQLException {
		return resolve(rs.getString(columnName));
	}

	@Override
	public List<Integer> getResult(ResultSet rs, int columnIndex) throws SQLException {
		return resolve(rs.getString(columnIndex));
	}

	@Override
	public List<Integer> getResult(CallableStatement cs, int columnIndex) throws SQLException {
		return resolve(cs.getString(columnIndex));
	}

	List<Integer> resolve(String s) {
		if (StringUtils.hasText(s)) {
			try {
				return JsonUtils.toObj(s, new TypeReference<List<Integer>>() {
				});
			}
			catch (Exception e) {
				return new ArrayList<>();
			}
		}
		return new ArrayList<>();
	}

}
