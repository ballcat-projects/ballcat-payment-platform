package live.lingting.payment.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;
import javax.validation.constraints.NotEmpty;
import lombok.Data;

/**
 * @author lingting 2021/7/8 10:58
 */
@Data
@ApiModel("地址更新可以使用的项目ID")
public class VirtualAddressProjectIdsDTO {

	@ApiModelProperty("选中的地址id")
	@NotEmpty(message = "至少选中一个地址!")
	private List<Integer> ids;

	@ApiModelProperty("新的可以使用的项目id")
	private List<Integer> projectIds;

}
