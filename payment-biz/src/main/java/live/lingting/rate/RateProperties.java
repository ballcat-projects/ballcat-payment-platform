package live.lingting.rate;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author lingting 2021/6/11 15:59
 */
@Getter
@Setter
@ConfigurationProperties("mix.rate")
public class RateProperties {

	private Yy yy;

	@Getter
	@Setter
	public static class Yy {

		private String code;

		private String key;

		private String security;

	}

}
