package live.lingting.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * @author lingting 2021/6/25 15:29
 */
@Getter
@Setter
@ToString
@TableName("api_access_log")
@Accessors(chain = true)
public class ApiAccessLog {

	@TableId
	private Long id;

	/**
	 * 追踪ID
	 */
	@ApiModelProperty(value = "追踪ID")
	private String traceId;

	/**
	 * 项目ID
	 */
	@ApiModelProperty(value = "项目ID")
	private Integer projectId;

	/**
	 * api key
	 */
	@TableField("`key`")
	private String key;

	/**
	 * 访问IP地址
	 */
	@ApiModelProperty(value = "访问IP地址")
	private String ip;

	/**
	 * 用户代理
	 */
	@ApiModelProperty(value = "UA")
	private String ua;

	/**
	 * 请求URI
	 */
	@ApiModelProperty(value = "请求URI")
	private String uri;

	/**
	 * 操作方式
	 */
	@ApiModelProperty(value = "操作方式")
	private String method;

	/**
	 * 请求参数
	 */
	@ApiModelProperty(value = "请求参数")
	private String reqParams;

	/**
	 * 请求body
	 */
	@ApiModelProperty(value = "请求body")
	private String reqBody;

	/**
	 * 响应状态码
	 */
	@ApiModelProperty(value = "响应状态码")
	private Integer httpStatus;

	/**
	 * 响应信息
	 */
	@ApiModelProperty(value = "响应信息")
	private String result;

	/**
	 * 错误消息
	 */
	@ApiModelProperty(value = "错误消息")
	private String errorMsg;

	/**
	 * 执行时长
	 */
	@ApiModelProperty(value = "执行时长")
	private Long time;

	/**
	 * 创建时间
	 */
	@TableField(fill = FieldFill.INSERT)
	@ApiModelProperty(value = "创建时间")
	private LocalDateTime createTime;

}
