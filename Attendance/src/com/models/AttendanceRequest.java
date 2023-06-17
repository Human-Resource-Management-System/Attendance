package com.models;

public class AttendanceRequest {
	private int employeeid;
    public int getEmployeeid() {
		return employeeid;
	}
	public void setEmployeeid(int employeeid) {
		this.employeeid = employeeid;
	}
	private int year;
    private int month;
    
	public AttendanceRequest(int year, int month , int employeeid) {
		this.year = year;
		this.month = month;
		this.employeeid = employeeid;
	}
	public AttendanceRequest() {
	}
	public int getYear() {
		return year;
	}
	public void setYear(int year) {
		this.year = year;
	}
	public int getMonth() {
		return month;
	}
	public void setMonth(int month) {
		this.month = month;
	}
}
