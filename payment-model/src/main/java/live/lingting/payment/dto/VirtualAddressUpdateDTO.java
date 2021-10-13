package live.lingting.payment.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author lingting 2021/6/8 14:38
 */
@Data
@ApiModel("地址修改参数")
public class VirtualAddressUpdateDTO {

	@NotNull(message = "地址id不能为空!")
	@ApiModelProperty("地址id")
	private Integer id;

	@ApiModelProperty("地址是否禁用")
	@NotNull(message = "是否禁用不能为空!")
	private Boolean disabled;

	@ApiModelProperty("可以使用地址的项目ID")
	private List<Integer> projectIds;

}
