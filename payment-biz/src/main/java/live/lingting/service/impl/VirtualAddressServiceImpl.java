package live.lingting.service.impl;

import com.hccake.extend.mybatis.plus.service.impl.ExtendServiceImpl;
import org.springframework.stereotype.Service;
import live.lingting.entity.VirtualAddress;
import live.lingting.mapper.VirtualAddressMapper;
import live.lingting.service.VirtualAddressService;

/**
 * @author lingting 2021/6/7 15:43
 */
@Service
public class VirtualAddressServiceImpl extends ExtendServiceImpl<VirtualAddressMapper, VirtualAddress>
		implements VirtualAddressService {

}
