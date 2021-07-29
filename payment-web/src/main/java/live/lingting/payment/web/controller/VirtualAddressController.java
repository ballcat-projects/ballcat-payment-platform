package live.lingting.payment.web.controller;

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
import live.lingting.payment.Page;
import live.lingting.payment.dto.VirtualAddressBalanceDTO;
import live.lingting.payment.dto.VirtualAddressCreateDTO;
import live.lingting.payment.dto.VirtualAddressDisabledDTO;
import live.lingting.payment.dto.VirtualAddressModeDTO;
import live.lingting.payment.dto.VirtualAddressProjectIdsDTO;
import live.lingting.payment.entity.VirtualAddress;
import live.lingting.payment.biz.service.VirtualAddressService;

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
