package live.lingting.dto;

import java.util.List;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import lombok.Data;
import live.lingting.enums.VirtualAddressMode;
import live.lingting.sdk.enums.Chain;

/**
 * @author lingting 2021/6/8 14:38
 */
@Data
public class VirtualAddressCreateDTO {

	@NotNull(message = "是否禁用不能为空!")
	private Boolean disabled;

	@NotNull(message = "链不能为空!")
	private Chain chain;

	@NotNull(message = "模式不能为空!")
	private VirtualAddressMode mode;

	private List<String> ids;

	@NotEmpty(message = "地址不能为空")
	private List<Va> list;

	@Data
	public static class Va {

		private String address;

		private Boolean success;

		private String desc;

	}

}
