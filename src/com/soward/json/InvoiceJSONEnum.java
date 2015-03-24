package com.soward.json;

/**
 * Created by IntelliJ IDEA.
 * User: ssoward
 * Date: 2/14/12
 * Time: 1:29 PM
 * To change this template use File | Settings | File Templates.
 */
public enum InvoiceJSONEnum {
    ACCOUNT                             ("account"),
    ACCOUNTNUM                          ("accountNum"),
    COLNAMES                            ("colNames"),
    DATEONE                             ("dateOne"),
    DATETWO                             ("dateTwo"),
    INVOICECHARGEDATE                   ("invoiceChargeDate"),
    INVOICECHARGEPAYMENTMETHOD          ("invoiceChargePaymentMethod"),
    INVOICECHARGESTATUS                 ("invoiceChargeStatus"),
    INVOICECONTACTNUM                   ("invoiceContactNum"),
    INVOICEDATE                         ("invoiceDate"),
    INVOICEDISCOUNT                     ("invoiceDiscount"),
    INVOICEDISCOUNTSUM                  ("invoiceDiscountSum"),
    INVOICENUM                          ("invoiceNum"),
    INVOICEPAID                         ("invoicePaid"),
    INVOICEPAID1                        ("invoicePaid1"),
    INVOICEPAID1SUM                     ("invoicePaid1Sum"),
    INVOICEPAID2                        ("invoicePaid2"),
    INVOICEPAID2SUM                     ("invoicePaid2Sum"),
    INVOICEPAIDSUM                      ("invoicePaidSum"),
    INVOICERECEIVEDBY                   ("invoiceReceivedBy"),
    INVOICEREFERENCENUM                 ("invoiceReferenceNum"),
    INVOICESHIPTOTAL                    ("invoiceShipTotal"),
    INVOICETAX                          ("invoiceTax"),
    INVOICETAXSUM                       ("invoiceTaxSum"),
    INVOICETOTAL                        ("invoiceTotal"),
    INVOICETOTALBD                      ("invoiceTotalBD"),
    INVOICETOTALSHIPBD                  ("invoiceTotalShipBD"),
    INVOICETOTALSUM                     ("invoiceTotalSum"),
    INVOICETOTALTAXBD                   ("invoiceTotalTaxBD"),
    INVOICEUNIXDATE                     ("invoiceUnixDate"),
    LOCATIONNUM                         ("locationNum"),
    PAYSTATUS                           ("payStatus"),
    PAYMENTMETHOD1                      ("paymentMethod1"),
    PAYMENTMETHOD2                      ("paymentMethod2"),
    TRANSLIST                           ("transList"),
    USERNAME2                           ("username2");

    public String label;
    InvoiceJSONEnum(String n){
        this.label = n;
    }
}

