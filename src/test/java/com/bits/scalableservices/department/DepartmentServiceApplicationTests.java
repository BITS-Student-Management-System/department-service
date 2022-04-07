package com.bits.scalableservices.department;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.Optional;

import org.apache.activemq.broker.BrokerService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.core.JmsTemplate;

import com.bits.scalableservices.department.config.JMSConfig;
import com.bits.scalableservices.department.entity.Department;
import com.bits.scalableservices.department.repository.DepartmentRepository;

@EnableAutoConfiguration
@Import({ JMSConfig.class, TestConfig.class })
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@EnableJms
class DepartmentServiceApplicationTests {

	@LocalServerPort
	int randomServerPort;

	@Autowired
	private JmsTemplate jmsTemplate;

	@Autowired
	private DepartmentRepository departmentRepository;

	@Test
	public void testReceiveMessage() {

		Department department = new Department();
		department.setDepartmentId(1l);
		department.setNumberOfStudents(1);
		departmentRepository.save(department);
		try {

			jmsTemplate.convertAndSend("department-queue", 1l);
			Thread.sleep(3000L);
			Optional<Department> savedDept = departmentRepository.findById(1l);
			assertTrue(savedDept.isPresent());
			assertEquals(2, savedDept.get().getNumberOfStudents());
		} catch (InterruptedException e) {
			fail();
		}

	}

}

@Configuration
class TestConfig {

	@Bean
	public BrokerService broker() throws Exception {
		BrokerService broker = new BrokerService();
		broker.addConnector("tcp://localhost:61616");
		broker.setPersistent(false);
		return broker;
	}
}
