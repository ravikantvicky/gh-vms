package com.global.vms.model;

import java.util.HashSet;
import java.util.Set;

public class VisitorApprovalData {
	private long visitorId;
	private long status;
	private Set<Long> approvers = new HashSet<>();

	public VisitorApprovalData() {
	}

	public VisitorApprovalData(long visitorId, long status, Set<Long> approvers) {
		this.visitorId = visitorId;
		this.status = status;
		this.approvers = approvers;
	}

	public long getVisitorId() {
		return visitorId;
	}

	public void setVisitorId(long visitorId) {
		this.visitorId = visitorId;
	}

	public long getStatus() {
		return status;
	}

	public void setStatus(long status) {
		this.status = status;
	}

	public Set<Long> getApprovers() {
		return approvers;
	}

	public void setApprovers(Set<Long> approvers) {
		this.approvers = approvers;
	}
}
