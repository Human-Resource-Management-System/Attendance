package com.dao;

import java.util.List;

import com.models.EmployeeAttendance;

public interface EmployeeAttendanceDAO {
	void save(EmployeeAttendance employeeAttendance);

	int getNextAttendanceRequestIndex(int employeeId);

	List<Object[]> getYesterdayPunchInAndPunchOut(int employeeId);

	List<Object[]> getPunchInAndPunchOutDataForMonthAndEmployee(int employeeId, int selectedMonth);
}
