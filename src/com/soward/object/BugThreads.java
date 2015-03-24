package com.soward.object;

public class BugThreads {

    public String bugThread_id;
    public String bugId;
    public String bugThread_reporter;
    public String bugThread_desc;
    public String bugThread_date;


    public BugThreads(){
        this.bugId = "";
        this.bugThread_desc = "";
        this.bugThread_reporter = "";
        this.bugThread_id = ""; 
        this.bugThread_date = "";
    }


    public String getBugId() {
        return bugId;
    }


    public void setBugId( String bugId ) {
        this.bugId = bugId;
    }


    public String getBugThread_desc() {
        return bugThread_desc;
    }


    public void setBugThread_desc( String bugThread_desc ) {
        this.bugThread_desc = bugThread_desc;
    }


    public String getBugThread_id() {
        return bugThread_id;
    }


    public void setBugThread_id( String bugThread_id ) {
        this.bugThread_id = bugThread_id;
    }


    public String getBugThread_reporter() {
        return bugThread_reporter;
    }


    public void setBugThread_reporter( String bugThread_reporter ) {
        this.bugThread_reporter = bugThread_reporter;
    }


    public String getBugThread_date() {
        return bugThread_date;
    }


    public void setBugThread_date( String bugThread_date ) {
        this.bugThread_date = bugThread_date;
    }



}
