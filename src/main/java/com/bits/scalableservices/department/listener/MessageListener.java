package com.bits.scalableservices.department.listener;

import java.io.IOException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import com.bits.scalableservices.department.entity.Department;
import com.bits.scalableservices.department.repository.DepartmentRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Component
@EnableJms
@Slf4j
public class MessageListener {

	@Autowired
	DepartmentRepository departmentRepository;

	@JmsListener(destination = "department-queue")
	public void updateStudentCount(String jsonInString) {
		log.info("Received message with value " + jsonInString);
		ObjectMapper mapper = new ObjectMapper();
		try {
			Long departmentId = mapper.readValue(jsonInString, Long.class);
			Optional<Department> departmentRecord = departmentRepository.findById(departmentId);
			departmentRecord.ifPresent(dept -> {
				log.info("Found department with id " + dept.getDepartmentId() + " with number of students "
						+ dept.getNumberOfStudents());
				Department department = departmentRecord.get();
				int numberofStudents = department.getNumberOfStudents();
				++numberofStudents;
				department.setNumberOfStudents(numberofStudents);
				departmentRepository.save(departmentRecord.get());
				log.info("Total number of students in " + dept.getDepartmentId() + " : " + dept.getNumberOfStudents());
			});

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
