package com.hipsy.spring.oracle.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hipsy.spring.oracle.model.Gender;

public interface GenderRepository extends JpaRepository<Gender, Long> {
	List<Gender> findByName(String name);
}
