package live.lingting.api.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import live.lingting.service.ProjectService;

/**
 * @author lingting 2021/6/7 17:05
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("pay")
public class PayController {

	private final ProjectService service;

}
