package live.lingting.dto;

import java.util.List;
import javax.validation.constraints.NotEmpty;
import lombok.Data;

/**
 * @author lingting 2021/7/8 10:58
 */
@Data
public class VirtualAddressProjectIdsDTO {

	@NotEmpty(message = "至少选中一个地址!")
	private List<Integer> ids;

	private List<Integer> projectIds;

}
