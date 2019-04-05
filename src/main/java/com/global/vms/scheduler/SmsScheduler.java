package com.global.vms.scheduler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.global.vms.exception.VMSException;
import com.global.vms.helper.SmsHelper;
import com.global.vms.model.VisitorExceedOutTime;
import com.global.vms.model.sms.Message;
import com.global.vms.repository.VMSRepository;
import com.global.vms.util.AppConstants;
import com.global.vms.util.VMSUtil;

@Component
public class SmsScheduler {
	private final Logger log = LoggerFactory.getLogger(this.getClass());
	@Autowired
	private Environment env;
	@Autowired
	private VMSRepository vmsRepository;
	@Autowired
	private SmsHelper smsHelper;

	@Scheduled(fixedRateString = "${smsjob.startduration.milis}")
	public void sendSmsToLateVisitors() {
		log.info("Sms scheduler started.");
		if (env.getProperty("smsjob.scheduled", Boolean.class, false)) {
			try {
				processSms();
			} catch (Exception e) {
				log.error("Error in snding sms", e);
			}
		}
	}

	private void processSms() {
		log.info("inside send sms method");
		List<VisitorExceedOutTime> visitors = vmsRepository.getVisitorsExceedOutTime();
		if (visitors == null || visitors.isEmpty())
			return;
		visitors.forEach(visitor -> {
			String msgBody = env.getProperty("sms.alert.message")
					.replace(AppConstants.MSG_PLACEHOLDER_NAME, VMSUtil.extractFirstName(visitor.getName()))
					.replace(AppConstants.MSG_PLACEHOLDER_EXIT_TIME, visitor.getOutTime());
			List<Message> msgs = new ArrayList<>();
			msgs.add(new Message(msgBody, Arrays.asList(new String[] { visitor.getMobile() })));
			try {
				smsHelper.sendSms(msgs);
			} catch (VMSException e) {
				log.error("Error in sending sms", e);
			}
		});
	}
}
