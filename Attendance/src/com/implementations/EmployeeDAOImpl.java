package com.implementations;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;

import com.dao.EmployeeDAO;
import com.models.Employee;

@Repository
public class EmployeeDAOImpl implements EmployeeDAO {
	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public Employee getEmployee(int employeeId) {
		return entityManager.find(Employee.class, employeeId);
	}

}