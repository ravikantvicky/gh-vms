package com.global.vms.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import com.global.vms.exception.VMSException;
import com.global.vms.model.ApproveVisitorRequest;
import com.global.vms.model.InsideVisitor;
import com.global.vms.model.LocationAccessResponse;
import com.global.vms.model.LoginResponse;
import com.global.vms.model.RoleVisitorAccess;
import com.global.vms.model.TodaysVisitors;
import com.global.vms.model.VisitorApprovalData;
import com.global.vms.model.VisitorExceedOutTime;
import com.global.vms.model.VisitorProfileResponse;
import com.global.vms.model.VisitorsLastDay;
import com.global.vms.model.VisitorsResponse;

@Repository
public class VMSRepository {
	private Logger log = LoggerFactory.getLogger(this.getClass());
	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Autowired
	Environment env;

	public LoginResponse login(String email, String passwordHash) throws VMSException {
		LoginResponse response = null;
		try {
			String sql = env.getProperty("sql.login");
			List<LoginResponse> resp = jdbcTemplate.query(sql, new Object[] { email, passwordHash },
					new GenericRowMapper<LoginResponse>(LoginResponse.class));
			if (resp.size() > 0) {
				response = resp.get(0);
				response.setVisitorTypeAccess(
						jdbcTemplate.query(env.getProperty("sql.login.access"), new Object[] { response.getUserId() },
								new GenericRowMapper<RoleVisitorAccess>(RoleVisitorAccess.class)));
			}
		} catch (Exception e) {
			log.error("Error in login", e);
			throw new VMSException(env.getProperty("errormsg.generic"));
		}
		if (response == null)
			throw new VMSException(env.getProperty("errormsg.login"));
		return response;
	}

	public VisitorsResponse fetchVisitors() throws VMSException {
		VisitorsResponse response = new VisitorsResponse();
		try {
			String sql = env.getProperty("sql.visitor.today");
			List<TodaysVisitors> todaysVisitors = jdbcTemplate.query(sql,
					new GenericRowMapper<TodaysVisitors>(TodaysVisitors.class));
			sql = env.getProperty("sql.visitor.lastDays");
			List<VisitorsLastDay> visitorLastDay = jdbcTemplate.query(sql,
					new GenericRowMapper<VisitorsLastDay>(VisitorsLastDay.class));
			response.setTodaysVisitors(todaysVisitors.size() > 0 ? todaysVisitors.get(0) : null);
			response.setVisitorLastDays(visitorLastDay);
		} catch (Exception e) {
			log.error("Error in Visitors fetch:", e);
			throw new VMSException(env.getProperty("errormsg.generic"));
		}
		return response;
	}

	public VisitorProfileResponse getVisitorProfile(long visitorId) throws VMSException {
		VisitorProfileResponse response = new VisitorProfileResponse();
		try {
			String sql = env.getProperty("sql.visitor.profile");
			List<VisitorProfileResponse> resp = jdbcTemplate.query(sql, new Object[] { visitorId },
					new GenericRowMapper<VisitorProfileResponse>(VisitorProfileResponse.class));
			response = resp.size() > 0 ? resp.get(0) : null;
		} catch (Exception e) {
			log.error("Error in Visitors fetch:", e);
			throw new VMSException(env.getProperty("errormsg.generic"));
		}
		return response;
	}

	public void checkApproverEligible(long visitorId, long empId) throws VMSException {
		try {
			String sql = env.getProperty("sql.approver.eligibility");
			VisitorApprovalData approvalData = jdbcTemplate.query(sql, new Object[] { visitorId },
					new ResultSetExtractor<VisitorApprovalData>() {

						@Override
						public VisitorApprovalData extractData(ResultSet rs) throws SQLException, DataAccessException {
							VisitorApprovalData result = new VisitorApprovalData();
							while (rs.next()) {
								result.setVisitorId(rs.getLong("id"));
								result.setStatus(rs.getLong("status"));
								if (rs.getString("refered_by") != null && rs.getLong("refered_by") > 0)
									result.getApprovers().add(rs.getLong("refered_by"));
								result.getApprovers().add(rs.getLong("df_approver"));
							}
							return result;
						}
					});
			if (approvalData == null || approvalData.getVisitorId() == 0
					|| !approvalData.getApprovers().contains(empId))
				throw new VMSException(env.getProperty("errormsg.invalidInput"));

			if (approvalData.getStatus() != env.getProperty("status.visitor.pendingApproval", Long.class))
				throw new VMSException(env.getProperty("errormsg.approval.alreadyApproved"));

		} catch (VMSException e) {
			throw e;
		} catch (Exception e) {
			log.error("Error in Visitor approval eligibility check:", e);
			throw new VMSException(env.getProperty("errormsg.approval"));
		}
	}

