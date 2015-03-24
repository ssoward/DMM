package com.soward.gwt.client.widget;

/**
 * Created by IntelliJ IDEA.
 * User: ssoward
 * Date: 10/29/11
 * Time: 4:00 PM
 * To change this template use File | Settings | File Templates.
 */
public interface FieldValidator<T> {
    public String validate(FormField<T> field);
}
