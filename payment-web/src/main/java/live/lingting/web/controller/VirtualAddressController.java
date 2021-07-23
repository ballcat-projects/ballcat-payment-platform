package live.lingting.web.controller;

import com.hccake.ballcat.common.model.domain.PageResult;
import com.hccake.ballcat.common.model.result.R;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import live.lingting.Page;
import live.lingting.dto.VirtualAddressBalanceDTO;
import live.lingting.dto.VirtualAddressCreateDTO;
import live.lingting.dto.VirtualAddressDisabledDTO;
import live.lingting.dto.VirtualAddressModeDTO;
import live.lingting.dto.VirtualAddressProjectIdsDTO;
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

	@PatchMapping("balance/all")
	@PreAuthorize("@per.hasPermission('virtual:address:read')")
	public R<?> balanceAll() {
		return balance(new VirtualAddressBalanceDTO());
	}

	@PatchMapping("balance")
	@PreAuthorize("@per.hasPermission('virtual:address:read')")
	public R<?> balance(@RequestBody @Validated VirtualAddressBalanceDTO dto) {
		service.balance(dto);
		return R.ok();
	}

	@PatchMapping("disabled")
	@PreAuthorize("@per.hasPermission('virtual:address:edit')")
	public R<?> disabled(@RequestBody @Validated VirtualAddressDisabledDTO dto) {
		service.disabled(dto.getIds(), dto.getDisabled());
		return R.ok();
	}

	@PatchMapping("mode")
	@PreAuthorize("@per.hasPermission('virtual:address:edit')")
	public R<?> mode(@RequestBody @Validated VirtualAddressModeDTO dto) {
		service.mode(dto.getIds(), dto.getMode());
		return R.ok();
	}

	@PatchMapping("project")
	@PreAuthorize("@per.hasPermission('virtual:address:edit')")
	public R<?> project(@RequestBody @Validated VirtualAddressProjectIdsDTO dto) {
		service.project(dto.getIds(), dto.getProjectIds());
		return R.ok();
	}

}
