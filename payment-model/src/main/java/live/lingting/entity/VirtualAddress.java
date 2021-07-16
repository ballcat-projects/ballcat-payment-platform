package live.lingting.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import live.lingting.enums.VirtualAddressMode;
import live.lingting.mybatis.AbstractJsonTypeHandler;
import live.lingting.sdk.enums.Chain;

/**
 * @author lingting 2021/6/7 15:37
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName(value = "virtual_address", autoResultMap = true)
public class VirtualAddress {

	@TableId
	private Integer id;

	private Chain chain;

	private String address;

	/**
	 * 是否禁用
	 */
	private Boolean disabled;

	/**
	 * 是否使用中
	 */
	@TableField("`using`")
	private Boolean using;

	private VirtualAddressMode mode;

	@TableField(typeHandler = ProjectIdsTypeHandler.class)
	private List<Integer> projectIds;

	@TableField(fill = FieldFill.INSERT)
	private LocalDateTime createTime;

	@MappedJdbcTypes(JdbcType.VARCHAR)
	public static class ProjectIdsTypeHandler extends AbstractJsonTypeHandler<List<Integer>> {

		@Override
		public List<Integer> getDefaultJavaVal() {
			return new ArrayList<>();
		}

	}

}
