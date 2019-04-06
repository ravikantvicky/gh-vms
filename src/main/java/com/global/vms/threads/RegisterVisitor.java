package com.global.vms.threads;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.core.env.Environment;
import org.springframework.transaction.annotation.Transactional;

import com.global.vms.exception.VMSException;
import com.global.vms.helper.EmailHelper;
import com.global.vms.helper.PdfHelper;
import com.global.vms.model.Approver;
import com.global.vms.model.RegVisitorResponse;
import com.global.vms.model.VisitorProfileResponse;
import com.global.vms.model.VisitorRegisterationRequest;
import com.global.vms.model.mail.MailAttachment;
import com.global.vms.model.mail.MailData;
import com.global.vms.repository.VMSRepository;
import com.global.vms.repository.VMSWebRepository;
import com.global.vms.service.MailContentService;
import com.global.vms.service.VMSService;
import com.global.vms.util.AppConstants;
import com.global.vms.util.ImageUtils;
import com.global.vms.util.VMSUtil;

public class RegisterVisitor implements Callable<RegVisitorResponse> {

	private static final Logger log = LoggerFactory.getLogger(RegisterVisitor.class);
	private VMSWebRepository vmsWebRepository;
	private VMSRepository vmsRepository;
	private VisitorRegisterationRequest regRequest;
	private PdfHelper pdfHelper;
	private EmailHelper emailHelper;
	private Environment env;
	private MailContentService mailContentService;
	private VMSService vmsService;
	private String baseUrl;
	private boolean isSpotRegistration;
	private RegVisitorResponse response;

