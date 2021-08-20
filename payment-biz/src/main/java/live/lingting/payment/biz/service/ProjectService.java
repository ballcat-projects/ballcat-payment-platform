package live.lingting.payment.biz.service;

import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;
import live.lingting.payment.Page;
import live.lingting.payment.dto.ProjectCreateDTO;
import live.lingting.payment.entity.Project;
import live.lingting.payment.enums.ProjectScope;
import live.lingting.payment.exception.PaymentException;

/**
 * @author lingting 2021/6/4 13:36
 */
public interface ProjectService extends IService<Project> {

	/**
	 * 搜索
	 * @param page 分页
	 * @param project 项目
	 * @return live.lingting.payment.Page<live.lingting.payment.entity.Project>
	 * @author lingting 2021-06-04 16:32
	 */
	Page<Project> list(Page<Project> page, Project project);

	/**
	 * 新增项目
	 * @param project 项目
	 * @author lingting 2021-06-04 17:15
	 */
	void create(ProjectCreateDTO  project);

	/**
	 * 重置API
	 * @param id 项目 Id
	 * @param userId 修改人
	 * @author lingting 2021-06-04 17:19
	 * @throws PaymentException 异常
	 */
	void resetApi(Integer id, Integer userId) throws PaymentException;

	/**
	 * 更新禁用信息
	 * @param id id
	 * @param disabled 是否禁用
	 * @param userId 修改人
	 * @author lingting 2021-06-04 17:26
	 * @throws PaymentException 异常
	 */
	void disabled(Integer id, Boolean disabled, Integer userId) throws PaymentException;

	/**
	 * 根据apiKey 获取项目信息
	 * @param key key
	 * @return live.lingting.payment.entity.Project
	 * @author lingting 2021-06-07 16:39
	 */
	Project getByApiKey(String key);

	/**
	 * 更新项目权限
	 * @param ids id
	 * @param scopes 新权限
	 * @author lingting 2021-07-16 16:25
	 */
	void scope(List<Integer> ids, List<ProjectScope> scopes);

}
