package com.soward.enums;

/**
 * Created by IntelliJ IDEA.
 * User: ssoward
 * Date: 2/4/12
 * Time: 9:34 AM
 * To change this template use File | Settings | File Templates.
 */
public enum DeleteInvoiceEnum {
    USER_ERROR (1l, "Employee Error"),
    CUSTOMER_ERROR (2l, "Customer Error"),
    DUPLICATE_INVOICE(3l, "Duplicate Invoice"),
    ONLINE_INVOICE(4l, "Online Invoice");

    private String name;
    private Long id;

    DeleteInvoiceEnum(Long i, String n){
        this.id = i;
        this.name = n;
    }

    public String getName() {
        return name;
    }

    public Long getId() {
        return id;
    }

    public static DeleteInvoiceEnum getForName(String reason) {
        for(DeleteInvoiceEnum en: DeleteInvoiceEnum.values()){
            if(en.getName().equals(reason)){
                return en;
            }
        }
        return null;
    }

    public static DeleteInvoiceEnum getForId(Long reason) {
        for(DeleteInvoiceEnum en: DeleteInvoiceEnum.values()){
            if(en.getId().equals(reason)){
                return en;
            }
        }
        return null;
    }
}
