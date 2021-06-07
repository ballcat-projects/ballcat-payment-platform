package live.lingting.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import live.lingting.enums.NotifyStatus;

/**
 * @author lingting 2021/6/4 13:22
 */
@Getter
@Setter
@TableName("notify_log")
@Accessors(chain = true)
public class NotifyLog {

	@TableId
	private Long id;

	/**
	 * 所属项目
	 */
	private Integer projectId;

	/**
	 * 交易号
	 */
	private String tradeNo;

	private String notifyUrl;

	/**
	 * 请求参数
	 */
	private String params;

	/**
	 * 请求状态
	 */
	private Integer httpStatus;

	/**
	 * 请求返回值
	 */
	private String res;

	/**
	 * 通知时间
	 */
	private LocalDateTime time;

	/**
	 * 通知状态
	 */
	private NotifyStatus status;

	@TableField(fill = FieldFill.INSERT)
	private LocalDateTime createTime;

}
