package com.soward.object;

import java.util.ArrayList;
import java.util.HashMap;

/*
 * this class if to be able to capture the column name with the value.
 * 
 */
public class InvBundle {
    String colName;

    String colValue;

    ArrayList<String> colNames = new ArrayList<String>();

    ArrayList<Invoice> invCollection = new ArrayList<Invoice>();

    HashMap<String, String> colTot = new HashMap<String, String>();

    public InvBundle() {
        this.colName = "";
        this.colValue = "";
    }

    public HashMap<String, String> getColTot() {
        return this.colTot;
    }

    public void setColTol( HashMap<String, String> amtList ) {
        this.colTot = amtList;
    }

    public ArrayList<Invoice> getInvCollection() {
        return this.invCollection;
    }

    public void setInvCollection( ArrayList<Invoice> al ) {
        this.invCollection = al;
    }

    public ArrayList<String> getColNames() {
        return this.colNames;
    }

    public void setColNames( ArrayList<String> al ) {
        this.colNames = al;
    }

    public void setColName( String str ) {
        this.colName = str;
    }

    public void setColValue( String str ) {
        this.colValue = str;
    }

    public String getColName() {
        return this.colName;
    }

    public String getColValue() {
        return this.colValue;
    }

}
