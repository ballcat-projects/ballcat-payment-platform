package live.lingting.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import live.lingting.sdk.enums.Chain;

/**
 * @author lingting 2021/6/7 15:37
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("virtual_address")
public class VirtualAddress {

	@TableId
	private Integer id;

	private Chain chain;

	private String address;

	/**
	 * 是否禁用
	 */
	private Boolean disabled;

	/**
	 * 是否使用中
	 */
	private Boolean using;

	@TableField(fill = FieldFill.INSERT)
	private LocalDateTime createTime;

}
