package live.lingting.virtual;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author lingting 2021/6/30 19:58
 */
@Getter
@Setter
@ConfigurationProperties("mix.virtual")
public class VirtualProperties {

	private String ethKey;

	private String ethSecurity;

	private String trcKey;

}
