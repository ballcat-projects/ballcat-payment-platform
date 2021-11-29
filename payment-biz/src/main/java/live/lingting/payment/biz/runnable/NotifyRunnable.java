package live.lingting.payment.biz.runnable;

import cn.hutool.core.util.RandomUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import java.time.LocalDateTime;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import live.lingting.payment.biz.service.NotifyLogService;
import live.lingting.payment.biz.service.NotifyService;
import live.lingting.payment.biz.service.PayService;
import live.lingting.payment.biz.service.ProjectService;
import live.lingting.payment.biz.util.NotifyUtils;
import live.lingting.payment.biz.util.SpringUtils;
import live.lingting.payment.entity.Notify;
import live.lingting.payment.entity.NotifyLog;
import live.lingting.payment.entity.Pay;
import live.lingting.payment.entity.Project;
import live.lingting.payment.http.HttpMediaType;
import live.lingting.payment.http.HttpPost;
import live.lingting.payment.http.HttpResponse;
import live.lingting.payment.sdk.constant.SdkConstants;
import live.lingting.payment.sdk.domain.MixCallback;
import live.lingting.payment.sdk.enums.NotifyStatus;
import live.lingting.payment.sdk.util.MixUtils;
import live.lingting.payment.util.JacksonUtils;

/**
 * @author lingting 2021/9/10 16:06
 */
@Slf4j
public class NotifyRunnable implements Runnable {

	private final PayService payService;

	private final ProjectService projectService;

	private final NotifyService service;

	private final NotifyLogService logService;

	private final Notify notify;

	public NotifyRunnable(Notify notify) {
		this.payService = SpringUtils.getBean(PayService.class);
		this.projectService = SpringUtils.getBean(ProjectService.class);
		this.service = SpringUtils.getBean(NotifyService.class);
		this.logService = SpringUtils.getBean(NotifyLogService.class);
		this.notify = notify;
	}

	public NotifyRunnable(PayService payService, ProjectService projectService, NotifyService service,
			NotifyLogService logService, Notify notify) {
		this.payService = payService;
		this.projectService = projectService;
		this.service = service;
		this.logService = logService;
		this.notify = notify;
	}

	@Override
	public void run() {
		if (!service.lock(notify)) {
			return;
		}
		final Project project = projectService.getById(notify.getProjectId());
		final Pay pay = payService.getById(notify.getTradeNo());
		try {
			HttpPost post = HttpPost.of(notify.getNotifyUrl());
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

	private NotifyLog execute(HttpPost post, String body) {
		post.setBody(body, HttpMediaType.APPLICATION_JSON.getValue());
		// 连接超时 10 秒
		post.setConnectTimeout(10000);
		// 读取超时 5 分钟
		post.setReadTimeout(300000);

		HttpResponse response = null;
		Exception exception = null;
		try {
			response = post.exec();
		}
		catch (Exception e) {
			exception = e;
		}
		boolean success = response != null && SdkConstants.SUCCESS_CODE.equals(response.getStatus());

		String res = response != null ? response.getBody() : "";

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

	public String getId() {
		return notify.getId().toString();
	}

}
