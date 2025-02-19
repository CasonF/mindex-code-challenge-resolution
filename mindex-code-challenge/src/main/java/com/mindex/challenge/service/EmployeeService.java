package com.mindex.challenge.service;

import com.mindex.challenge.data.Compensation;
import com.mindex.challenge.data.Employee;
import com.mindex.challenge.data.txn.ReportingStructure;

public interface EmployeeService {
    Employee create(Employee employee);
    Employee read(String id);
    Employee update(Employee employee);

    //TASK 1
    ReportingStructure obtainReportingStructureByEmployeeId(String id);

    //TASK 2
    Compensation obtainEmployeeCompensation(String id);
}