	public RegisterVisitor(VMSWebRepository vmsWebRepository, VMSRepository vmsRepository,
			VisitorRegisterationRequest regRequest, PdfHelper pdfHelper, EmailHelper emailHelper, Environment env,
			MailContentService mailContentService, VMSService vmsService, String baseUrl, boolean isSpotRegistration) {
		super();
		this.vmsWebRepository = vmsWebRepository;
		this.vmsRepository = vmsRepository;
		this.regRequest = regRequest;
		this.pdfHelper = pdfHelper;
		this.emailHelper = emailHelper;
		this.env = env;
		this.mailContentService = mailContentService;
		this.vmsService = vmsService;
		this.baseUrl = baseUrl;
		this.isSpotRegistration = isSpotRegistration;
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public RegVisitorResponse call() throws Exception {
		long visitorId = 0;
		VisitorProfileResponse profile = new VisitorProfileResponse();
		BeanUtils.copyProperties(regRequest, profile);
		response = new RegVisitorResponse(true,
				env.getProperty("errormsg.visitor.register").replace("{}", regRequest.getName()), false, profile);
		try {
			long status = isSpotRegistration ? env.getProperty("status.visitor.pendingApproval", Long.class)
					: env.getProperty("status.visitor.initial", Long.class);
			long valSpotRegistration = isSpotRegistration ? 1 : 0;
			visitorId = vmsWebRepository.insertVisitor(regRequest, status, valSpotRegistration);
			log.info("Visitor Id: {}", visitorId);
			profile = vmsRepository.getVisitorProfile(visitorId);
			profile.setPhoto(ImageUtils
					.imageToBase64(ImageUtils.resizeImage(ImageUtils.base64ToImage(profile.getPhoto()), 120, 120)));
			response.setProfile(profile);
			if (isSpotRegistration)
				registeredBySecurity(profile);
			else
				registeredByEmployee(profile);
		} catch (VMSException e) {
			response.setError(true);
			response.setErrorMsg(e.getMessage());
			return response;
		} catch (Exception e) {
			log.error("Error in Visitor Registration.", e);
			if (visitorId != 0) {
				try {
					vmsWebRepository.deleteVisitor(visitorId);
				} catch (Exception e2) {
					log.error("Error in visitor delete", e2);
				}
			}
			response.setError(true);
			response.setErrorMsg(env.getProperty("errormsg.visitor.register").replace("{}", regRequest.getName()));
			return response;
		}
		response.setError(false);
		response.setErrorMsg(null);
		return response;
	}

	private void registeredByEmployee(VisitorProfileResponse profile) throws VMSException {
		byte[] pdfGatePass = pdfHelper.generateGatePass(profile);
		String mailBody = mailContentService.getMailBody(env.getProperty("mail.content.gatepass"));
		mailBody = mailBody.replace(AppConstants.MSG_PLACEHOLDER_NAME, VMSUtil.extractFirstName(profile.getName()));
		mailBody = mailBody.replace(AppConstants.MSG_PLACEHOLDER_EXPECTED_ENTRY, VMSUtil.formatStringDate(
				profile.getExpectedEntry(), env.getProperty("dateformat.default"), env.getProperty("dateformat.ui")));
		MailData mail = new MailData();
		mail.setSubject(env.getProperty("mail.gatePass.subject"));
		mail.setTo(new ArrayList<>());
		mail.getTo().add(profile.getEmail());
		mail.setMailBody(mailBody);
		mail.setInlineAttachments(new ArrayList<>());
		mail.getInlineAttachments().add(
				new MailAttachment("logo", null, mailContentService.getMailAttachment(env.getProperty("mail.logo"))));
		mail.setFileAttachments(new ArrayList<>());
		mail.getFileAttachments().add(new MailAttachment("gate-pass.pdf", null, pdfGatePass));
		emailHelper.sendMail(mail);
		if (response != null)
			response.setSentForVerification(false);
	}

	private void registeredBySecurity(VisitorProfileResponse profile) throws VMSException {
		List<Approver> approvers;
		if (regRequest.getRefferedBy() > 0) {
			approvers = vmsWebRepository.getApprover(regRequest.getRefferedBy());
			if (approvers == null || approvers.isEmpty())
				throw new VMSException("Error in fetching approver.");
		} else {
			approvers = vmsWebRepository.getApprover(regRequest.getVisitorType());
		}
		if (approvers == null || approvers.isEmpty()) {
			registeredByEmployee(profile);
			return;
		}

		// Approver mail
		for (Approver app : approvers) {
			String mailBody = mailContentService.getMailBody(env.getProperty("mail.content.approval"));
			mailBody = mailBody.replace(AppConstants.MSG_PLACEHOLDER_NAME, VMSUtil.extractFirstName(app.getName()));
			mailBody = mailBody.replace(AppConstants.MSG_PLACEHOLDER_VISITOR_NAME, profile.getName());
			mailBody = mailBody.replace(AppConstants.MSG_PLACEHOLDER_VISITOR_TYPE, profile.getVisitorType());
			mailBody = mailBody.replace(AppConstants.MSG_PLACEHOLDER_EXPECTED_ENTRY,
					VMSUtil.formatStringDate(profile.getExpectedEntry(), env.getProperty("dateformat.default"),
							env.getProperty("dateformat.ui")));
			mailBody = mailBody.replace(AppConstants.MSG_PLACEHOLDER_APPROVAL_LINK,
					vmsService.getApprovalUrl(baseUrl, profile.getVisitorId(), app.getId()));
			MailData mail = new MailData();
			mail.setSubject(env.getProperty("mail.approval.subject"));
			mail.setTo(new ArrayList<>());
			mail.getTo().add(app.getEmail());
			mail.setMailBody(mailBody);
			mail.setInlineAttachments(new ArrayList<>());
			mail.getInlineAttachments().add(new MailAttachment("logo", null,
					mailContentService.getMailAttachment(env.getProperty("mail.logo"))));
			mail.getInlineAttachments()
					.add(new MailAttachment("photo", null, ImageUtils.base64ToImage(profile.getPhoto())));
			emailHelper.sendMail(mail);
		}
		if (response != null)
			response.setSentForVerification(true);
	}
}
