package com.global.vms.model;

import com.global.vms.annotation.DbColumn;

public class VisitorsLastDay {
	@DbColumn("date")
	private String date;
	@DbColumn
	private long count;

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public long getCount() {
		return count;
	}

	public void setCount(long count) {
		this.count = count;
	}
}
