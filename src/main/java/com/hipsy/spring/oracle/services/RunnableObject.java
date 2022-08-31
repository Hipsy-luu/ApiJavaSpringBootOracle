package com.hipsy.spring.oracle.services;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import com.hipsy.spring.oracle.model.Employee;
import com.hipsy.spring.oracle.repository.EmployeeRepository;

public class RunnableObject implements Runnable {
	
	Long id;
	EmployeeRepository employeeRepository;
    List<Object> employeesDataShared;
	
	public RunnableObject(EmployeeRepository employeeRepository,Long id,List<Object> employeesDataShared) {
		this.id = id;
		this.employeeRepository = employeeRepository;
		this.employeesDataShared = employeesDataShared;
	}

	@Override
	public void run() {
		//System.out.println("Started thread for id = " + id);

        Optional<Employee> employeeData = employeeRepository.findById( this.id );

        if (employeeData.isPresent()) {
            this.employeesDataShared.add(employeeData);
        }
		
		//System.out.println("Ended thread for id = " + id);
	}
}