	public void approveVisitor(long visitorId, long approverId) throws VMSException {
		try {
			String sql = env.getProperty("sql.visitor.approve");
			jdbcTemplate.update(new PreparedStatementCreator() {

				@Override
				public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
					PreparedStatement ps = con.prepareStatement(sql);
					ps.setLong(1, env.getProperty("status.visitor.initial", Long.class));
					ps.setLong(2, approverId);
					ps.setLong(3, visitorId);
					return ps;
				}
			});
		} catch (Exception e) {
			log.error("Error in Visitor approval:", e);
			throw new VMSException(env.getProperty("errormsg.approval"));
		}
	}

	public List<InsideVisitor> allInsideVisitors(String baseUrl) throws VMSException {
		try {
			String sql = env.getProperty("sql.visitor.inside");
			return jdbcTemplate.query(sql, new Object[] { baseUrl + "/images/" },
					new GenericRowMapper<InsideVisitor>(InsideVisitor.class));
		} catch (Exception e) {
			log.error("Error in allInsideVisitors:", e);
			throw new VMSException(env.getProperty("errormsg.generic"));
		}
	}

	public List<VisitorExceedOutTime> getVisitorsExceedOutTime() {
		try {
			String sql = env.getProperty("sql.visitor.exceedOutTime");
			return jdbcTemplate.query(sql, new GenericRowMapper<VisitorExceedOutTime>(VisitorExceedOutTime.class));
		} catch (Exception e) {
			log.error("Error in getVisitorsExceedOutTime:", e);
			return null;
		}
	}

	public long insertImage(String imageData) throws VMSException {
		try {
			GeneratedKeyHolder holder = new GeneratedKeyHolder();
			jdbcTemplate.update(new PreparedStatementCreator() {

				@Override
				public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
					PreparedStatement ps = con.prepareStatement(env.getProperty("sql.images.insert"),
							Statement.RETURN_GENERATED_KEYS);
					ps.setString(1, imageData);
					return ps;
				}
			}, holder);
			return holder.getKey().longValue();
		} catch (Exception e) {
			log.error("Error in Inserting Image:", e);
			throw new VMSException(env.getProperty("errormsg.generic"));
		}
	}

	public void approveVisitorBySecurity(ApproveVisitorRequest request) throws VMSException {
		try {
			long imageId = insertImage(request.getVisitorPhoto());
			String sql = env.getProperty("sql.visitor.approve.security");
			jdbcTemplate.update(new PreparedStatementCreator() {

				@Override
				public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
					PreparedStatement ps = con.prepareStatement(sql);
					ps.setLong(1, imageId);
					ps.setLong(2, env.getProperty("status.visitor.inside", Long.class));
					ps.setLong(3, request.getVisitorId());
					return ps;
				}
			});
			addVisitorLog(request.getVisitorId(), request.getSecurityId(),
					env.getProperty("eventtype.entry", Long.class));
		} catch (Exception e) {
			log.error("Error in approveVisitorBySecurity:", e);
			throw new VMSException(env.getProperty("errormsg.generic"));
		}
	}

	public void addVisitorLog(long visitorId, long securityId, long eventType) throws VMSException {
		try {
			String sql = env.getProperty("sql.visitor.log.insert");
			jdbcTemplate.update(new PreparedStatementCreator() {

				@Override
				public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
					PreparedStatement ps = con.prepareStatement(sql);
					ps.setLong(1, visitorId);
					ps.setLong(2, eventType);
					ps.setLong(3, securityId);
					return ps;
				}
			});
		} catch (Exception e) {
			log.error("Error in addVisitorLog:", e);
			throw new VMSException(env.getProperty("errormsg.generic"));
		}
	}

	public void updateStatus(long visitorId, long securityId, long statusCode) throws VMSException {
		try {
			String sql = (statusCode == env.getProperty("status.visitor.visitComplete", Long.class)
					? env.getProperty("sql.visitor.finalExit")
					: env.getProperty("sql.visitor.updateStatus"));
			jdbcTemplate.update(new PreparedStatementCreator() {

				@Override
				public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
					PreparedStatement ps = con.prepareStatement(sql);
					ps.setLong(1, statusCode);
					ps.setLong(2, visitorId);
					return ps;
				}
			});
			long eventType = (statusCode == env.getProperty("status.visitor.visitComplete", Long.class)
					|| statusCode == env.getProperty("status.visitor.tempOut", Long.class)
							? env.getProperty("eventtype.exit", Long.class)
							: env.getProperty("eventtype.entry", Long.class));
			addVisitorLog(visitorId, securityId, eventType);
		} catch (Exception e) {
			log.error("Error in updateStatus:", e);
			throw new VMSException(env.getProperty("errormsg.generic"));
		}
	}

	public long getSecurityLocationId(long securityId) throws VMSException {
		try {
			String sql = env.getProperty("sql.security.location");
			return jdbcTemplate.query(sql, new Object[] {securityId}, new ResultSetExtractor<Long>() {

				@Override
				public Long extractData(ResultSet rs) throws SQLException, DataAccessException {
					if (rs.next())
						return rs.getLong("location_id");
					return 0l;
				}});
		} catch (Exception e) {
			log.error("Error in getSecurityLocationId:", e);
			throw new VMSException(env.getProperty("errormsg.generic"));
		}
	}
	
	public LocationAccessResponse visitorLocations(long visitorId, long locationId, String imageBaseUrl) throws VMSException {
		try {
			String sql = env.getProperty("sql.visitor.allowedLocations");
			return jdbcTemplate.query(sql, new Object[] {imageBaseUrl, visitorId}, new ResultSetExtractor<LocationAccessResponse>() {

				@Override
				public LocationAccessResponse extractData(ResultSet rs) throws SQLException, DataAccessException {
					LocationAccessResponse res = new LocationAccessResponse();
					res.setAllowed(false);
					while (rs.next()) {
						res.setName(rs.getString("name"));
						res.setVisitorType(rs.getString("visitor_type"));
						res.setPhoto(rs.getString("photo"));
						if (res.getAllowedLocations() == null)
							res.setAllowedLocations(new ArrayList<String>());
						res.getAllowedLocations().add(rs.getString("allowed_location"));
						if (locationId == rs.getLong("location_code")) {
							res.setAllowed(true);
							res.setCurrentLocation(rs.getString("allowed_location"));
						}
					}
					return res;
				}});
		} catch (Exception e) {
			log.error("Error in visitorLocations:", e);
			throw new VMSException(env.getProperty("errormsg.generic"));
		}
	}
}
