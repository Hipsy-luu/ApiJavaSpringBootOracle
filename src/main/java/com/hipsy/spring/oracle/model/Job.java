package com.hipsy.spring.oracle.model;

import java.util.Set;

import javax.persistence.*;

@Entity
@Table(name = "JOBS")
public class Job {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "ID")
	private long id;

	@Column(name = "NAME")
	private String name;

	@Column(name = "SALARY")
	private float salary;

    /* @OneToMany
    @JoinColumn(name = "id", referencedColumnName = "job_id", insertable = false, updatable = false)
    private Employee employees; */

	@OneToMany(mappedBy="job_id")
    private Set<Employee> employeeList;

	public Job() {}

	public Job(String name,float salary) {
		this.name = name;
		this.salary = salary;
	}

	public long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public float getSalary() {
		return salary;
	}

	public void setSalary(float salary) {
		this.salary = salary;
	}

	@Override
	public String toString() {
		return "Job [id=" + id + ", name=" + name+ ", salary=" + salary + "]";
	}

}
