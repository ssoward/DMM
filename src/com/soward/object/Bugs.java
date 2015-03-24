package com.soward.object;

public class Bugs {

    /**
     * @param args
     */
    public String bugId;
    public String bugTitle;
    public String bugReporter;
    public String bugDesc;
    public String bugStatus;
    public String bugDate;
    

    public Bugs(){
        this.bugId = "";
        this.bugDesc = "";
        this.bugReporter = "";
        this.bugStatus = ""; 
        this.bugTitle = ""; 
    }
    
    public String getBugDate() {
        return bugDate;
    }
    
    public void setBugDate( String bugDate ) {
        this.bugDate = bugDate;
    }

    public String getBugDesc() {
        return bugDesc;
    }

    public void setBugDesc( String bugDesc ) {
        this.bugDesc = bugDesc;
    }

    public String getBugId() {
        return bugId;
    }

    public void setBugId( String bugId ) {
        this.bugId = bugId;
    }

    public String getBugReporter() {
        return bugReporter;
    }

    public void setBugReporter( String bugReporter ) {
        this.bugReporter = bugReporter;
    }

    public String getBugStatus() {
        return bugStatus;
    }

    public void setBugStatus( String bugStatus ) {
        this.bugStatus = bugStatus;
    }

    public String getBugTitle() {
        return bugTitle;
    }

    public void setBugTitle( String bugTitle ) {
        this.bugTitle = bugTitle;
    }
    

}
