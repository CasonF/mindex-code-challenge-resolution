package com.mindex.challenge.data.txn;

import com.mindex.challenge.data.Employee;

public class ReportingStructure {
    public ReportingStructure() {}

    private Employee employee = null;
    private int numberOfReports = 0;

    public Employee getEmployee() {return employee;}
    public void setEmployee(Employee employee) {this.employee = employee;}

    public int getNumberOfReports() {return numberOfReports;}
    public void setNumberOfReports(int numberOfReports) {this.numberOfReports = numberOfReports;}
    public void addNumberOfReports(int numberOfReports) {this.numberOfReports += numberOfReports;}
}
