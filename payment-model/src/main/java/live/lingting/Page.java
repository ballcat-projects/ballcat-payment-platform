package live.lingting;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.hccake.ballcat.common.model.domain.PageParam;

/**
 * @author lingting 2021/6/4 16:29
 */
public class Page<T> extends PageParam {

	public Page() {
	}

	public Page(long current, long size) {
		super();
		this.setCurrent(current);
		this.setSize(size);
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
