package com.hipsy.spring.oracle.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.*;

import org.hibernate.annotations.Type;

@Entity
@Table(name = "EMPLOYEE_WORKED_HOURS")
public class EmployeeWorkedHours implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "ID")
	private long id;

	@Column(name = "EMPLOYEE_ID")
	private long employee_id;

	@Column(name = "WORKED_HOURS")
	//@Transient
	private int worked_hours;

	@Column(name = "WORKED_DATE") 
	private Date worked_date;
		
    /* @ManyToOne
    @JoinColumn(name = "employee_id", referencedColumnName = "id", insertable = false, updatable = false)
    private Employee employee; */

	public EmployeeWorkedHours() {}

	public EmployeeWorkedHours(long employee_id,int worked_hours,Date worked_date) {
		this.employee_id = employee_id;
		this.worked_hours = worked_hours;
		this.worked_date = worked_date;
	}

	public long getId() {
		return id;
	}

	public long getEmployeeId() {
		return employee_id;
	}
	public void setEmployeeId(long employee_id) {
		this.employee_id = employee_id;
	}
	
	public int getWorkedHours() {
		return worked_hours;
	}
	public void setWorkedHours(int worked_hours) {
		this.worked_hours = worked_hours;
	}
	
	public Date getWorkedDate() {
		return worked_date;
	}
	public void setWorkedDate(Date worked_date) {
		this.worked_date = worked_date;
	}

	/* @Override
	public String toString() {
		return "EmployeeWorkedHours [id=" + id + ", name=" + name+ ", salary=" + salary + "]";
	} */

}
