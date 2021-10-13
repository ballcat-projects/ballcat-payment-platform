package live.lingting.payment.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import lombok.Data;
import live.lingting.payment.sdk.enums.Chain;

/**
 * @author lingting 2021/6/8 14:38
 */
@Data
@ApiModel("地址新增参数")
public class VirtualAddressCreateDTO {

	@ApiModelProperty("地址是否禁用")
	@NotNull(message = "是否禁用不能为空!")
	private Boolean disabled;

	@ApiModelProperty("地址所属链")
	@NotNull(message = "链不能为空!")
	private Chain chain;

	@ApiModelProperty("可以使用地址的项目ID")
	private List<Integer> projectIds;

	@ApiModelProperty("地址新增详情")
	@NotEmpty(message = "地址不能为空")
	private List<Va> list;

	@Data
	@ApiModel("地址新增详情")
	public static class Va {

		@ApiModelProperty("地址")
		private String address;

		@ApiModelProperty("是否新增成功")
		private Boolean success;

		@ApiModelProperty("描述")
		private String desc;

	}

}
