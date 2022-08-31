package com.hipsy.spring.oracle.model;

import java.util.Date;

import javax.persistence.*;

@Entity
@Table(name = "EMPLOYEES")
public class Employee {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "ID")
	private long id;

	@Column(name = "GENDER_ID")
	private long gender_id;

	@Column(name = "JOB_ID")
	private long job_id;
		
    @ManyToOne
    @JoinColumn(name = "gender_id", referencedColumnName = "id", insertable = false, updatable = false)
    private Gender gender;

    @ManyToOne
    @JoinColumn(name = "job_id", referencedColumnName = "id", insertable = false, updatable = false)
    private Job job;

	@Column(name = "NAME")
	private String name;

	@Column(name = "LAST_NAME")
	private String last_name;

	@Column(name = "BIRTHDATE")
	private Date birthdate;

	public Employee() {}

	public Employee(long gender_id,long job_id,String name,String last_name,Date birthdate) {
		this.gender_id = gender_id;
		this.job_id = job_id;
		this.name = name;
		this.last_name = last_name;
		this.birthdate = birthdate;
	}

	public long getId() {
		return id;
	}

	public Long getGenderId() {
		return gender_id;
	}
	public void setGenderId(long gender_id) {
		this.gender_id = gender_id;
	}

	public long getJobId() {
		return job_id;
	}
	public void setJobId(long job_id) {
		this.job_id = job_id;
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	public String getLastName() {
		return last_name;
	}
	public void setLastName(String last_name) {
		this.last_name = last_name;
	}

	public Date getBirthdate() {
		return birthdate;
	}
	public void setBirthdate(Date birthdate) {
		this.birthdate = birthdate;
	}

	/* @Override
	public String toString() {
		return "Employee [id=" + id + ", name=" + name+ ", salary=" + salary + "]";
	} */

}

