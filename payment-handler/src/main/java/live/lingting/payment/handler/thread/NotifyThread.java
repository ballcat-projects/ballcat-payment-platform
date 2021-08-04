package live.lingting.payment.handler.thread;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import live.lingting.payment.http.utils.JacksonUtils;
import com.fasterxml.jackson.core.type.TypeReference;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.DependsOn;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import live.lingting.payment.biz.service.NotifyLogService;
import live.lingting.payment.biz.service.NotifyService;
import live.lingting.payment.biz.service.PayService;
import live.lingting.payment.biz.service.ProjectService;
import live.lingting.payment.entity.Notify;
import live.lingting.payment.entity.NotifyLog;
import live.lingting.payment.entity.Pay;
import live.lingting.payment.entity.Project;
import live.lingting.payment.sdk.constant.SdkConstants;
import live.lingting.payment.sdk.domain.MixCallback;
import live.lingting.payment.sdk.enums.NotifyStatus;
import live.lingting.payment.sdk.util.MixUtils;
import live.lingting.payment.util.NotifyUtils;

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
				NotifyLog nl = execute(post, getBody(project, pay));

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

		private NotifyLog execute(HttpRequest post, String body) {
			post.body(body, MediaType.APPLICATION_JSON_VALUE);
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
					.setParams(body).setRes(res);
		}

		private String getBody(Project project, Pay pay) {
			final MixCallback callback = new MixCallback();
			callback.setTradeNo(pay.getTradeNo());
			callback.setProjectId(pay.getProjectId());
			callback.setProjectTradeNo(pay.getProjectTradeNo());
			callback.setThirdPartTradeNo(pay.getThirdPartTradeNo());
			callback.setStatus(pay.getStatus());
			callback.setAmount(pay.getAmount());
			callback.setCurrency(pay.getCurrency());
			callback.setChain(pay.getChain());
			callback.setAddress(pay.getAddress());
			callback.setRetryEndTime(pay.getRetryEndTime());
			callback.setThirdPart(pay.getThirdPart());
			callback.setMode(pay.getMode());
			callback.setDesc(pay.getDesc());
			callback.setNotifyUrl(pay.getNotifyUrl());
			callback.setRate(notify.getRate());
			callback.setNotifyStatus(pay.getNotifyStatus());
			callback.setCompleteTime(pay.getCompleteTime());
			callback.setCreateTime(pay.getCreateTime());

			callback.setKey(project.getApiKey());
			callback.setNonce(RandomUtil.randomString(6));

			Map<String, String> params = JacksonUtils.toObj(JacksonUtils.toJson(callback),
					new TypeReference<Map<String, String>>() {
					});

			callback.setSign(MixUtils.sign(project.getApiSecurity(), params));

			return JacksonUtils.toJson(callback);
		}

	}

}
