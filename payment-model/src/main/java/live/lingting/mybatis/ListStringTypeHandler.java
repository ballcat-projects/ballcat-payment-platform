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
import org.apache.ibatis.type.TypeHandler;
import org.springframework.util.StringUtils;

/**
 * @author lingting 2021/7/5 20:16
 */
public class ListStringTypeHandler implements TypeHandler<List<String>> {

	@Override
	public void setParameter(PreparedStatement ps, int i, List<String> parameter, JdbcType jdbcType)
			throws SQLException {
		try {
			ps.setString(i, JsonUtils.toJson(parameter));
		}
		catch (Exception e) {
			ps.setString(i, "[]");
		}
	}

	@Override
	public List<String> getResult(ResultSet rs, String columnName) throws SQLException {
		return resolve(rs.getString(columnName));
	}

	@Override
	public List<String> getResult(ResultSet rs, int columnIndex) throws SQLException {
		return resolve(rs.getString(columnIndex));
	}

	@Override
	public List<String> getResult(CallableStatement cs, int columnIndex) throws SQLException {
		return resolve(cs.getString(columnIndex));
	}

	List<String> resolve(String s) {
		if (StringUtils.hasText(s)) {
			try {
				return JsonUtils.toObj(s, new TypeReference<List<String>>() {
				});
			}
			catch (Exception e) {
				return new ArrayList<>();
			}
		}
		return new ArrayList<>();
	}

}
