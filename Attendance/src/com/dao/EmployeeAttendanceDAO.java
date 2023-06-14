package com.dao;

import com.models.EmployeeAttendance;

public interface EmployeeAttendanceDAO {
	void save(EmployeeAttendance employeeAttendance);

	int getNextAttendanceRequestIndex(int employeeId);
}
