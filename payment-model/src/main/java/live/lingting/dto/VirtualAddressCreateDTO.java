package live.lingting.dto;

import lombok.Data;
import live.lingting.sdk.enums.Chain;

/**
 * @author lingting 2021/6/8 14:38
 */
@Data
public class VirtualAddressCreateDTO {

	private Chain chain;

	private String address;

	private Boolean disabled;

	private Boolean success;

	private String desc;

}
