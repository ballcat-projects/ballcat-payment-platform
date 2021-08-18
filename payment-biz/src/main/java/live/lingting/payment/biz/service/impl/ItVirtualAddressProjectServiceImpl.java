package live.lingting.payment.biz.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import java.util.List;
import org.springframework.stereotype.Service;
import live.lingting.payment.biz.mapper.ItVirtualAddressProjectMapper;
import live.lingting.payment.biz.mybatis.WrappersX;
import live.lingting.payment.biz.mybatis.conditions.LambdaQueryWrapperX;
import live.lingting.payment.biz.service.ItVirtualAddressProjectService;
import live.lingting.payment.entity.ItVirtualAddressProject;

/**
 * @author lingting 2021/8/18 15:26
 */
@Service
public class ItVirtualAddressProjectServiceImpl extends
		ServiceImpl<ItVirtualAddressProjectMapper, ItVirtualAddressProject> implements ItVirtualAddressProjectService {

	@Override
	public int removeByVa(Integer vaId) {
		LambdaQueryWrapperX<ItVirtualAddressProject> wrapperX = WrappersX.<ItVirtualAddressProject>lambdaQueryX()

				.eq(ItVirtualAddressProject::getVaId, vaId);

		return baseMapper.delete(wrapperX);
	}

	@Override
	public void insert(Integer vaId, List<Integer> projectIds) {
		for (Integer pId : projectIds) {
			try {
				save(new ItVirtualAddressProject().setVaId(vaId).setProjectId(pId));
			}
			catch (Exception ignored) {
				// empty
			}
		}
	}

}
