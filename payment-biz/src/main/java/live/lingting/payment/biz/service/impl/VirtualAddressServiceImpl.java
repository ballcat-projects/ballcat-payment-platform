package live.lingting.payment.biz.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import java.util.Collections;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import live.lingting.payment.Page;
import live.lingting.payment.biz.mapper.VirtualAddressMapper;
import live.lingting.payment.biz.service.VirtualAddressService;
import live.lingting.payment.biz.virtual.VirtualHandler;
import live.lingting.payment.dto.VirtualAddressBalanceDTO;
import live.lingting.payment.dto.VirtualAddressCreateDTO;
import live.lingting.payment.entity.Project;
import live.lingting.payment.entity.VirtualAddress;
import live.lingting.payment.enums.VirtualAddressMode;
import live.lingting.payment.sdk.model.MixVirtualPayModel;

/**
 * @author lingting 2021/6/7 15:43
 */
@Service
@RequiredArgsConstructor
public class VirtualAddressServiceImpl extends ServiceImpl<VirtualAddressMapper, VirtualAddress>
		implements VirtualAddressService {

	private static final Integer SHUFFLE_MIN = 3;

	private final VirtualHandler handler;

	@Override
	public VirtualAddress lock(MixVirtualPayModel model, Project project) {
		final List<VirtualAddress> list = baseMapper.load(model.getChain(), project.getId(), project.getMode());
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
	public void disabled(List<Integer> ids, Boolean disabled) {
		baseMapper.disabled(ids, disabled);
	}

	@Override
	public VirtualAddressCreateDTO create(VirtualAddressCreateDTO dto) {
		for (VirtualAddressCreateDTO.Va address : dto.getList()) {
			address.setSuccess(false);
			final VirtualAddress va = new VirtualAddress().setAddress(address.getAddress()).setChain(dto.getChain())
					.setMode(dto.getMode()).setProjectIds(dto.getIds());
			if (!handler.valid(va)) {
				address.setDesc("无效地址!");
				continue;
			}

			if (baseMapper.selectCount(baseMapper.getWrapper(va)) > 0) {
				address.setDesc("已存在相同地址!");
			}

			try {
				va.setDisabled(dto.getDisabled());
				address.setSuccess(save(va));
				if (!address.getSuccess()) {
					address.setDesc("保存失败!");
				}
			}
			catch (Exception e) {
				address.setSuccess(false);
				address.setDesc("保存异常!");
			}
		}

		return dto;
	}

	@Override
	public void mode(List<Integer> ids, VirtualAddressMode mode) {
		baseMapper.mode(ids, mode);
	}

	@Override
	public void project(List<Integer> ids, List<Integer> projectIds) {
		baseMapper.project(ids, projectIds);
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
			address.setMode(null);
			address.setProjectIds(null);
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
