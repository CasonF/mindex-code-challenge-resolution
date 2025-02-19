package com.mindex.challenge.data;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Compensation {
    public Compensation(){}
    public Compensation(int salary) {
        this.salary = salary;
        this.effectiveDate = new Date();
    }
    public Compensation(int salary, Date effectiveDate) {
        this.salary = salary;
        this.effectiveDate = effectiveDate;
    }
    public Compensation(int salary, String effectiveDate) {
        this.salary = salary;
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            this.effectiveDate = formatter.parse(effectiveDate);
        } catch(ParseException e) {
            this.effectiveDate = new Date();
        }
    }

    //Don't feel like dealing with floats or longs on this one...
    private int salary = 92500;
    private Date effectiveDate = new Date();

    public int getSalary() {return salary;}
    public void setSalary(int salary) {this.salary = salary;}

    public Date getEffectiveDate() {return effectiveDate;}
    public void setEffectiveDate(Date effectiveDate) {this.effectiveDate = effectiveDate;}
}
