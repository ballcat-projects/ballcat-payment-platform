package live.lingting.service.impl;

import com.hccake.ballcat.common.model.domain.PageResult;
import com.hccake.extend.mybatis.plus.service.impl.ExtendServiceImpl;
import java.util.Collections;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import live.lingting.Page;
import live.lingting.dto.VirtualAddressCreateDTO;
import live.lingting.entity.VirtualAddress;
import live.lingting.mapper.VirtualAddressMapper;
import live.lingting.sdk.model.MixVirtualPayModel;
import live.lingting.service.VirtualAddressService;
import live.lingting.virtual.VirtualHandler;

/**
 * @author lingting 2021/6/7 15:43
 */
@Service
@RequiredArgsConstructor
public class VirtualAddressServiceImpl extends ExtendServiceImpl<VirtualAddressMapper, VirtualAddress>
		implements VirtualAddressService {

	private final VirtualHandler handler;

	private static final Integer SHUFFLE_MIN = 3;

	@Override
	public VirtualAddress lock(MixVirtualPayModel model) {
		final VirtualAddress qo = new VirtualAddress().setChain(model.getChain()).setDisabled(false).setUsing(false);
		final List<VirtualAddress> list = baseMapper.selectList(baseMapper.getWrapper(qo));
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
	public PageResult<VirtualAddress> list(Page<VirtualAddress> page, VirtualAddress qo) {
		return baseMapper.list(page, qo);
	}

	@Override
	public void disabled(Integer id, Boolean disabled) {
		baseMapper.disabled(id, disabled);
	}

	@Override
	public List<VirtualAddressCreateDTO> create(List<VirtualAddressCreateDTO> list) {
		for (VirtualAddressCreateDTO dto : list) {
			dto.setSuccess(false);
			final VirtualAddress va = new VirtualAddress().setAddress(dto.getAddress()).setChain(dto.getChain());
			if (!handler.valid(va)) {
				dto.setDesc("无效地址!");
				continue;
			}

			if (baseMapper.selectCount(baseMapper.getWrapper(va)) > 0) {
				dto.setDesc("已存在相同地址!");
			}

			try {
				va.setDisabled(dto.getDisabled());
				dto.setSuccess(save(va));
				if (!dto.getSuccess()) {
					dto.setDesc("保存失败!");
				}
			}
			catch (Exception e) {
				dto.setSuccess(false);
				dto.setDesc("保存异常!");
			}
		}

		return list;
	}

}
