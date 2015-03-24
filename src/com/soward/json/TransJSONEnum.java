package com.soward.json;

/**
 * Created by IntelliJ IDEA.
 * User: ssoward
 * Date: 2/14/12
 * Time: 5:20 PM
 * To change this template use File | Settings | File Templates.
 */
public enum TransJSONEnum {
    ACCOUNT               ("account"),
    INVOICE               ("invoice"),
    INVOICENUM            ("invoiceNum"),
    LOCATIONNUM           ("locationNum"),
    PROD                  ("prod"),
    PRODUCTNAME           ("productName"),
    PRODUCTNUM            ("productNum"),
    PRODUCTQTY            ("productQty"),
    TRANSCOST             ("transCost"),
    TRANSDATE             ("transDate"),
    TRANSDATEFORMATTED    ("transDateFormatted"),
    TRANSNUM              ("transNum"),
    TRANSPRODUCTSTATUS    ("transProductStatus"),
    TRANSSHIPCOST         ("transShipCost"),
    TRANSSHIPDATE         ("transShipDate"),
    TRANSSHIPPED          ("transShipped"),
    TRANSTAX              ("transTax"),
    TRANSTYPE             ("transType"),
    TRANSYEAR             ("transYear"),
    USERNAME              ("username");

    public String label;
    TransJSONEnum(String n){
        this.label = n;
    }
}





















