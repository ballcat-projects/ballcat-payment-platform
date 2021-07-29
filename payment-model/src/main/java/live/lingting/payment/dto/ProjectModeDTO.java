package live.lingting.payment.dto;

import java.util.List;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import lombok.Data;
import live.lingting.payment.enums.ProjectMode;

/**
 * @author lingting 2021/7/7 10:15
 */
@Data
public class ProjectModeDTO {

	@NotEmpty(message = "必须选择至少一个项目!")
	private List<Integer> ids;

	@NotNull(message = "请选择新模式!")
	private ProjectMode mode;

}
