package com.hipsy.spring.oracle.model;

import javax.persistence.*;

@Entity
@Table(name = "GENDERS")
public class Gender {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "ID")
	private long id;

	@Column(name = "NAME")
	private String name;

	public Gender() {

	}

	public Gender(String name) {
		this.name = name;
		
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

	@Override
	public String toString() {
		return "Gender [id=" + id + ", name=" + name + "]";
	}

}
