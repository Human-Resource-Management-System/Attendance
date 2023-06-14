package com.implementations;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

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

	@Override
	public void getYesterdayPunchInAndPunchOut(int employeeId) {
		LocalDate yesterday = LocalDate.now().minusDays(1);
		LocalDateTime startOfDay = LocalDateTime.of(yesterday, LocalTime.MIN);
		LocalDateTime endOfDay = LocalDateTime.of(yesterday, LocalTime.MAX);

		String queryString = "SELECT ea.punchIn, ea.punchOut FROM EmployeeAttendance ea "
				+ "WHERE ea.attendanceId.employeeId = :employeeId " + "AND ea.punchIn >= :startOfDay "
				+ "AND ea.punchOut <= :endOfDay";

		TypedQuery<Object[]> query = entityManager.createQuery(queryString, Object[].class);
		query.setParameter("employeeId", employeeId);
		query.setParameter("startOfDay", startOfDay);
		query.setParameter("endOfDay", endOfDay);

		List<Object[]> results = query.getResultList();
		for (Object[] row : results) {
			LocalDateTime punchIn = (LocalDateTime) row[0];
			LocalDateTime punchOut = (LocalDateTime) row[1];
			System.out.println("Punch In: " + punchIn);
			System.out.println("Punch Out: " + punchOut);
		}

	}
}
