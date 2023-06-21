package com.controllers;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.dao.EmployeeAttendanceDAO;
import com.dao.EmployeeDAO;
import com.google.gson.Gson;
import com.models.AttendanceEvent;
import com.models.AttendanceRequest;
import com.models.Employee;
import com.models.EmployeeAttendance;
import com.models.EmployeeAttendanceId;
import com.models.EmployeeRequestResult;
import com.services.EmployeeAttendanceService;

@Controller
public class AttendanceController {

	private EmployeeAttendance attendance;
	private EmployeeAttendanceId attendanceId;
	private Gson gson;
	private EmployeeDAO employeeDAO;

	@Autowired
	public AttendanceController(EmployeeAttendance attendance, EmployeeAttendanceId attendanceId, Gson gson,
			EmployeeDAO employeeDAO) {
		this.attendance = attendance;
		this.attendanceId = attendanceId;
		this.gson = gson;
		this.employeeDAO = employeeDAO;
	}

	@Autowired
	private EmployeeAttendanceService employeeAttendanceService;

	@Autowired
	private EmployeeAttendanceDAO employeeAttendanceDAO;

	@RequestMapping(value = "/attendanceform", method = RequestMethod.GET)
	public String attendanceform() {
		return "attendanceform";
	}

	@RequestMapping(value = "/punch", method = RequestMethod.GET)
	public String punchform() {
		return "punchdata";
	}

	// employee side attendance
	@RequestMapping(value = "/employeeAttendance", method = RequestMethod.GET)
	public String employeeAttendanceForm(HttpSession session, Model model) {

		// should handle the employee id
		session.setAttribute("employeeid", 1);
		int id = (int) session.getAttribute("employeeid");
		Employee employee = employeeDAO.getEmployee(id);
		System.out.println(employee);
		Date joinDate = employee.getEmplJondate();
		System.out.println(joinDate);
		List<Integer> yearsList = employeeAttendanceService.getYears(joinDate);

		for (Integer i : yearsList)
			System.out.println(i);

		model.addAttribute("years", yearsList);
		return "employeeAttendance";

	}

	// Admin side attendance
	@RequestMapping(value = "/adminAttendance", method = RequestMethod.GET)
	public String adminAttendanceForm() {

		return "adminAttendance";
	}

	// to get attendance data
	@RequestMapping(value = "/attendance", method = RequestMethod.POST)
	public ResponseEntity<String> attendanceData(@ModelAttribute AttendanceRequest attendanceRequest) {
		int year = attendanceRequest.getYear();
		int month = attendanceRequest.getMonth();
		int id = attendanceRequest.getEmployeeid();

		System.out.println(year);
		System.out.println(month);

		List<Object[]> results = employeeAttendanceDAO.getPunchInAndPunchOutDataForYearAndMonthAndEmployee(id, year,
				month);
		for (Object[] row : results) {
			LocalDateTime punchIn = (LocalDateTime) row[0];
			LocalDateTime punchOut = (LocalDateTime) row[1];
			System.out.println(punchIn + "  " + punchOut);
		}

		EmployeeRequestResult response = employeeAttendanceService.calculateAttendance(results);
		return ResponseEntity.ok(gson.toJson(response));
	}

	// to get punch data for graphs
	@RequestMapping(value = "/punchData", method = RequestMethod.GET)
	public ResponseEntity<String> getPunchData() {
		List<AttendanceEvent> punchData = employeeAttendanceService.getYesterdayPunchData(1);
		return ResponseEntity.status(HttpStatus.OK).body(gson.toJson(punchData));
	}

	@RequestMapping(value = "/uploadAttendance", method = RequestMethod.POST)
	public ResponseEntity<String> uploadEmployeeAttendance(@RequestParam("file") MultipartFile file) {
		try (Workbook workbook = WorkbookFactory.create(file.getInputStream())) {
			Sheet sheet = workbook.getSheetAt(0);
			Iterator<Row> rowIterator = sheet.iterator();

			rowIterator.next(); // Skip header row

			while (rowIterator.hasNext()) {
				Row row = rowIterator.next();

				// Assuming column order: employeeId, punchIn, punchOut, punchSystem

				Integer employeeId = (int) row.getCell(0).getNumericCellValue();
				LocalDateTime punchIn = employeeAttendanceService.convertToDateTime(row.getCell(1));
				LocalDateTime punchOut = employeeAttendanceService.convertToDateTime(row.getCell(2));
				String punchSystem = row.getCell(3).toString();

				attendance.setPunchIn(punchIn);
				attendance.setPunchOut(punchOut);
				attendance.setPunchSystem(punchSystem);

				attendanceId.setEmployeeId(employeeId);
				int nextIndex = employeeAttendanceDAO.getNextAttendanceRequestIndex(employeeId);
				attendanceId.setEmplPIndex(nextIndex);

				attendance.setAttendanceId(attendanceId);

				employeeAttendanceService.insertEmployeeAttendance(attendance);

			}

			// Process attendances and insert into the database using the service and DAO
		} catch (IOException e) {
			e.printStackTrace();
			String errorMessage = "Internal Server Error";
			return new ResponseEntity<>(errorMessage, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return ResponseEntity.ok("succesfully updated");
	}

	@RequestMapping(value = "/getYearsList", method = RequestMethod.POST)
	public ResponseEntity<String> getYearsOfEmployee(@ModelAttribute AttendanceRequest attendanceRequest) {
		int id = attendanceRequest.getEmployeeid();
		System.out.println(id);
		Employee employee = employeeDAO.getEmployee(id);

		if (employee == null)
			return new ResponseEntity<>("Internal Server Error", HttpStatus.INTERNAL_SERVER_ERROR);

		System.out.println(employee);
		Date joinDate = employee.getEmplJondate();
		System.out.println(joinDate);
		List<Integer> yearsList = employeeAttendanceService.getYears(joinDate);

		System.out.println(gson.toJson(yearsList));

		return ResponseEntity.status(HttpStatus.OK).body(gson.toJson(yearsList));
	}
}
