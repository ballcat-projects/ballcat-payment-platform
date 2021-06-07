package live.lingting.sdk.model;

import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;

/**
 * 请求参数基础数据
 * @author lingting 2021/6/7 19:24
 */
@Getter
@Setter
public abstract class MixModel implements Serializable {

	private static final long serialVersionUID = 1L;

	private String tradeNo;

	private String projectTradeNo;

	private String notifyUrl;

}
