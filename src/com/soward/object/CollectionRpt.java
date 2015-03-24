
/**
 * @author Scott Soward
 * Date: May 17, 2007
 * 
 */
package com.soward.object;

import java.util.ArrayList;
/*
 * class to contain the various outstanding invoices: 0-30, 31-60, 61-90, 90-*
 */
public class CollectionRpt {
    ArrayList<Invoice> coll_00_30;
    ArrayList<Invoice> coll_31_60;
    ArrayList<Invoice> coll_61_90;
    ArrayList<Invoice> coll_91_up;
    double coll_00_30Sum;
    double coll_31_60Sum;
    double coll_61_90Sum;
    double coll_91_upSum;
    double collSum;
    
    Account acct;
    


    public CollectionRpt( ArrayList<Invoice> coll_00_30, ArrayList<Invoice> coll_31_60, ArrayList<Invoice> coll_61_90, ArrayList<Invoice> coll_91_up, int coll_00_30Sum, int coll_31_60Sum, int coll_61_90Sum, int coll_91_upSum, Account acct ) {
        this.coll_00_30 = coll_00_30;
        this.coll_31_60 = coll_31_60;
        this.coll_61_90 = coll_61_90;
        this.coll_91_up = coll_91_up;
        this.coll_00_30Sum = coll_00_30Sum;
        this.coll_31_60Sum = coll_31_60Sum;
        this.coll_61_90Sum = coll_61_90Sum;
        this.coll_91_upSum = coll_91_upSum;
        this.acct = acct;
    }



    public CollectionRpt() {
        this.coll_00_30 = new ArrayList<Invoice>();
        this.coll_31_60 = new ArrayList<Invoice>();
        this.coll_61_90 = new ArrayList<Invoice>();
        this.coll_91_up = new ArrayList<Invoice>();
        this.coll_00_30Sum = 0.0;
        this.coll_31_60Sum = 0.0;
        this.coll_61_90Sum = 0.0;
        this.coll_91_upSum = 0.0;
        this.collSum = 0.0;
        this.acct = new Account();
    }



    public static void main( String[] args ) {
        // TODO Auto-generated method stub

    }



    /**
     * @return the coll_00_30
     */
    public ArrayList<Invoice> getColl_00_30() {
        return coll_00_30;
    }



    /**
     * @param coll_00_30 the coll_00_30 to set
     */
    public void setColl_00_30( ArrayList<Invoice> coll_00_30 ) {
        this.coll_00_30 = coll_00_30;
    }



    /**
     * @return the coll_31_60
     */
    public ArrayList<Invoice> getColl_31_60() {
        return coll_31_60;
    }



    /**
     * @param coll_31_60 the coll_31_60 to set
     */
    public void setColl_31_60( ArrayList<Invoice> coll_31_60 ) {
        this.coll_31_60 = coll_31_60;
    }



    /**
     * @return the coll_61_90
     */
    public ArrayList<Invoice> getColl_61_90() {
        return coll_61_90;
    }



    /**
     * @param coll_61_90 the coll_61_90 to set
     */
    public void setColl_61_90( ArrayList<Invoice> coll_61_90 ) {
        this.coll_61_90 = coll_61_90;
    }



    /**
     * @return the coll_91_up
     */
    public ArrayList<Invoice> getColl_91_up() {
        return coll_91_up;
    }



    /**
     * @param coll_91_up the coll_91_up to set
     */
    /**
     * @param coll_91_up
     */
    public void setColl_91_up( ArrayList<Invoice> coll_91_up ) {
        this.coll_91_up = coll_91_up;
    }



    /**
     * @return the acct
     */
    public Account getAcct() {
        return acct;
    }



    /**
     * @param acct the acct to set
     */
    public void setAcct( Account acct ) {
        this.acct = acct;
    }



    /**
     * @return the coll_00_30Sum
     */
    public double getColl_00_30Sum() {
        return coll_00_30Sum;
    }



    /**
     * @param coll_00_30Sum the coll_00_30Sum to set
     */
    public void setColl_00_30Sum( double coll_00_30Sum ) {
        this.coll_00_30Sum = coll_00_30Sum;
    }



    /**
     * @return the coll_31_60Sum
     */
    public double getColl_31_60Sum() {
        return coll_31_60Sum;
    }



    /**
     * @param coll_31_60Sum the coll_31_60Sum to set
     */
    public void setColl_31_60Sum( double coll_31_60Sum ) {
        this.coll_31_60Sum = coll_31_60Sum;
    }



    /**
     * @return the coll_61_90Sum
     */
    public double getColl_61_90Sum() {
        return coll_61_90Sum;
    }



    /**
     * @param coll_61_90Sum the coll_61_90Sum to set
     */
    public void setColl_61_90Sum( double coll_61_90Sum ) {
        this.coll_61_90Sum = coll_61_90Sum;
    }



    /**
     * @return the coll_91_upSum
     */
    public double getcoll_91_upSum() {
        return coll_91_upSum;
    }



    /**
     * @param coll_91_upSum the coll_91_upSum to set
     */
    public void setColl_91_upSum( double coll_91_upSum ) {
        this.coll_91_upSum = coll_91_upSum;
    }



    /**
     * @return the collSum
     */
    public double getCollSum() {
        return collSum;
    }



    /**
     * @param collSum the collSum to set
     */
    public void setCollSum( double collSum ) {
        this.collSum = collSum;
    }



    public CollectionRpt( ArrayList<Invoice> coll_00_30, ArrayList<Invoice> coll_31_60, ArrayList<Invoice> coll_61_90, ArrayList<Invoice> coll_91_up, double coll_00_30Sum, double coll_31_60Sum, double coll_61_90Sum, double coll_91_upSum, double collSum, Account acct ) {
        this.coll_00_30 = coll_00_30;
        this.coll_31_60 = coll_31_60;
        this.coll_61_90 = coll_61_90;
        this.coll_91_up = coll_91_up;
        this.coll_00_30Sum = coll_00_30Sum;
        this.coll_31_60Sum = coll_31_60Sum;
        this.coll_61_90Sum = coll_61_90Sum;
        this.coll_91_upSum = coll_91_upSum;
        this.collSum = collSum;
        this.acct = acct;
    }



    
}
