package com.global.vms.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import com.global.vms.exception.VMSException;
import com.global.vms.model.EmployeeFamily;
import com.global.vms.model.PersonOfInterest;
import com.global.vms.model.PoiRequest;
import com.global.vms.model.ApprovedVisitorsToday;
import com.global.vms.model.Approver;
import com.global.vms.model.Employee;
import com.global.vms.model.VisitorRegisterationRequest;
import com.global.vms.util.VMSUtil;

@Repository
public class VMSWebRepository {
	private Logger log = LoggerFactory.getLogger(this.getClass());
	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Autowired
	Environment env;

	public List<EmployeeFamily> getEmployeeFamily(long empId) throws VMSException {
		List<EmployeeFamily> family = null;

		try {
			String sql = env.getProperty("sql.employee.family");
			family = jdbcTemplate.query(sql, new Object[] { empId },
					new GenericRowMapper<EmployeeFamily>(EmployeeFamily.class));
		} catch (Exception e) {
			log.error("Error in Visitors fetch:", e);
			throw new VMSException(env.getProperty("errormsg.generic"));
		}

		return family;
	}

	public long insertVisitor(VisitorRegisterationRequest regRequest, long status, long valSpotRegistration)
			throws VMSException {
		try {
			GeneratedKeyHolder holder = new GeneratedKeyHolder();
			jdbcTemplate.update(new PreparedStatementCreator() {

				@Override
				public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
					PreparedStatement ps = con
							.prepareStatement(
									(regRequest.getRefferedBy() == 0 ? env.getProperty("sql.visitor.register.noref")
											: env.getProperty("sql.visitor.register")),
									Statement.RETURN_GENERATED_KEYS);
					ps.setString(1, regRequest.getVisitorType());
					ps.setString(2, regRequest.getName());
					ps.setString(3, regRequest.getEmail());
					ps.setString(4, regRequest.getMobile());
					ps.setLong(5, regRequest.getPhoto());
					if (regRequest.getRefferedBy() > 0) {
						ps.setLong(6, regRequest.getRefferedBy());
						ps.setString(7, VMSUtil.formatStringDate(regRequest.getInTime(),
								env.getProperty("dateformat.default"), env.getProperty("dateformat.default")));
						ps.setString(8, VMSUtil.formatStringDate(regRequest.getOutTime(),
								env.getProperty("dateformat.default"), env.getProperty("dateformat.default")));
						ps.setLong(9, status);
						ps.setLong(10, valSpotRegistration);
					} else {
						ps.setString(6, VMSUtil.formatStringDate(regRequest.getInTime(),
								env.getProperty("dateformat.default"), env.getProperty("dateformat.default")));
						ps.setString(7, VMSUtil.formatStringDate(regRequest.getOutTime(),
								env.getProperty("dateformat.default"), env.getProperty("dateformat.default")));
						ps.setLong(8, status);
						ps.setLong(9, valSpotRegistration);
					}
					return ps;
				}
			}, holder);
			return holder.getKey().longValue();
		} catch (Exception e) {
			log.error("Error in Visitors registration:", e);
			throw new VMSException(env.getProperty("errormsg.generic"));
		}
	}

	public List<ApprovedVisitorsToday> approvedVisitorsToday() throws VMSException {
		List<ApprovedVisitorsToday> family = null;

		try {
			String sql = env.getProperty("sql.visitor.approvedToday");
			family = jdbcTemplate.query(sql, new GenericRowMapper<ApprovedVisitorsToday>(ApprovedVisitorsToday.class));
		} catch (Exception e) {
			log.error("Error in Inside Visitors fetch:", e);
			throw new VMSException(env.getProperty("errormsg.generic"));
		}

		return family;
	}

	public List<Approver> getApprover(long empId) throws VMSException {
		try {
			String sql = env.getProperty("sql.visitor.approver.byEmpId");
			return jdbcTemplate.query(sql, new Object[] { empId }, new GenericRowMapper<Approver>(Approver.class));
		} catch (Exception e) {
			log.error("Error in Approver fetch:", e);
			throw new VMSException(env.getProperty("errormsg.generic"));
		}
	}

	public List<Approver> getApprover(String visitorType) throws VMSException {
		try {
			String sql = env.getProperty("sql.visitor.approver.byVType");
			return jdbcTemplate.query(sql, new Object[] { visitorType },
					new GenericRowMapper<Approver>(Approver.class));
		} catch (Exception e) {
			log.error("Error in Approver fetch:", e);
			throw new VMSException(env.getProperty("errormsg.generic"));
		}
	}

	public void deleteVisitor(long visitorId) throws VMSException {
		try {
			String sql = env.getProperty("sql.visitor.delete");
			jdbcTemplate.update(sql, new Object[] { visitorId });
		} catch (Exception e) {
			log.error("Error in deleteVisitor:", e);
			throw new VMSException(env.getProperty("errormsg.generic"));
		}
	}

	public Employee fetchEmployeebyId(long empId) throws VMSException {
		try {
			String sql = env.getProperty("sql.employee.byEmpId");
			List<Employee> emp = jdbcTemplate.query(sql, new Object[] { empId },
					new GenericRowMapper<Employee>(Employee.class));
			if (emp == null || emp.isEmpty())
				throw new VMSException(env.getProperty("errormsg.employee.notFound"));
			return emp.get(0);
		} catch (Exception e) {
			log.error("Error in deleteVisitor:", e);
			throw new VMSException(env.getProperty("errormsg.generic"));
		}
	}

	public void addPoi(PoiRequest request) throws VMSException {
		try {
			String sql = env.getProperty("sql.poi.add");
			jdbcTemplate.update(sql, new Object[] { request.getName(), request.getType(), request.getPhoto(),
					request.getDescription() });
		} catch (Exception e) {
			log.error("Error in addPoi:", e);
			throw new VMSException(env.getProperty("errormsg.generic"));
		}
	}

	public List<PersonOfInterest> getPoi() throws VMSException {
		try {
			String sql = env.getProperty("sql.poi.get");
			List<PersonOfInterest> poi = jdbcTemplate.query(sql,
					new GenericRowMapper<PersonOfInterest>(PersonOfInterest.class));
			return poi;
		} catch (Exception e) {
			log.error("Error in getPoi:", e);
			throw new VMSException(env.getProperty("errormsg.generic"));
		}
	}

}
