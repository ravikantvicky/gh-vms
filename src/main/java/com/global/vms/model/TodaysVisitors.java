package com.global.vms.model;

import com.global.vms.annotation.DbColumn;

public class TodaysVisitors {
	@DbColumn
	private int total;
	@DbColumn
	private int inside;
	@DbColumn
	private int remaining;

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public int getInside() {
		return inside;
	}

	public void setInside(int inside) {
		this.inside = inside;
	}

	public int getRemaining() {
		return remaining;
	}

	public void setRemaining(int remaining) {
		this.remaining = remaining;
	}
}
