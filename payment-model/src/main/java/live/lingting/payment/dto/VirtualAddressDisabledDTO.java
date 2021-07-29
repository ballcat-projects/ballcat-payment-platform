package live.lingting.payment.dto;

import java.util.List;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import lombok.Data;

/**
 * @author lingting 2021/7/8 10:58
 */
@Data
public class VirtualAddressDisabledDTO {

	@NotEmpty(message = "至少选中一个地址!")
	private List<Integer> ids;

	@NotNull(message = "请选择是否禁用")
	private Boolean disabled;

}
