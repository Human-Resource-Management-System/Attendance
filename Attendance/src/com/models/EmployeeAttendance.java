package com.models;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "EmployeeAttendance")
public class EmployeeAttendance {
	@EmbeddedId
	private EmployeeAttendanceId attendanceId;

	@Column(name = "eatn_punch_in")
	private LocalDateTime punchIn;

	@Column(name = "eatn_punch_out")
	private LocalDateTime punchOut;

	@Column(name = "eatn_punchsystem")
	private String punchSystem;

	// Getters and Setters

	public EmployeeAttendanceId getAttendanceId() {
		return attendanceId;
	}

	public void setAttendanceId(EmployeeAttendanceId attendanceId) {
		this.attendanceId = attendanceId;
	}

	public Integer getEmployeeId() {
		return attendanceId.getEmployeeId();
	}

	public void setEmployeeId(Integer employeeId) {
		attendanceId.setEmployeeId(employeeId);
	}

	public Integer getEmplPIndex() {
		return attendanceId.getEmplPIndex();
	}

	public void setEmplPIndex(Integer emplPIndex) {
		attendanceId.setEmplPIndex(emplPIndex);
	}

	public LocalDateTime getPunchIn() {
		return punchIn;
	}

	public void setPunchIn(LocalDateTime punchIn) {
		this.punchIn = punchIn;
	}

	public LocalDateTime getPunchOut() {
		return punchOut;
	}

	public void setPunchOut(LocalDateTime punchOut) {
		this.punchOut = punchOut;
	}

	public String getPunchSystem() {
		return punchSystem;
	}

	public void setPunchSystem(String punchSystem) {
		this.punchSystem = punchSystem;
	}
}
