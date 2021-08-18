package live.lingting.payment.biz.service;

import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;
import live.lingting.payment.entity.ItVirtualAddressProject;

/**
 * @author lingting 2021/8/18 15:26
 */
public interface ItVirtualAddressProjectService extends IService<ItVirtualAddressProject> {

	/**
	 * 根据地址id删除关联
	 * @param vaId 地址id
	 * @return int
	 * @author lingting 2021-08-18 15:27
	 */
	int removeByVa(Integer vaId);

	/**
	 * 插入多个关联
	 * @param vaId 地址id
	 * @param projectIds 项目id
	 * @author lingting 2021-08-18 15:28
	 */
	void insert(Integer vaId, List<Integer> projectIds);

}
