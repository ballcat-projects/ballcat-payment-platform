package live.lingting.payment.biz.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import java.util.Collections;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import live.lingting.payment.Page;
import live.lingting.payment.biz.mapper.VirtualAddressMapper;
import live.lingting.payment.biz.mybatis.WrappersX;
import live.lingting.payment.biz.service.ItVirtualAddressProjectService;
import live.lingting.payment.biz.service.VirtualAddressService;
import live.lingting.payment.biz.virtual.VirtualHandler;
import live.lingting.payment.dto.VirtualAddressBalanceDTO;
import live.lingting.payment.dto.VirtualAddressCreateDTO;
import live.lingting.payment.entity.Project;
import live.lingting.payment.entity.VirtualAddress;
import live.lingting.payment.sdk.model.MixVirtualPayModel;
import live.lingting.payment.vo.VirtualAddressVO;

/**
 * @author lingting 2021/6/7 15:43
 */
@Service
@RequiredArgsConstructor
public class VirtualAddressServiceImpl extends ServiceImpl<VirtualAddressMapper, VirtualAddress>
		implements VirtualAddressService {

	private static final Integer SHUFFLE_MIN = 3;

	private final VirtualHandler handler;

	private final ItVirtualAddressProjectService service;

	@Override
	public VirtualAddress lock(MixVirtualPayModel model, Project project) {
		final List<VirtualAddress> list = baseMapper.load(model.getChain(), project.getId());
		// 乱序
		if (list.size() > SHUFFLE_MIN) {
			Collections.shuffle(list);
		}
		for (VirtualAddress va : list) {
			// 锁
			if (baseMapper.lock(va)) {
				va.setUsing(true);
				return va;
			}
		}
		return null;
	}

	@Override
	public boolean unlock(String address) {
		return baseMapper.unlock(address);
	}

	@Override
	public Page<VirtualAddress> list(Page<VirtualAddress> page, VirtualAddress qo) {
		return baseMapper.list(page, qo);
	}

	@Override
	public Page<VirtualAddressVO> listVo(Page<VirtualAddress> page, VirtualAddress qo) {
		Wrapper<VirtualAddress> wrapper = WrappersX.<VirtualAddress>lambdaQueryX()
				// address
				.eqIfPresent(VirtualAddress::getAddress, qo.getAddress())
				// chain
				.eqIfPresent(VirtualAddress::getChain, qo.getChain())
				// disabled
				.eqIfPresent(VirtualAddress::getDisabled, qo.getDisabled())
				// using
				.eqIfPresent(VirtualAddress::getUsing, qo.getUsing());

		IPage<VirtualAddressVO> iPage = baseMapper.listVo(page.toPage(), wrapper);
		return new Page<>(iPage.getRecords(), iPage.getTotal());
	}

	@Override
	public void disabled(List<Integer> ids, Boolean disabled) {
		baseMapper.disabled(ids, disabled);
	}

	@Override
	public VirtualAddressCreateDTO create(VirtualAddressCreateDTO dto) {
		for (VirtualAddressCreateDTO.Va address : dto.getList()) {
			address.setSuccess(false);
			final VirtualAddress va = new VirtualAddress().setAddress(address.getAddress()).setChain(dto.getChain());
			if (!handler.valid(va)) {
				address.setDesc("无效地址!");
				continue;
			}

			if (baseMapper.selectCount(baseMapper.getWrapper(va)) > 0) {
				address.setDesc("已存在相同地址!");
				continue;
			}

			try {
				va.setDisabled(dto.getDisabled());
				address.setSuccess(save(va));
				if (!address.getSuccess()) {
					address.setDesc("保存失败!");
				}
				// 添加关联
				service.insert(va.getId(), dto.getProjectIds());
			}
			catch (Exception e) {
				address.setSuccess(false);
				address.setDesc("保存异常!");
			}
		}

		return dto;
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void project(List<Integer> ids, List<Integer> projectIds) {
		for (Integer id : ids) {
			service.removeByVa(id);
			service.insert(id, projectIds);
		}
	}

	@Override
	public void balance(VirtualAddressBalanceDTO dto) {
		List<VirtualAddress> list;
		if (CollectionUtils.isEmpty(dto.getIds())) {
			list = list();
		}
		else {
			list = listByIds(dto.getIds());
		}

		for (VirtualAddress address : list) {
			address.setUsing(null);
			address.setDisabled(null);
			try {
				address.setUsdtAmount(handler.getBalance(address));
			}
			catch (Exception e) {
				log.error("更新地址余额异常! 地址: " + address.getAddress(), e);
			}
		}

		updateBatchById(list);
	}

}
