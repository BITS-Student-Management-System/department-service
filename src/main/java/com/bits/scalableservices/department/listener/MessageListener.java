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

@Component
@EnableJms
public class MessageListener {

	@Autowired
	DepartmentRepository departmentRepository;

	@JmsListener(destination = "department-queue")
	public void updateStudentCount(String jsonInString) {
		ObjectMapper mapper = new ObjectMapper();
		try {
			Long departmentId = mapper.readValue(jsonInString, Long.class);
			Optional<Department> departmentRecord = departmentRepository.findById(departmentId);
			departmentRecord.ifPresent(dept -> {
				Department department = departmentRecord.get();
				int numberofStudents = department.getNumberOfStudents();
				++numberofStudents;
				department.setNumberOfStudents(numberofStudents);
			});
			departmentRepository.save(departmentRecord.get());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
