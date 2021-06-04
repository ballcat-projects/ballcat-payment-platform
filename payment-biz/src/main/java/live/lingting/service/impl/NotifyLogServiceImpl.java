package live.lingting.service.impl;

import com.hccake.extend.mybatis.plus.service.impl.ExtendServiceImpl;
import org.springframework.stereotype.Service;
import live.lingting.entity.NotifyLog;
import live.lingting.mapper.NotifyLogMapper;
import live.lingting.service.NotifyLogService;

/**
 * @author lingting 2021/6/4 13:42
 */
@Service
public class NotifyLogServiceImpl extends ExtendServiceImpl<NotifyLogMapper, NotifyLog> implements NotifyLogService {

}
