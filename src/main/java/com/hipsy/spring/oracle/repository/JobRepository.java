package com.hipsy.spring.oracle.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hipsy.spring.oracle.model.Job;

public interface JobRepository extends JpaRepository<Job, Long> {
	List<Job> findByName(String name);
}
