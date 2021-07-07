package live.lingting.web.controller;

import com.hccake.ballcat.common.model.domain.PageResult;
import com.hccake.ballcat.common.model.result.R;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import live.lingting.Page;
import live.lingting.dto.VirtualAddressCreateDTO;
import live.lingting.entity.VirtualAddress;
import live.lingting.service.VirtualAddressService;

/**
 * @author lingting 2021/6/8 13:43
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("virtual/address")
public class VirtualAddressController {

	private final VirtualAddressService service;

	@GetMapping
	@PreAuthorize("@per.hasPermission('virtual:address:read')")
	public R<PageResult<VirtualAddress>> list(Page<VirtualAddress> page, VirtualAddress qo) {
		return R.ok(service.list(page, qo));
	}

	@PostMapping
	@PreAuthorize("@per.hasPermission('virtual:address:add')")
	public R<VirtualAddressCreateDTO> create(@RequestBody VirtualAddressCreateDTO dto) {
		return R.ok(service.create(dto));
	}

	@PatchMapping("disabled/{id}/{disabled}")
	@PreAuthorize("@per.hasPermission('virtual:address:edit')")
	public R<?> disabled(@PathVariable Integer id, @PathVariable Boolean disabled) {
		service.disabled(id, disabled);
		return R.ok();
	}

}
