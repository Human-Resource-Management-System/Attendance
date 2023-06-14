package com.models;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class EmployeeAttendanceId implements Serializable {

	@Column(name = "empl_pindex")
	private int emplPIndex;

	@Column(name = "empl_id")
	private int employeeId;

	public EmployeeAttendanceId() {
	}

	public Integer getEmplPIndex() {
		return emplPIndex;
	}

	public void setEmplPIndex(Integer emplPIndex) {
		this.emplPIndex = emplPIndex;
	}

	public Integer getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(Integer employeeId) {
		this.employeeId = employeeId;
	}

	// Implement equals and hashCode methods

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		EmployeeAttendanceId that = (EmployeeAttendanceId) o;
		return Objects.equals(emplPIndex, that.emplPIndex) && Objects.equals(employeeId, that.employeeId);
	}

	@Override
	public int hashCode() {
		return Objects.hash(emplPIndex, employeeId);
	}
}
