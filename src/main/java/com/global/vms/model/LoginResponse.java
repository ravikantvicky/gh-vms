package com.global.vms.model;

import java.util.List;

import com.global.vms.annotation.DbColumn;

public class LoginResponse {
	@DbColumn("id")
	private long userId;
	@DbColumn("name")
	private String userName;
	@DbColumn
	private String email;
	@DbColumn("role_code")
	private String userRole;
	private String token;
	private List<RoleVisitorAccess> visitorTypeAccess;
	private List<EmployeeFamily> employeeFamily;

	public LoginResponse() {
	}

	public LoginResponse(long userId, String userName, String email, String userRole, String token,
			List<RoleVisitorAccess> visitorTypeAccess, List<EmployeeFamily> employeeFamily) {
		this.userId = userId;
		this.userName = userName;
		this.email = email;
		this.userRole = userRole;
		this.token = token;
		this.visitorTypeAccess = visitorTypeAccess;
		this.employeeFamily = employeeFamily;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getUserRole() {
		return userRole;
	}

	public void setUserRole(String userRole) {
		this.userRole = userRole;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public List<RoleVisitorAccess> getVisitorTypeAccess() {
		return visitorTypeAccess;
	}

	public void setVisitorTypeAccess(List<RoleVisitorAccess> visitorTypeAccess) {
		this.visitorTypeAccess = visitorTypeAccess;
	}

	public List<EmployeeFamily> getEmployeeFamily() {
		return employeeFamily;
	}

	public void setEmployeeFamily(List<EmployeeFamily> employeeFamily) {
		this.employeeFamily = employeeFamily;
	}
}
