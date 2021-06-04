package live.lingting.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

/**
 * 项目修改记录
 *
 * @author lingting 2021/6/4 10:49
 */
@Getter
@Setter
@TableName("project_history")
public class ProjectHistory {

	@TableId
	private Integer id;

	@ApiModelProperty("操作人")
	private Integer userId;

	private Integer projectId;

	/**
	 * 项目名
	 */
	private String name;

	/**
	 * 是否禁用
	 */
	private Boolean disabled;

	private String apiKey;

	private String apiSecurity;

	@TableField(fill = FieldFill.INSERT)
	private LocalDateTime createTime;

}
