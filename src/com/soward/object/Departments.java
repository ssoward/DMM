package com.soward.object;

public class Departments {
    public String departmentCode;
    public String departmentName;
    public String departmentHead;


    public Departments(  ) {
        this.departmentCode = null;
        this.departmentHead = null;
        this.departmentName = null;
    }
    public Departments( String departmentCode, String departmentName, String departmentHead ) {
        this.departmentCode = departmentCode;
        this.departmentName = departmentName;
        this.departmentHead = departmentHead;
    }
    public String getDepartmentName() {
        return departmentName;
    }
    public void setDepartmentName( String departmentName ) {
        this.departmentName = departmentName;
    }
    public String getDepartmentCode() {
        return departmentCode;
    }
    public void setDepartmentCode( String departmentCode ) {
        this.departmentCode = departmentCode;
    }
    public String getDepartmentHead() {
        return departmentHead;
    }
    public void setDepartmentHead( String departmentHead ) {
        this.departmentHead = departmentHead;
    }

}
