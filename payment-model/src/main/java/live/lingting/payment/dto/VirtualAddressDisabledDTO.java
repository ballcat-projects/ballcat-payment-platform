package live.lingting.payment.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import lombok.Data;

/**
 * @author lingting 2021/7/8 10:58
 */
@Data
@ApiModel("地址禁用/启用参数")
public class VirtualAddressDisabledDTO {

	@ApiModelProperty("选中的地址id")
	@NotEmpty(message = "至少选中一个地址!")
	private List<Integer> ids;

	@ApiModelProperty("是否禁用选中的地址")
	@NotNull(message = "请选择是否禁用")
	private Boolean disabled;

}
