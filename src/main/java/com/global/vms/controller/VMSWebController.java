package com.global.vms.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.global.vms.exception.VMSException;
import com.global.vms.model.ApprovedVisitorsToday;
import com.global.vms.model.Employee;
import com.global.vms.model.EmployeeFamily;
import com.global.vms.model.PersonOfInterest;
import com.global.vms.model.PoiRequest;
import com.global.vms.model.ServiceResponse;
import com.global.vms.model.VisitorRegisterationRequest;
import com.global.vms.model.VisitorRegistrationResponse;
import com.global.vms.service.VMSWebService;

@RestController
public class VMSWebController {
	private final Logger log = LoggerFactory.getLogger(VMSWebController.class);
	@Autowired
	private VMSWebService vmsWebService;
	@Autowired
	Environment env;

	@GetMapping("/web/employeeFamily/{empId}")
	public ServiceResponse<List<EmployeeFamily>> login(@PathVariable("empId") long empId, HttpServletRequest request)
			throws VMSException {
		try {
			return new ServiceResponse<>(env.getProperty("status.success", Integer.class), null,
					vmsWebService.getEmployeeFamily(empId));
		} catch (VMSException e) {
			throw e;
		} catch (Exception e) {
			log.error("Error in login: ", e);
			throw new VMSException(env.getProperty("errormsg.generic"));
		}
	}

	@PostMapping("/web/addVisitorEmployee")
	public ServiceResponse<List<VisitorRegistrationResponse>> registerVisitorEmployee(
			@RequestBody List<VisitorRegisterationRequest> regRequest, HttpServletRequest req) throws VMSException {
		try {
			return vmsWebService.registerVisitor(req, regRequest, false);
		} catch (VMSException e) {
			throw e;
		} catch (Exception e) {
			log.error("Error in login: ", e);
			throw new VMSException(env.getProperty("errormsg.generic"));
		}
	}

	@PostMapping("/web/addVisitorSecurity")
	public ServiceResponse<List<VisitorRegistrationResponse>> registerVisitorSecurity(
			@RequestBody List<VisitorRegisterationRequest> regRequest, HttpServletRequest req) throws VMSException {
		try {
			return vmsWebService.registerVisitor(req, regRequest, true);
		} catch (VMSException e) {
			throw e;
		} catch (Exception e) {
			log.error("Error in login: ", e);
			throw new VMSException(env.getProperty("errormsg.generic"));
		}
	}

	@GetMapping("/web/getApprovedVisitorsToday")
	public ServiceResponse<List<ApprovedVisitorsToday>> approvedVisitorsToday() throws VMSException {
		try {
			return new ServiceResponse<>(env.getProperty("status.success", Integer.class), null,
					vmsWebService.approvedVisitorsToday());
		} catch (VMSException e) {
			throw e;
		} catch (Exception e) {
			log.error("Error in approvedVisitorsToday: ", e);
			throw new VMSException(env.getProperty("errormsg.generic"));
		}
	}

	@GetMapping("/web/employee/{empId}")
	public Employee getEmployeeDetails(@PathVariable("empId") long empId) throws VMSException {
		try {
			return vmsWebService.fetchEmployeeById(empId);
		} catch (VMSException e) {
			throw e;
		} catch (Exception e) {
			log.error("Error in getEmployeeDetails: ", e);
			throw new VMSException(env.getProperty("errormsg.generic"));
		}
	}

	@PostMapping("/web/addPoi")
	public ServiceResponse<Object> addPoi(@RequestBody List<PoiRequest> request, HttpServletRequest req)
			throws VMSException {
		try {
			vmsWebService.addPoi(request.get(0));
			return new ServiceResponse<>(env.getProperty("status.success", Integer.class),
					"Person of Interest added successfully !", null);
		} catch (VMSException e) {
			throw e;
		} catch (Exception e) {
			log.error("Error in addPoi: ", e);
			throw new VMSException(env.getProperty("errormsg.generic"));
		}
	}

	@GetMapping("/web/getPoi")
	public ServiceResponse<List<PersonOfInterest>> getPoi() throws VMSException {
		try {
			return new ServiceResponse<>(env.getProperty("status.success", Integer.class), null,
					vmsWebService.getPoi());
		} catch (VMSException e) {
			throw e;
		} catch (Exception e) {
			log.error("Error in getPoi: ", e);
			throw new VMSException(env.getProperty("errormsg.generic"));
		}
	}
}
