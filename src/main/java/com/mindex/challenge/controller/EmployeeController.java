package com.mindex.challenge.controller;

import com.mindex.challenge.data.Compensation;
import com.mindex.challenge.data.Employee;
import com.mindex.challenge.data.txn.ReportingStructure;
import com.mindex.challenge.service.EmployeeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EmployeeController {
    private static final Logger LOG = LoggerFactory.getLogger(EmployeeController.class);

    @Autowired
    private EmployeeService employeeService;

    @PostMapping("/employee")
    public Employee create(@RequestBody Employee employee) {
        LOG.debug("Received employee create request for [{}]", employee);

        return employeeService.create(employee);
    }

    @GetMapping("/employee/{id}")
    public Employee read(@PathVariable String id) {
        LOG.debug("Received employee create request for id [{}]", id);

        return employeeService.read(id);
    }

    @PutMapping("/employee/{id}")
    public Employee update(@PathVariable String id, @RequestBody Employee employee) {
        LOG.debug("Received employee create request for id [{}] and employee [{}]", id, employee);

        employee.setEmployeeId(id);
        return employeeService.update(employee);
    }

    /**
     * TASK 1: This new type should have a new REST endpoint created for it.
     * This new endpoint should accept an employeeId and return the fully filled
     * out ReportingStructure for the specified employeeId.
     * The values should be computed on the fly and will not be persisted.
     */
    // Same general url structure
    @GetMapping("/employee/{id}/report-structure")
    public ReportingStructure obtainReportingStructureByEmployeeId(@PathVariable String id) {
        LOG.debug("Received report structure obtain request for id [{}]", id);

        return employeeService.obtainReportingStructureByEmployeeId(id);
    }

    /**
     * TASK 2: Create two new REST endpoints to create and read Compensation information
     * from the database. These endpoints should persist and fetch Compensation data
     * for a specific Employee using the persistence layer.
     */
    @PutMapping("/employee/{id}/compensation/update")
    public Employee updateEmployeeCompensation(@PathVariable String id, @RequestBody Employee employee) {
        //This reads just like the main Employee update request...
        LOG.debug("Received employee compensation update request for [{}]", id);

        employee.setEmployeeId(id);
        return employeeService.update(employee);
    }

    @GetMapping("/employee/{id}/compensation/read")
    public Compensation obtainEmployeeCompensation(@PathVariable String id) {
        LOG.debug("Received compensation obtain request for id [{}]", id);

        return employeeService.obtainEmployeeCompensation(id);
    }
}
