package com.mindex.challenge.service.impl;

import com.mindex.challenge.dao.EmployeeRepository;
import com.mindex.challenge.data.Compensation;
import com.mindex.challenge.data.Employee;
import com.mindex.challenge.data.txn.ReportingStructure;
import com.mindex.challenge.service.EmployeeService;
import io.micrometer.common.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.UUID;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    private static final Logger LOG = LoggerFactory.getLogger(EmployeeServiceImpl.class);

    @Autowired
    private EmployeeRepository employeeRepository;

    @Override
    public Employee create(Employee employee) {
        LOG.debug("Creating employee [{}]", employee);

        employee.setEmployeeId(UUID.randomUUID().toString());
        employeeRepository.insert(employee);

        return employee;
    }

    @Override
    public Employee read(String id) {
        LOG.debug("Creating employee with id [{}]", id);

        Employee employee = employeeRepository.findByEmployeeId(id);

        if (employee == null) {
            throw new RuntimeException("Invalid employeeId: " + id);
        }

        return employee;
    }

    @Override
    public Employee update(Employee employee) {
        LOG.debug("Updating employee [{}]", employee);

        return employeeRepository.save(employee);
    }

    @Override
    public ReportingStructure obtainReportingStructureByEmployeeId(String id) {
        return obtainReportingStructureByEmployeeId(id, new ReportingStructure());
    }

    public ReportingStructure obtainReportingStructureByEmployeeId(String id, ReportingStructure reportingStructure)
    {
        LOG.debug("Obtaining report structure for employee with id [{}]", id);

        Employee employee = employeeRepository.findByEmployeeId(id);

        if (employee == null) {
            throw new RuntimeException("Invalid employeeId: " + id);
        } else if (reportingStructure.getEmployee() == null) {
            reportingStructure.setEmployee(employee);
        }

        if (employee.getDirectReports() != null && !employee.getDirectReports().isEmpty()) {
            reportingStructure.addNumberOfReports(employee.getDirectReports().size());
            for(Employee reportingEmployee : employee.getDirectReports()) {
                // Using recursion... Feels not good, but most straightforward solution
                // Probably inefficient, especially with depths greater than half a dozen
                obtainReportingStructureByEmployeeId(reportingEmployee.getEmployeeId(), reportingStructure);
            }
        }

        return reportingStructure;
    }

    @Override
    public Compensation obtainEmployeeCompensation(String id) {
        LOG.debug("Obtaining compensation for employee with id [{}]", id);

        Employee employee = employeeRepository.findByEmployeeId(id);

        if (employee == null) {
            throw new RuntimeException("Invalid employeeId: " + id);
        }

        return employee.getCompensation();
    }
}
