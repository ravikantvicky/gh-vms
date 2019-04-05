package com.global.vms.service;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.global.vms.exception.VMSException;
import com.global.vms.helper.EmailHelper;
import com.global.vms.helper.PdfHelper;
import com.global.vms.helper.TokenUtils;
import com.global.vms.model.InsideVisitor;
import com.global.vms.model.LoginRequest;
import com.global.vms.model.LoginResponse;
import com.global.vms.model.VisitorProfileRequest;
import com.global.vms.model.VisitorProfileResponse;
import com.global.vms.model.VisitorsResponse;
import com.global.vms.model.mail.MailAttachment;
import com.global.vms.model.mail.MailData;
import com.global.vms.repository.VMSRepository;
import com.global.vms.util.AppConstants;
import com.global.vms.util.MessageUtils;
import com.global.vms.util.VMSUtil;
import com.global.vms.util.WebUtils;

@Service
public class VMSService {
	private Logger log = LoggerFactory.getLogger(this.getClass());
	@Autowired
	private VMSRepository vmsRepository;
	@Autowired
	private TokenUtils tokenUtils;
	@Autowired
	private TextCryptService textCryptService;
	@Autowired
	private VMSWebService vmsWebService;
	@Autowired
	private PdfHelper pdfHelper;
	@Autowired
	private EmailHelper emailHelper;
	@Autowired
	private Environment env;
	@Autowired
	private MailContentService mailContentService;

	public LoginResponse login(LoginRequest request, String ip) throws VMSException {
		LoginResponse resp;
		try {
			resp = vmsRepository.login(request.getEmail(), MessageUtils.toSHA256(request.getPassword()));
			resp.setToken(tokenUtils.generateToken(ip));
			if (resp.getUserRole().equalsIgnoreCase(env.getProperty("role.employee")))
				resp.setEmployeeFamily(vmsWebService.getEmployeeFamily(resp.getUserId()));
		} catch (VMSException e) {
			throw e;
		} catch (Exception e) {
			log.error("Error in login service: ", e);
			throw new VMSException(env.getProperty("errormsg.generic"));
		}
		return resp;
	}

	public String getApprovalUrl(String baseUrl, long visitorId, long approverId) throws VMSException {
		try {
			StringBuilder url = new StringBuilder(baseUrl);
			url.append(
					"/approve/" + textCryptService.encryptText(visitorId + AppConstants.SAP_APPROVAL_URL + approverId));
			return url.toString();
		} catch (Exception e) {
			throw new VMSException(env.getProperty("errormsg.generic"));
		}
	}

	public void approve(String cryptData) throws VMSException {
		try {
			String dcryptData = textCryptService.decryptText(cryptData);
			log.info("Dcrypted data: {}", dcryptData);
			if (!dcryptData.contains(AppConstants.SAP_APPROVAL_URL))
				throw new VMSException(env.getProperty("errormsg.invalidInput"));
			String data[] = dcryptData.split("\\" + AppConstants.SAP_APPROVAL_URL);
			if (data.length != 2)
				throw new VMSException(env.getProperty("errormsg.invalidInput"));
			long visitorId = Long.valueOf(data[0]), approverId = Long.valueOf(data[1]);
			vmsRepository.checkApproverEligible(visitorId, approverId);
			vmsRepository.approveVisitor(visitorId, approverId);
			// Send gate pass in mail
			VisitorProfileResponse profile = vmsRepository.getVisitorProfile(visitorId);
			byte[] pdfGatePass = pdfHelper.generateGatePass(profile);
			String mailBody = mailContentService.getMailBody(env.getProperty("mail.content.gatepass"));
			mailBody = mailBody.replace(AppConstants.MSG_PLACEHOLDER_NAME, VMSUtil.extractFirstName(profile.getName()));
			mailBody = mailBody.replace(AppConstants.MSG_PLACEHOLDER_EXPECTED_ENTRY,
					VMSUtil.formatStringDate(profile.getExpectedEntry(), env.getProperty("dateformat.default"),
							env.getProperty("dateformat.ui")));
			MailData mail = new MailData();
			mail.setSubject(env.getProperty("mail.gatePass.subject"));
			mail.setTo(new ArrayList<>());
			mail.getTo().add(profile.getEmail());
			mail.setMailBody(mailBody);
			mail.setInlineAttachments(new ArrayList<>());
			mail.getInlineAttachments().add(new MailAttachment("logo", null,
					mailContentService.getMailAttachment(env.getProperty("mail.logo"))));
			mail.setFileAttachments(new ArrayList<>());
			mail.getFileAttachments().add(new MailAttachment("gate-pass.pdf", null, pdfGatePass));
			emailHelper.sendMail(mail);
		} catch (VMSException e) {
			throw e;
		} catch (Exception e) {
			log.error("Error in approval", e);
			throw new VMSException(env.getProperty("errormsg.approval"));
		}
	}

	public VisitorsResponse fetchVisitors() throws VMSException {
		VisitorsResponse resp;
		try {
			resp = vmsRepository.fetchVisitors();
		} catch (VMSException e) {
			throw e;
		} catch (Exception e) {
			log.error("Error in fetchVisitors service: ", e);
			throw new VMSException(env.getProperty("errormsg.generic"));
		}
		return resp;
	}

	public VisitorProfileResponse fetchVisitorProfile(VisitorProfileRequest request) throws VMSException {
		VisitorProfileResponse resp;
		try {
			String visitorId = null;
			if (request.getEncrypted() == env.getProperty("value.encrypted.true", Integer.class))
				visitorId = textCryptService.decryptText(request.getVisitorId());
			else
				visitorId = request.getVisitorId();
			resp = vmsRepository.getVisitorProfile(Long.valueOf(visitorId));
		} catch (VMSException e) {
			throw e;
		} catch (Exception e) {
			log.error("Error in fetchVisitorProfile service: ", e);
			throw new VMSException(env.getProperty("errormsg.generic"));
		}
		return resp;
	}

	public List<InsideVisitor> allInsideVisitors(HttpServletRequest req) throws VMSException {
		try {
			return vmsRepository.allInsideVisitors(WebUtils.getBaseUrl(req));
		} catch (VMSException e) {
			throw e;
		} catch (Exception e) {
			log.error("Error in allInsideVisitors service: ", e);
			throw new VMSException(env.getProperty("errormsg.generic"));
		}
	}
}
