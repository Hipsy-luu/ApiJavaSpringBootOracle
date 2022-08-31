package com.hipsy.spring.oracle.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.hipsy.spring.oracle.model.EmployeeWorkedHours;

public interface EmployeeWorkedHoursRepository extends JpaRepository<EmployeeWorkedHours, Long> {
	
	@Query("select SUM(c.worked_hours) from EmployeeWorkedHours c where c.employee_id = :employee_id AND (c.worked_date BETWEEN :start_date AND :end_date)")
	List<Object> findEmployeeWorkedRegisters(
        @Param("employee_id")  Long employee_id,
        @Param("start_date")Date start_date,
        @Param("end_date")Date end_date ); 
}
