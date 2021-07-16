package live.lingting.dto;

import java.util.List;
import javax.validation.constraints.NotEmpty;
import lombok.Data;
import live.lingting.enums.ProjectScope;

/**
 * @author lingting 2021/7/7 10:15
 */
@Data
public class ProjectScopeDTO {

	@NotEmpty(message = "必须选择至少一个项目!")
	private List<Integer> ids;

	private List<ProjectScope> scopes;

}
