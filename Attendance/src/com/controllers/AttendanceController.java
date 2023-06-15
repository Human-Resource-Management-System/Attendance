package com.controllers;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.dao.EmployeeAttendanceDAO;
import com.models.AttendanceEvent;
import com.models.EmployeeAttendance;
import com.models.EmployeeAttendanceId;
import com.services.EmployeeAttendanceService;

@Controller
public class AttendanceController {

	@Autowired
	private EmployeeAttendanceService employeeAttendanceService;

	@Autowired
	private EmployeeAttendanceDAO employeeAttendanceDAO;

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String attendanceform() {
		return "attendanceform";
	}

	@RequestMapping(value = "/punch", method = RequestMethod.GET)
	public String punchdataform() {
		return "punchdata";
	}

	@RequestMapping(value = "/punchData", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public List<AttendanceEvent> getPunchData() {
		List<AttendanceEvent> formattedData = employeeAttendanceService.getYesterdayPunchData(1);
		return formattedData;
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

				EmployeeAttendance attendance = new EmployeeAttendance();
				attendance.setPunchIn(punchIn);
				attendance.setPunchOut(punchOut);
				attendance.setPunchSystem(punchSystem);

				EmployeeAttendanceId attendanceId = attendance.getAttendanceId();
				if (attendanceId == null) {
					attendanceId = new EmployeeAttendanceId();
					attendanceId.setEmployeeId(employeeId);
					attendance.setAttendanceId(attendanceId);
				} else if (attendanceId.getEmployeeId() == 0) {
					attendanceId.setEmployeeId(employeeId);
				}

				int nextIndex = employeeAttendanceDAO.getNextAttendanceRequestIndex(employeeId);
				attendanceId.setEmplPIndex(nextIndex);

				employeeAttendanceService.insertEmployeeAttendance(attendance);

			}

			// Process attendances and insert into the database using the service and DAO
		} catch (IOException e) {
			e.printStackTrace();
			return (ResponseEntity<String>) ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return ResponseEntity.ok("succesfully updated");
	}

}
