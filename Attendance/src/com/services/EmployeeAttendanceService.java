package com.services;

import java.time.LocalDateTime;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dao.EmployeeAttendanceDAO;
import com.models.EmployeeAttendance;

@Service
public class EmployeeAttendanceService {
	@Autowired
	private EmployeeAttendanceDAO employeeAttendanceDAO;

	@Transactional
	public void insertEmployeeAttendance(EmployeeAttendance attendance) {
		employeeAttendanceDAO.save(attendance);

	}

	public void getYesterdayPunchData(int employeeId) {
		employeeAttendanceDAO.getYesterdayPunchInAndPunchOut(employeeId);
	}

	public LocalDateTime convertToDateTime(Cell cell) {
		if (cell.getCellType() == CellType.NUMERIC) {
			// Assuming the cell contains a date/time value
			return cell.getLocalDateTimeCellValue();
		} else {
			// Handle other cell types or formats as needed
			return null;
		}
	}
}
