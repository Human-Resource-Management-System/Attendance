package com.services;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dao.EmployeeAttendanceDAO;
import com.models.AttendanceEvent;
import com.models.EmployeeAttendance;
import com.models.EmployeeRequestResult;

@Service
public class EmployeeAttendanceService {
	@Autowired
	private EmployeeAttendanceDAO employeeAttendanceDAO;

	@Autowired
	private ApplicationContext context;

	private EmployeeRequestResult response;

	@Autowired
	public EmployeeAttendanceService(EmployeeRequestResult response) {
		this.response = response;
	}

	@Transactional
	public void insertEmployeeAttendance(EmployeeAttendance attendance) {
		employeeAttendanceDAO.save(attendance);

	}

	public List<AttendanceEvent> getYesterdayPunchData(int employeeId) {
		List<Object[]> results = employeeAttendanceDAO.getYesterdayPunchInAndPunchOut(employeeId);

		// Formatting the data required for the graphs
		List<AttendanceEvent> formattedEvents = new ArrayList<>();
		DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("hh:mm a");
		for (Object[] row : results) {
			LocalDateTime punchIn = (LocalDateTime) row[0];
			LocalDateTime punchOut = (LocalDateTime) row[1];

			String formattedPunchIn = punchIn.format(outputFormatter);
			String formattedPunchOut = punchOut.format(outputFormatter);

			AttendanceEvent attendanceEvent = context.getBean(AttendanceEvent.class);
			attendanceEvent.setTime(formattedPunchIn);
			attendanceEvent.setEvent("Punch In");
			formattedEvents.add(attendanceEvent);

			attendanceEvent = context.getBean(AttendanceEvent.class);
			attendanceEvent.setTime(formattedPunchOut);
			attendanceEvent.setEvent("Punch Out");
			formattedEvents.add(attendanceEvent);
		}

		for (AttendanceEvent ae : formattedEvents) {
			System.out.println(ae.getEvent() + " " + ae.getTime());
		}
		return formattedEvents;
	}

	public EmployeeRequestResult calculateAttendance(List<Object[]> punchData) {
		int daysWithMinimumHours = 0;

		Map<LocalDateTime, Duration> workingHoursPerDay = new HashMap<>();

		for (Object[] punches : punchData) {
			LocalDateTime punchIn = (LocalDateTime) punches[0];
			LocalDateTime punchOut = (LocalDateTime) punches[1];

			if (punchIn == null || punchOut == null) {
				continue; // Skip data with missing punch-in or punch-out
			}

			Duration duration = Duration.between(punchIn, punchOut);

			LocalDateTime dateOnly = punchIn.toLocalDate().atStartOfDay();

			if (workingHoursPerDay.containsKey(dateOnly)) {
				Duration totalDuration = workingHoursPerDay.get(dateOnly).plus(duration);
				workingHoursPerDay.put(dateOnly, totalDuration);
			} else {
				workingHoursPerDay.put(dateOnly, duration);
			}
		}

		for (Duration duration : workingHoursPerDay.values()) {
			long hours = duration.toHours();

			if (hours >= 8) {
				daysWithMinimumHours++;
			}
		}

		int totalDays = workingHoursPerDay.size();
		double attendancePercentage = (double) daysWithMinimumHours / totalDays * 100;

		if (Double.isNaN(attendancePercentage)) {
			attendancePercentage = 0.0;
		}

		System.out.println("no of days : " + totalDays);
		System.out.println("days with minimum hours" + daysWithMinimumHours);
		System.out.println("attendance percentage" + attendancePercentage);

		response.setDayswithminhrs(daysWithMinimumHours);
		response.setPercentage(attendancePercentage);
		response.setTotaldays(totalDays);

		return response;

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

	public List<Integer> getYears(Date joinDate) {

		LocalDate join = joinDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		LocalDate currentDate = LocalDate.now();

		List<Integer> yearList = new ArrayList<>();

		int joinYear = join.getYear();
		int currentYear = currentDate.getYear();

		while (joinYear <= currentYear) {
			yearList.add(joinYear);
			joinYear++;
		}

		return yearList;

	}
}
