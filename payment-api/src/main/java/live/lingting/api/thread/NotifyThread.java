package live.lingting.api.thread;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import com.hccake.ballcat.common.util.JsonUtils;
import com.hccake.ballcat.common.util.json.TypeReference;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.DependsOn;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import live.lingting.entity.Notify;
import live.lingting.entity.NotifyLog;
import live.lingting.entity.Pay;
import live.lingting.entity.Project;
import live.lingting.sdk.constant.SdkConstants;
import live.lingting.sdk.enums.NotifyStatus;
import live.lingting.sdk.util.MixUtils;
import live.lingting.service.NotifyLogService;
import live.lingting.service.NotifyService;
import live.lingting.service.PayService;
import live.lingting.service.ProjectService;
import live.lingting.util.NotifyUtils;

/**
 * @author lingting 2021/6/10 16:54
 */
@Component
@RequiredArgsConstructor
@DependsOn("objectMapper")
public class NotifyThread extends AbstractThread<Notify> {

	/**
	 * 获取数据大小
	 */
	private static final Integer SIZE = 100;

	private final PayService payService;

	private final ProjectService projectService;

	private final NotifyService service;

	private final NotifyLogService logService;

	@Override
	public List<Notify> listData() {
		return service.listNotifying(SIZE);
	}

	@Override
	public void handler(Notify notify) {
		NotifyUtils.run(new NotifyRunnable(payService, projectService, service, logService, notify));
	}

	@Slf4j
	@RequiredArgsConstructor
	public static class NotifyRunnable implements Runnable {

		private final PayService payService;

		private final ProjectService projectService;

		private final NotifyService service;

		private final NotifyLogService logService;

		private final Notify notify;

		@Override
		public void run() {
			if (!service.lock(notify)) {
				return;
			}
			final Project project = projectService.getById(notify.getProjectId());
			final Pay pay = payService.getById(notify.getTradeNo());
			try {
				final HttpRequest post = HttpUtil.createPost(notify.getNotifyUrl());
				Map<String, String> params = generateParams(project, pay);
				NotifyLog nl = execute(post, params);

				logService.save(nl);

				if (nl.getStatus().equals(NotifyStatus.SUCCESS)) {
					service.notifyComplete(notify, NotifyStatus.SUCCESS, null);
					payService.notifyComplete(pay, NotifyStatus.SUCCESS);
				}
				else {
					LocalDateTime nextTime = NotifyUtils.generateNextTime(notify.getCount() + 1);
					// 通知新状态
					NotifyStatus status = nextTime == null ? NotifyStatus.FAIL : NotifyStatus.WAIT;

					service.notifyComplete(notify, status, nextTime);

					if (status.equals(NotifyStatus.FAIL)) {
						payService.notifyComplete(pay, NotifyStatus.FAIL);
					}
				}

			}
			catch (Exception e) {
				service.unlock(notify);
				log.error("发起回调请求时异常! tradeNo: {}", notify.getTradeNo(), e);
			}
		}

		private NotifyLog execute(HttpRequest post, Map<String, String> params) {
			final String json = JsonUtils.toJson(params);
			post.body(json, MediaType.APPLICATION_JSON_VALUE);
			// 连接超时 10 秒
			post.setConnectionTimeout(10 * 1000);
			// 读取超时 5 分钟
			post.setReadTimeout(5 * 60 * 1000);

			HttpResponse response = null;
			Exception exception = null;
			try {
				response = post.execute();
			}
			catch (Exception e) {
				exception = e;
			}
			boolean success = response != null && response.getStatus() == SdkConstants.SUCCESS_CODE;

			String res = response != null ? response.body() : "";

			if ("".equals(res) && exception != null) {
				res = exception.getMessage();
			}

			if (success) {
				// 返回值不是 success 不算成功
				success = SdkConstants.SUCCESS_BODY.equalsIgnoreCase(res);
			}

			return new NotifyLog().setNotifyId(notify.getId()).setNotifyUrl(notify.getNotifyUrl())
					.setTradeNo(notify.getTradeNo()).setStatus(success ? NotifyStatus.SUCCESS : NotifyStatus.FAIL)
					.setHttpStatus(response == null ? 0 : response.getStatus()).setProjectId(notify.getProjectId())
					.setParams(json).setRes(res);
		}

		private Map<String, String> generateParams(Project project, Pay pay) {
			Map<String, String> params = JsonUtils.toObj(JsonUtils.toJson(pay),
					new TypeReference<Map<String, String>>() {
					});

			params.put(SdkConstants.FIELD_RATE, notify.getRate() == null ? null : notify.getRate().toPlainString());
			params.put(SdkConstants.FIELD_KEY, project.getApiKey());
			params.put(SdkConstants.FIELD_NONCE, RandomUtil.randomString(6));
			params.put(SdkConstants.FIELD_SIGN, MixUtils.sign(project.getApiSecurity(), params));
			return params;
		}

	}

}
