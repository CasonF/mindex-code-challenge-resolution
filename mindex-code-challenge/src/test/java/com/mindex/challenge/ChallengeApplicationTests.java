package com.mindex.challenge;

import com.mindex.challenge.dao.EmployeeRepository;
import com.mindex.challenge.data.Compensation;
import com.mindex.challenge.data.Employee;
import com.mindex.challenge.data.txn.ReportingStructure;
import com.mindex.challenge.service.EmployeeService;
import com.mindex.challenge.service.impl.EmployeeServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ChallengeApplicationTests {
	// Unsure if you prefer to see outputs logged or printed to console with sysouts
	// I'm not opposed to either, but my workplace tends to opt for sysouts for local testing
	private static final Logger LOG = LoggerFactory.getLogger(ChallengeApplicationTests.class);

	private String employeeUrl;

	@Autowired
	private EmployeeService employeeService;

	@LocalServerPort
	private int port;

	@Autowired
	private TestRestTemplate restTemplate;

	@Before
	public void setup() {
		employeeUrl = "http://localhost:" + port + "/employee";
	}

	@Test
	public void contextLoads() {
	}

	@Test
	public void testReportingStructure() {
		// Build for our specific case
		String reportStructUrl = employeeUrl + "/{id}/report-structure";

		// John Lennon, head honcho - result = 4
		final String employeeId = "16a596ae-edd3-4847-99fe-c4518e82c86f";
		final int expectedReportsCount = 4;

		//Ringo Star, second in command - result = 2
//		final String employeeId = "03aa1462-ffa9-4978-901b-7c001562cf6f";
//		final int expectedReportsCount = 2;

		//Paul McCartney, right-hand man with no reporters = result = 0
//		final String employeeId = "b7839309-3348-463b-a7e3-5de1c168beb3";
//		final int expectedReportsCount = 0;

		ReportingStructure reportingStructure = restTemplate.getForEntity(reportStructUrl, ReportingStructure.class, employeeId).getBody();
		assertNotNull(reportingStructure);
		assertNotNull(reportingStructure.getEmployee());
		assertEquals(expectedReportsCount, reportingStructure.getNumberOfReports());

		//TODO: Add visualizer to see the actual report structure as a tree diagram?
		LOG.debug("***** Reports count for [{}] is [{}]", employeeId, reportingStructure.getNumberOfReports());

	}

	@Test
	public void testCreateReadCompensation() {
		// Build for our specific case
		String compensationUpdateUrl = employeeUrl + "/{id}/compensation/update";
		String compensationReadUrl = employeeUrl + "/{id}/compensation/read";

		Employee testEmployee = new Employee();
		testEmployee.setFirstName("John");
		testEmployee.setLastName("Doe");
		testEmployee.setDepartment("Engineering");
		testEmployee.setPosition("Developer");
		testEmployee.setCompensation(new Compensation(117200));

		// Create employee with compensation
		Employee createdEmployee = restTemplate.postForEntity(employeeUrl, testEmployee, Employee.class).getBody();

		assertNotNull(createdEmployee);
		assertNotNull(createdEmployee.getEmployeeId());
		assertNotNull(createdEmployee.getCompensation());

		// Read checks
		Compensation readCompensation = restTemplate.getForEntity(compensationReadUrl, Compensation.class, createdEmployee.getEmployeeId()).getBody();
		assertNotNull(readCompensation);
		assertEquals(readCompensation.getSalary(), createdEmployee.getCompensation().getSalary());
		assertEquals(readCompensation.getEffectiveDate(), createdEmployee.getCompensation().getEffectiveDate());

		LOG.debug("***** Initial Compensation for [{}] is [{}] starting on [{}]", createdEmployee.getEmployeeId(), readCompensation.getSalary(), readCompensation.getEffectiveDate());

		// Update checks
		Compensation updatedCompensation = new Compensation(155250, "2025-02-25");
		createdEmployee.setCompensation(updatedCompensation);

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		Employee updatedEmployee =
				restTemplate.exchange(compensationUpdateUrl,
						HttpMethod.PUT,
						new HttpEntity<Employee>(createdEmployee, headers),
						Employee.class,
						createdEmployee.getEmployeeId()).getBody();

		assertNotNull(updatedEmployee);
		assertEquals(createdEmployee.getEmployeeId(), updatedEmployee.getEmployeeId());
		assertEquals(updatedCompensation.getSalary(), updatedEmployee.getCompensation().getSalary());
		assertEquals(updatedCompensation.getEffectiveDate(), updatedEmployee.getCompensation().getEffectiveDate());

		LOG.debug("***** Updated Compensation for [{}] is [{}] starting on [{}]", updatedEmployee.getEmployeeId(), updatedEmployee.getCompensation().getSalary(), updatedEmployee.getCompensation().getEffectiveDate());

	}

}
