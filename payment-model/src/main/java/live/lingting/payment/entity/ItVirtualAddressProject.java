package live.lingting.payment.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * @author lingting 2021/8/18 15:11
 */
@Getter
@Setter
@Accessors(chain = true)
@ApiModel("中间表:虚拟货币地址-项目")
@TableName(value = "lingting_payment_virtual_address_project")
public class ItVirtualAddressProject {

	@TableId
	@ApiModelProperty("关联id")
	private Integer id;

	@ApiModelProperty("地址id")
	private Integer vaId;

	@ApiModelProperty("项目id")
	private Integer projectId;

}
