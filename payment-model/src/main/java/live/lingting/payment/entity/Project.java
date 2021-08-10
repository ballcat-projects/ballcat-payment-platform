package live.lingting.payment.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import live.lingting.payment.enums.ProjectScope;
import live.lingting.payment.mybatis.AbstractJsonTypeHandler;

/**
 * 项目
 *
 * @author lingting 2021/6/4 0:42
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName(value = "lingting_payment_project", autoResultMap = true)
public class Project implements Serializable {

	private static final long serialVersionUID = 1L;

	@TableId
	private Integer id;

	/**
	 * 项目名
	 */
	@Size(max = 50, message = "项目名最多使用50个字符!")
	private String name;

	/**
	 * 是否禁用
	 */
	private Boolean disabled;

	private String apiKey;

	private String apiSecurity;

	@Size(max = 20, message = "项目标志最大为20个字符!")
	@NotEmpty(message = "项目标志不能为空!")
	private String mark;

	/**
	 * 项目权限
	 */
	@TableField(typeHandler = ScopeTypeHandler.class)
	private Set<ProjectScope> scope;

	@TableField(fill = FieldFill.INSERT)
	private LocalDateTime createTime;

	public static class ScopeTypeHandler extends AbstractJsonTypeHandler<Set<ProjectScope>> {

		@Override
		public Set<ProjectScope> getDefaultJavaVal() {
			return new HashSet<>();
		}

	}

}
