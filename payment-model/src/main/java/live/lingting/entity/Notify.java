package live.lingting.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import live.lingting.sdk.enums.NotifyStatus;

/**
 * 通知
 *
 * @author lingting 2021/6/10 16:22
 */
@Getter
@Setter
@TableName("notify")
@Accessors(chain = true)
public class Notify {

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
	 * 通知状态
	 */
	private NotifyStatus status;

	/**
	 * 下次通知时间
	 */
	private LocalDateTime nextTime;

	/**
	 * 通知次数
	 */
	private Integer count;

	@TableField(fill = FieldFill.INSERT)
	private LocalDateTime createTime;

}
