package com.implementations;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.dao.EmployeeAttendanceDAO;
import com.models.EmployeeAttendance;

@Repository
public class EmployeeAttendanceDAOImpl implements EmployeeAttendanceDAO {
	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public void save(EmployeeAttendance employeeAttendance) {
		entityManager.persist(employeeAttendance);
	}

	@Override
	public int getNextAttendanceRequestIndex(int employeeId) {
		String queryString = "SELECT COALESCE(MAX(ea.attendanceId.emplPIndex), 0) + 1 " + "FROM EmployeeAttendance ea "
				+ "WHERE ea.attendanceId.employeeId = :employeeId";
		Query query = entityManager.createQuery(queryString);
		query.setParameter("employeeId", employeeId);
		return (Integer) query.getSingleResult();
	}
}
