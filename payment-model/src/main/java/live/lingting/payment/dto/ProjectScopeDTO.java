package live.lingting.payment.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;
import javax.validation.constraints.NotEmpty;
import lombok.Data;
import live.lingting.payment.enums.ProjectScope;

/**
 * @author lingting 2021/7/7 10:15
 */
@Data
@ApiModel("项目权限配置参数")
public class ProjectScopeDTO {

	@ApiModelProperty("更新权限的项目")
	@NotEmpty(message = "必须选择至少一个项目!")
	private List<Integer> ids;

	@ApiModelProperty("新权限")
	private List<ProjectScope> scopes;

}
