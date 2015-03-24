/**
 *
 */
package com.soward.object;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.omg.IOP.TransactionService;

/**
 * @author amorvivir
 *
 */
public class Invoice implements Serializable {

    public String invoiceNum;
    public String accountNum;
    public String invoiceDate;
    public String locationNum;
    public String username2;
    public String invoiceTotal;
    public String invoiceTax;
    public String invoiceShipTotal;
    public String invoicePaid;
    public String paymentMethod1;
    public String paymentMethod2;
    public String invoicePaid1;
    public String invoicePaid2;
    public String invoiceReceivedBy;
    public String invoiceContactNum;
    public String invoiceReferenceNum;
    public String invoiceChargeStatus;
    public String invoiceChargeDate;
    public String invoiceChargePaymentMethod;
    public String invoiceDiscount;
    public String invoiceUnixDate;
    public String payStatus;
    public String dateOne;
    public String dateTwo;
    public String invoiceTotalSum;
    public String invoicePaidSum;
    public String invoicePaid1Sum;
    public String invoicePaid2Sum;
    public String invoiceDiscountSum;
    public String invoiceTaxSum;
    public Account account;
    public List<Transaction> transList;
    public ArrayList<String> colNames = new ArrayList<String>();
    public BigDecimal invoiceTotalBD;
    public BigDecimal invoiceTotalTaxBD;
    public BigDecimal invoiceTotalShipBD;

    public Invoice() {
        this.account = new Account();
        this.invoiceNum = "";
        this.accountNum = "";
        this.invoiceDate = "";
        this.locationNum = "";
        this.username2 = "";
        this.invoiceTotal = "";
        this.invoiceTax = "";
        this.invoiceShipTotal = "";
        this.invoicePaid = "";
        this.paymentMethod1 = "";
        this.paymentMethod2 = "";
        this.invoicePaid1 = "";
        this.invoicePaid2 = "";
        this.invoiceReceivedBy = "";
        this.invoiceContactNum = "";
        this.invoiceReferenceNum = "";
        this.invoiceChargeStatus = "";
        this.invoiceChargeDate = "";
        this.invoiceChargePaymentMethod = "";
        this.invoiceDiscount = "";
        this.payStatus = "";
        this.dateOne = "";
        this.dateTwo = "";
        this.invoiceTotalSum= "0.0";
        this.invoicePaidSum= "0.0";
        this.invoicePaid1Sum= "0.0";
        this.invoicePaid2Sum= "0.0";
        this.invoiceDiscountSum= "0.0";
        this.invoiceTaxSum= "";
        this.transList = new ArrayList<Transaction>();
    }
    public void setInvoiceTotalSum(String str)   {this.invoiceTotalSum= str;}
    public void setInvoicePaidSum(String str)    {this.invoicePaidSum= str;}
    public void setInvoicePaid1Sum(String str)   {this.invoicePaid1Sum=str;}
    public void setInvoicePaid2Sum(String str)   {this.invoicePaid2Sum=str;}
    public void setInvoiceDiscountSum(String str){this.invoiceDiscountSum= str;}
    public void setInvoiceTaxSum(String str)     {this.invoiceTaxSum= str;}
    public void setAccount(Account str)     {this.account= str;}
    public void setTransList(List<Transaction> arg){this.transList = arg;}

    public String getInvoiceTotalSum()   {return this.invoiceTotalSum;}
    public String getInvoicePaidSum()    {return this.invoicePaidSum;}
    public String getInvoicePaid1Sum()   {return this.invoicePaid1Sum;}
    public String getInvoicePaid2Sum()   {return this.invoicePaid2Sum;}
    public String getInvoiceDiscountSum(){return this.invoiceDiscountSum;}
    public String getInvoiceTaxSum()     {return this.invoiceTaxSum;}
    public Account getAccount()     {return this.account;}
    public List<Transaction>getTransList(){return this.transList;}




    public ArrayList<String> getColNames() {
        return this.colNames;
    }

    public void setColNames( ArrayList<String> al ) {
        this.colNames = al;
    }

    public String getInvoiceNum() {
        return this.invoiceNum;
    }

    public String getAccountNum() {
        return this.accountNum;
    }

    public String getInvoiceDate() {
        return this.invoiceDate;
    }

    public String getLocationNum() {
        return this.locationNum;
    }

    public String getUsername2() {
        return this.username2;
    }

    public String getInvoiceTotal() {
        return this.invoiceTotal;
    }

    public String getInvoiceTax() {
        return this.invoiceTax;
    }

    public String getInvoiceShipTotal() {
        return this.invoiceShipTotal;
    }

    public String getInvoicePaid() {
        return this.invoicePaid;
    }

    public String getPaymentMethod1() {
        return this.paymentMethod1;
    }

    public String getPaymentMethod2() {
        return this.paymentMethod2;
    }

    public String getInvoicePaid1() {
        return this.invoicePaid1;
    }

    public String getInvoicePaid2() {
        return this.invoicePaid2;
    }

    public String getInvoiceReceivedBy() {
        return this.invoiceReceivedBy;
    }

    public String getInvoiceContactNum() {
        return this.invoiceContactNum;
    }

    public String getInvoiceReferenceNum() {
        return this.invoiceReferenceNum;
    }

    public String getInvoiceChargeStatus() {
        return this.invoiceChargeStatus;
    }

    public String getInvoiceChargeDate() {
        return this.invoiceChargeDate;
    }

    public String getInvoiceChargePaymentMethod() {
        return this.invoiceChargePaymentMethod;
    }

    public String getInvoiceDiscount() {
        return this.invoiceDiscount;
    }

    public String getPayStatus() {
        return this.payStatus;
    }

