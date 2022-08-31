package com.hipsy.spring.oracle.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.hipsy.spring.oracle.model.Employee;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
	List<Employee> findByName(String name);

	@Query("select c from Employee c where c.name like ?1 AND c.last_name like ?2")
	//@Query(value = "select c from DBJAVATEST.EMPLOYEES c where c.name like %?1")
	//@Query("SELECT t FROM Employee t")
	List<Employee> findByNameAndLastName(String name,String last_name);
}
