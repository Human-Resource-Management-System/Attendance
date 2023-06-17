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
	public List<Object[]> getYesterdayPunchInAndPunchOut(int employeeId) {
		LocalDate yesterday = LocalDate.now().minusDays(1);
		LocalDateTime startOfDay = LocalDateTime.of(yesterday, LocalTime.MIN);
		LocalDateTime endOfDay = LocalDateTime.of(yesterday, LocalTime.MAX);

		// query to get punch data from the 00:00 to 23:59:59 of yesterday
		String queryString = "SELECT ea.punchIn, ea.punchOut FROM EmployeeAttendance ea "
				+ "WHERE ea.attendanceId.employeeId = :employeeId " + "AND ea.punchIn >= :startOfDay "
				+ "AND ea.punchOut <= :endOfDay";

		TypedQuery<Object[]> query = entityManager.createQuery(queryString, Object[].class);
		query.setParameter("employeeId", employeeId);
		query.setParameter("startOfDay", startOfDay);
		query.setParameter("endOfDay", endOfDay);

		List<Object[]> results = query.getResultList();

		return results;

	}

	@Override
	public List<Object[]> getPunchInAndPunchOutDataForYearAndMonthAndEmployee(int employeeId, int selectedYear, int selectedMonth) {
	    String queryString = "SELECT ea.punchIn, ea.punchOut FROM EmployeeAttendance ea "
	            + "WHERE ea.attendanceId.employeeId = :employeeId "
	            + "AND YEAR(ea.punchIn) = :selectedYear "
	            + "AND MONTH(ea.punchIn) = :selectedMonth";

	    Query query = entityManager.createQuery(queryString);
	    query.setParameter("employeeId", employeeId);
	    query.setParameter("selectedYear", selectedYear);
	    query.setParameter("selectedMonth", selectedMonth);

	    List<Object[]> results = query.getResultList();
	    return results;
	}


}