    public String getDateOne() {
        return this.dateOne;
    }

    public String getDateTwo() {
        return this.dateTwo;
    }

    public void setInvoiceNum( String str ) {
        this.invoiceNum = str;
    }

    public void setAccountNum( String str ) {
        this.accountNum = str;
    }

    public void setInvoiceDate( String str ) {
        this.invoiceDate = str;
    }

    public void setLocationNum( String str ) {
        this.locationNum = str;
    }

    public void setUsername2( String str ) {
        this.username2 = str;
    }

    public void setInvoiceTotal( String str ) {
        this.invoiceTotal = str;
    }

    public void setInvoiceTax( String str ) {
        this.invoiceTax = str;
    }

    public void setInvoiceShipTotal( String str ) {
        this.invoiceShipTotal = str;
    }

    public void setInvoicePaid( String str ) {
        this.invoicePaid = str;
    }

    public void setPaymentMethod1( String str ) {
        this.paymentMethod1 = str;
    }

    public void setPaymentMethod2( String str ) {
        this.paymentMethod2 = str;
    }

    public void setInvoicePaid1( String str ) {
        this.invoicePaid1 = str;
    }

    public void setInvoicePaid2( String str ) {
        this.invoicePaid2 = str;
    }

    public void setInvoiceReceivedBy( String str ) {
        this.invoiceReceivedBy = str;
    }

    public void setInvoiceContactNum( String str ) {
        this.invoiceContactNum = str;
    }

    public void setInvoiceReferenceNum( String str ) {
        this.invoiceReferenceNum = str;
    }

    public void setInvoiceChargeStatus( String str ) {
        this.invoiceChargeStatus = str;
    }

    public void setInvoiceChargeDate( String str ) {
        this.invoiceChargeDate = str;
    }

    public void setInvoiceChargePaymentMethod( String str ) {
        this.invoiceChargePaymentMethod = str;
    }

    public void setInvoiceDiscount( String str ) {
        this.invoiceDiscount = str;
    }

    public void setPayStatus( String str ) {
        this.payStatus = str;
    }

    public void setDateOne( String str ) {
        this.dateOne = str;
    }

    public void setDateTwo( String str ) {
        this.dateTwo = str;
    }

    /**
     * @param args
     */
    public static void main( String[] args ) {
        // TODO Auto-generated method stub

    }
    public void setInvoiceUnixDate( String string ) {
        this.invoiceUnixDate = string;
    }
    /**
     * @return the invoiceUnixDate
     */
    public String getInvoiceUnixDate() {
        return invoiceUnixDate;
    }
    public BigDecimal getInvoiceTotalBD() {
        return invoiceTotalBD;
    }
    public void setInvoiceTotalBD( BigDecimal invoiceTotalBD ) {
        this.invoiceTotalBD = invoiceTotalBD;
    }
    public BigDecimal getInvoiceTotalTaxBD() {
        return invoiceTotalTaxBD;
    }
    public void setInvoiceTotalTaxBD( BigDecimal invoiceTotalTaxBD ) {
        this.invoiceTotalTaxBD = invoiceTotalTaxBD;
    }
    public BigDecimal getInvoiceTotalShipBD() {
        return invoiceTotalShipBD;
    }
    public void setInvoiceTotalShipBD( BigDecimal invoiceTotalShipBD ) {
        this.invoiceTotalShipBD = invoiceTotalShipBD;
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer("");
        sb.append("Invoice[");
        sb.append("invoiceNum="+                 this.invoiceNum                );
        sb.append(",accountNum="+                this.accountNum                );
        sb.append(",invoiceDate="+               this.invoiceDate               );
        sb.append(",locationNum="+               this.locationNum               );
        sb.append(",username2="+                 this.username2                 );
        sb.append(",invoiceTotal="+              this.invoiceTotal              );
        sb.append(",invoiceTax="+                this.invoiceTax                );
        sb.append(",invoiceShipTotal="+          this.invoiceShipTotal          );
        sb.append(",invoicePaid="+               this.invoicePaid               );
        sb.append(",paymentMethod1="+            this.paymentMethod1            );
        sb.append(",paymentMethod2="+            this.paymentMethod2            );
        sb.append(",invoicePaid1="+              this.invoicePaid1              );
        sb.append(",invoicePaid2="+              this.invoicePaid2              );
        sb.append(",invoiceReceivedBy="+         this.invoiceReceivedBy         );
        sb.append(",invoiceContactNum="+         this.invoiceContactNum         );
        sb.append(",invoiceReferenceNum="+       this.invoiceReferenceNum       );
        sb.append(",invoiceChargeStatus="+       this.invoiceChargeStatus       );
        sb.append(",invoiceChargeDate="+         this.invoiceChargeDate         );
        sb.append(",invoiceChargePaymentMethod="+this.invoiceChargePaymentMethod);
        sb.append(",invoiceDiscount="+           this.invoiceDiscount           );
        sb.append(",payStatus="+                 this.payStatus                 );
        sb.append(",dateOne="+                   this.dateOne                   );
        sb.append(",dateTwo="+                   this.dateTwo                   );
        sb.append(",invoiceTotalSum="+           this.invoiceTotalSum           );
        sb.append(",invoicePaidSum="+            this.invoicePaidSum            );
        sb.append(",invoicePaid1Sum="+           this.invoicePaid1Sum           );
        sb.append(",invoicePaid2Sum="+           this.invoicePaid2Sum           );
        sb.append(",invoiceDiscountSum="+        this.invoiceDiscountSum        );
        sb.append(",invoiceTaxSum="+             this.invoiceTaxSum             );
        sb.append(",transList="+                 this.transList                 );
        sb.append("]");
        return sb.toString();
    }
}






























