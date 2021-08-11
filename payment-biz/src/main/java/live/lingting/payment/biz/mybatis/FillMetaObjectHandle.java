package live.lingting.payment.biz.mybatis;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import java.time.LocalDateTime;
import org.apache.ibatis.reflection.MetaObject;

/**
 * @author lingting 2021/8/11 16:57
 */
public class FillMetaObjectHandle implements MetaObjectHandler {

	@Override
	public void insertFill(MetaObject metaObject) {
		// 逻辑删除标识
		this.strictInsertFill(metaObject, "deleted", Long.class, 0L);
		// 创建时间
		this.strictInsertFill(metaObject, "createTime", LocalDateTime.class, LocalDateTime.now());
	}

	@Override
	public void updateFill(MetaObject metaObject) {
		// 修改时间
		this.strictUpdateFill(metaObject, "updateTime", LocalDateTime.class, LocalDateTime.now());
	}

}
