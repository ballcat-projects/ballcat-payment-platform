package live.lingting.payment.dto;

import java.util.List;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import lombok.Data;
import live.lingting.payment.enums.VirtualAddressMode;

/**
 * @author lingting 2021/7/8 10:58
 */
@Data
public class VirtualAddressModeDTO {

	@NotEmpty(message = "至少选中一个地址!")
	private List<Integer> ids;

	@NotNull(message = "请选择新模式")
	private VirtualAddressMode mode;

}
