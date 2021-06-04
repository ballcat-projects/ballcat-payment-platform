package live.lingting.service.impl;

import com.hccake.extend.mybatis.plus.service.impl.ExtendServiceImpl;
import org.springframework.stereotype.Service;
import live.lingting.entity.Project;
import live.lingting.mapper.ProjectMapper;
import live.lingting.service.ProjectService;

/**
 * @author lingting 2021/6/4 13:38
 */
@Service
public class ProjectServiceImpl extends ExtendServiceImpl<ProjectMapper, Project> implements ProjectService {

}
