package com.services;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dao.EmployeeAttendanceDAO;
import com.models.AttendanceEvent;
import com.models.EmployeeAttendance;

@Service
public class EmployeeAttendanceService {
	@Autowired
	private EmployeeAttendanceDAO employeeAttendanceDAO;

	@Transactional
	public void insertEmployeeAttendance(EmployeeAttendance attendance) {
		employeeAttendanceDAO.save(attendance);

	}

	public List<AttendanceEvent> getYesterdayPunchData(int employeeId) {
		List<Object[]> results = employeeAttendanceDAO.getYesterdayPunchInAndPunchOut(employeeId);

		// fromatting the data required for the graphs
		List<AttendanceEvent> formattedEvents = new ArrayList<>();
		DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("hh:mm a");
		for (Object[] row : results) {
			LocalDateTime punchIn = (LocalDateTime) row[0];
			LocalDateTime punchOut = (LocalDateTime) row[1];

			String formattedPunchIn = punchIn.format(outputFormatter);
			String formattedPunchOut = punchOut.format(outputFormatter);

			formattedEvents.add(new AttendanceEvent(formattedPunchIn, "Punch In"));
			formattedEvents.add(new AttendanceEvent(formattedPunchOut, "Punch Out"));
		}

		for (AttendanceEvent ae : formattedEvents) {
			System.out.println(ae.getEvent() + " " + ae.getTime());
		}
		return formattedEvents;
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
