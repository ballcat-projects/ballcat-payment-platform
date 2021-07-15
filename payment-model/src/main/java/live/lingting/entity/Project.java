package live.lingting.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import live.lingting.enums.ProjectMode;

/**
 * 项目
 *
 * @author lingting 2021/6/4 0:42
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("project")
public class Project implements Serializable {

	private static final long serialVersionUID = 1L;

	@TableId
	private Integer id;

	/**
	 * 项目名
	 */
	@Size(max = 50, message = "项目名最多使用50个字符!")
	private String name;

	/**
	 * 是否禁用
	 */
	private Boolean disabled;

	private String apiKey;

	private String apiSecurity;

	@NotNull(message = "模式不能为空")
	private ProjectMode mode;

	@Size(max = 20, message = "项目标志最大为20个字符!")
	@NotEmpty(message = "项目标志不能为空!")
	private String mark;

	@TableField(fill = FieldFill.INSERT)
	private LocalDateTime createTime;

}
