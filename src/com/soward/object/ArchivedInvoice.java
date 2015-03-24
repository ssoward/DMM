package com.soward.object;

import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: ssoward
 * Date: 2/4/12
 * Time: 2:03 PM
 * To change this template use File | Settings | File Templates.
 */
public class ArchivedInvoice {
    private Long id;
    private Date date;
    private String invoiceJSON;
    private String userId;
    private String userName;
    private Long   reason;
    private String invoiceNum;
    private String additionComments;
    private String register;

    public void setInvoiceJSON(String invoiceJSON) {
        this.invoiceJSON = invoiceJSON;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setInvoiceNum(String invoiceNum) {
        this.invoiceNum = invoiceNum;
    }

    public void setAdditionComments(String additionComments) {
        this.additionComments = additionComments;
    }

    public String getInvoiceJSON() {
        return invoiceJSON;
    }

    public String getUserId() {
        return userId;
    }

    public String getUserName() {
        return userName;
    }

    public String getInvoiceNum() {
        return invoiceNum;
    }

    public String getAdditionComments() {
        return additionComments;
    }

    public Long getReason() {
        return reason;
    }

    public void setReason(Long reason) {
        this.reason = reason;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getRegister() {
        return register;
    }

    public void setRegister(String register) {
        this.register = register;
    }
}
