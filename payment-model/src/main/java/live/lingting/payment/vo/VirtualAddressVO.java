package live.lingting.payment.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import live.lingting.payment.mybatis.AbstractJsonTypeHandler;
import live.lingting.payment.sdk.enums.Chain;

/**
 * @author lingting 2021/8/16 17:35
 */
@Data
@ApiModel("地址列表返回")
public class VirtualAddressVO {

	@ApiModelProperty("地址id")
	private Integer id;

	@ApiModelProperty("链")
	private Chain chain;

	@ApiModelProperty("地址")
	private String address;

	@ApiModelProperty("是否禁用")
	private Boolean disabled;

	@ApiModelProperty("是否使用中")
	private Boolean using;

	@ApiModelProperty("USDT 余额")
	private BigDecimal usdtAmount;

	@ApiModelProperty("地址只允许给指定的项目使用")
	private List<Integer> projectIds;

	private List<String> projectNames;

	private LocalDateTime createTime;

	public static class ProjectIdsTypeHandler extends AbstractJsonTypeHandler<List<Integer>> {

		@Override
		public List<Integer> getDefaultJavaVal() {
			return new ArrayList<>();
		}

	}

	public static class ProjectNamesTypeHandler extends AbstractJsonTypeHandler<List<String>> {

		@Override
		public List<String> getDefaultJavaVal() {
			return new ArrayList<>();
		}

	}

}
