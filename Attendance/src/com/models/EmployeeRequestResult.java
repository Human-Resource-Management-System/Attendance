package com.models;

public class EmployeeRequestResult {

	private String employeename;
	private int totaldays;
	private int dayswithminhrs;
	private double percentage;

	public void setEmployeename(String employeename) {
		this.employeename = employeename;
	}

	public EmployeeRequestResult() {
		
	}

	public String getEmployeename() {
		return employeename;
	}

	public int getTotaldays() {
		return totaldays;
	}

	public int getDayswithminhrs() {
		return dayswithminhrs;
	}

	public double getPercentage() {
		return percentage;
	}

	public void setTotaldays(int totaldays) {
		this.totaldays = totaldays;
	}

	public void setDayswithminhrs(int dayswithminhrs) {
		this.dayswithminhrs = dayswithminhrs;
	}

	public void setPercentage(double percentage) {
		this.percentage = percentage;
	}

}
