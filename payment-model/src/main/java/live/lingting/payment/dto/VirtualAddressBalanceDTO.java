package live.lingting.payment.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;
import javax.validation.constraints.NotEmpty;
import lombok.Data;

/**
 * @author lingting 2021/7/22 9:40
 */
@Data
@ApiModel("地址余额更新参数")
public class VirtualAddressBalanceDTO {

	@ApiModelProperty("更新余额的地址")
	@NotEmpty(message = "至少选择一个地址!")
	private List<Integer> ids;

}
