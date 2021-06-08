package live.lingting.service.impl;

import com.hccake.extend.mybatis.plus.service.impl.ExtendServiceImpl;
import java.util.List;
import org.springframework.stereotype.Service;
import live.lingting.entity.VirtualAddress;
import live.lingting.mapper.VirtualAddressMapper;
import live.lingting.sdk.model.MixVirtualPayModel;
import live.lingting.service.VirtualAddressService;

/**
 * @author lingting 2021/6/7 15:43
 */
@Service
public class VirtualAddressServiceImpl extends ExtendServiceImpl<VirtualAddressMapper, VirtualAddress>
		implements VirtualAddressService {

	@Override
	public VirtualAddress lock(MixVirtualPayModel model) {
		final VirtualAddress qo = new VirtualAddress().setChain(model.getChain()).setDisabled(false).setUsing(false);
		final List<VirtualAddress> list = baseMapper.selectList(baseMapper.getWrapper(qo));
		for (VirtualAddress va : list) {
			// 锁
			if (baseMapper.lock(va)) {
				va.setUsing(true);
				return va;
			}
		}
		return null;
	}

}
