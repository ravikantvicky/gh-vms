package com.global.vms.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.global.vms.exception.VMSException;
import com.global.vms.model.ApproveVisitorRequest;
import com.global.vms.model.InsideVisitor;
import com.global.vms.model.LocationAccessRequest;
import com.global.vms.model.LocationAccessResponse;
import com.global.vms.model.ServiceResponse;
import com.global.vms.model.UpdateStatusRequest;
import com.global.vms.model.VisitorProfileRequest;
import com.global.vms.model.VisitorProfileResponse;
import com.global.vms.model.VisitorsResponse;
import com.global.vms.service.VMSAppService;
import com.global.vms.service.VMSService;

@RestController
public class VmsAppController {

	private final Logger log = LoggerFactory.getLogger(this.getClass());
	@Autowired
	private VMSService vmsService;
	@Autowired
	private VMSAppService vmsAppService;
	@Autowired
	Environment env;

	@GetMapping("/mobile/getVisitors")
	public ServiceResponse<VisitorsResponse> getVisitors() throws VMSException {
		try {
			return new ServiceResponse<>(env.getProperty("status.success", Integer.class), null,
					vmsService.fetchVisitors());
		} catch (VMSException e) {
			throw e;
		} catch (Exception e) {
			log.error("Error in login: ", e);
			throw new VMSException(env.getProperty("errormsg.generic"));
		}
	}

	@PostMapping("/mobile/getVisitorProfile")
	public ServiceResponse<VisitorProfileResponse> getVisitorProfile(@RequestBody VisitorProfileRequest request)
			throws VMSException {
		try {
			return new ServiceResponse<>(env.getProperty("status.success", Integer.class), null,
					vmsService.fetchVisitorProfile(request));
		} catch (VMSException e) {
			throw e;
		} catch (Exception e) {
			log.error("Error in getVisitorProfile: ", e);
			throw new VMSException(env.getProperty("errormsg.generic"));
		}
	}

	@GetMapping("/mobile/insideVisitors")
	public ServiceResponse<List<InsideVisitor>> getAllInsideVisitor(HttpServletRequest req) throws VMSException {
		try {
			return new ServiceResponse<>(env.getProperty("status.success", Integer.class), null,
					vmsService.allInsideVisitors(req));
		} catch (VMSException e) {
			throw e;
		} catch (Exception e) {
			log.error("Error in getAllInsideVisitor: ", e);
			throw new VMSException(env.getProperty("errormsg.generic"));
		}
	}

	@PostMapping("mobile/approveVisitor")
	public ServiceResponse<Object> approveVisitor(@RequestBody ApproveVisitorRequest request) throws VMSException {
		try {
			vmsAppService.approveVisitor(request);
			return new ServiceResponse<>(env.getProperty("status.success", Integer.class), null, null);
		} catch (VMSException e) {
			throw e;
		} catch (Exception e) {
			log.error("Error in approveVisitor: ", e);
			throw new VMSException(env.getProperty("errormsg.generic"));
		}
	}

	@PostMapping("/mobile/updateStatus")
	public ServiceResponse<Object> updateStatus(@RequestBody UpdateStatusRequest request) throws VMSException {
		try {
			vmsAppService.updateStatus(request);
			return new ServiceResponse<>(env.getProperty("status.success", Integer.class), null, null);
		} catch (VMSException e) {
			throw e;
		} catch (Exception e) {
			log.error("Error in updateStatus: ", e);
			throw new VMSException(env.getProperty("errormsg.generic"));
		}
	}

	@PostMapping("/mobile/locationAccess")
	public ServiceResponse<LocationAccessResponse> locationAccess(@RequestBody LocationAccessRequest request,
			HttpServletRequest req) throws VMSException {
		try {
			LocationAccessResponse loc = vmsAppService.visitorLocations(request, req);
			if (loc != null && loc.getName() != null)
				return new ServiceResponse<>(env.getProperty("status.success", Integer.class), null, loc);
			throw new VMSException(env.getProperty("errormsg.visitor.notFound"));
		} catch (VMSException e) {
			throw e;
		} catch (Exception e) {
			log.error("Error in updateStatus: ", e);
			throw new VMSException(env.getProperty("errormsg.generic"));
		}
	}
}
