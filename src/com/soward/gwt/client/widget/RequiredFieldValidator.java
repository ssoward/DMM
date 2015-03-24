package com.soward.gwt.client.widget;

/**
 * Created by IntelliJ IDEA.
 * User: ssoward
 * Date: 10/29/11
 * Time: 4:01 PM
 * To change this template use File | Settings | File Templates.
 */
public class RequiredFieldValidator implements FieldValidator {
    public String validate(FormField field) {
        if(field.getValue() == null) {
            return "is required";
        }
        return null;
    }
}