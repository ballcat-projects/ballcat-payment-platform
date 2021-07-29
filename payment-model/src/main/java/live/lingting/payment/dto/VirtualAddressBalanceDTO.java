package live.lingting.payment.dto;

import java.util.List;
import javax.validation.constraints.NotEmpty;
import lombok.Data;

/**
 * @author lingting 2021/7/22 9:40
 */
@Data
public class VirtualAddressBalanceDTO {

	@NotEmpty(message = "至少选择一个地址!")
	private List<Integer> ids;

}
