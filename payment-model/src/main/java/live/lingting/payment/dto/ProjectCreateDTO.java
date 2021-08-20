package live.lingting.payment.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.Set;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Data;
import live.lingting.payment.entity.Project;
import live.lingting.payment.enums.ProjectScope;

/**
 * @author lingting 2021/8/20 16:07
 */
@Data
@ApiModel("项目创建参数")
public class ProjectCreateDTO {

	/**
	 * 项目名
	 */
	@ApiModelProperty("项目名")
	@NotEmpty(message = "项目不能为空!")
	@Size(max = 50, message = "项目名最多使用50个字符!")
	private String name;

	/**
	 * 是否禁用
	 */
	@NotNull(message = "请指定是否禁用!")
	@ApiModelProperty("是否禁用")
	private Boolean disabled;

	@ApiModelProperty("项目标志")
	@NotEmpty(message = "项目标志不能为空!")
	@Size(max = 20, message = "项目标志最大为20个字符!")
	private String mark;

	/**
	 * 项目权限
	 */
	@ApiModelProperty("项目权限")
	@TableField(typeHandler = Project.ScopeTypeHandler.class)
	private Set<ProjectScope> scope;

	public Project toEntity() {
		return new Project().setName(getName()).setMark(getMark()).setDisabled(getDisabled()).setScope(getScope());
	}

}
