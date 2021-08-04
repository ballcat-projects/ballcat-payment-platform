package live.lingting.payment;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import io.swagger.annotations.ApiModel;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Range;

/**
 * @author lingting 2021/6/4 16:29
 */
@Getter
@Setter
public class Page<T> {

	/**
	 * 当前页
	 */
	@NotNull(message = "当前页码不能为空")
	@Min(value = 1, message = "当前页不能小于 1")
	private long current = 1;

	/**
	 * 每页显示条数，默认 10
	 */
	@NotNull(message = "每页条数不能为空")
	@Range(min = 1, max = 100, message = "条数范围为 [1, 100]")
	private long size = 10;

	private long total;

	private Collection<T> records;

	private List<Sort> sorts = new ArrayList<>();

	@Getter
	@Setter
	@ApiModel("排序元素载体")
	public static class Sort {

		/**
		 * 排序字段
		 */
		private String field;

		/**
		 * 是否正序排序
		 */
		private boolean asc;

	}

	public Page() {
	}

	public Page(long current, long size) {
		super();
		this.setCurrent(current);
		this.setSize(size);
	}

	public Page(Collection<T> records, long total) {
		super();
		setRecords(records);
		setTotal(total);
	}

	public IPage<T> toPage() {
		com.baomidou.mybatisplus.extension.plugins.pagination.Page<T> page = new com.baomidou.mybatisplus.extension.plugins.pagination.Page<T>()
				// 当前页
				.setCurrent(getCurrent())
				// 每页数量
				.setSize(getSize());

		// 排序规则
		for (Sort sort : getSorts()) {
			page.getOrders().add(new OrderItem(sort.getField(), sort.isAsc()));
		}
		return page;
	}

}
