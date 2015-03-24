package com.soward.gwt.client.widget;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: ssoward
 * Date: 10/29/11
 * Time: 4:00 PM
 * To change this template use File | Settings | File Templates.
 */
public abstract class FormField<T extends Object> extends Composite {

    protected FlowPanel panel;
    protected Label label;
    protected Label errorMessageLabel;
    protected Label instructionsLabel;

    protected String labelText;
    protected List<FieldValidator> fieldValidators;

    protected ModifiedListener modifiedListener;

    public boolean validate = true;

    public FormField(String labelText, ModifiedListener listener, boolean required, FieldValidator ... validators) {
        this.labelText = labelText;
        this.modifiedListener = listener;

        List<FieldValidator> fieldValidators = new ArrayList<FieldValidator>();
        for (FieldValidator v : validators) {
            fieldValidators.add(v);
        }
        if (required) {
            fieldValidators.add(0, new RequiredFieldValidator());
        }
        this.fieldValidators = fieldValidators;

        panel = new FlowPanel();
        panel.setStyleName("formFieldWrapper");

        label = new Label();
        label.getElement().setInnerHTML((required)?("<span class='formFieldRequired'>*</span> " + labelText):labelText);
        label.setStyleName("formFieldLabel");

        errorMessageLabel = new Label();
        errorMessageLabel.setStyleName("formFieldErrorMessage");
        errorMessageLabel.setVisible(false);

        instructionsLabel = new Label();
        instructionsLabel.setStyleName("formFieldInstructions");
        instructionsLabel.setVisible(false);

    }

    public abstract T getValue();
    public abstract void setValue(T value);

    public List<String> validate(){
        List<String> errors = new ArrayList<String>();

        if (validate) {
            String firstErrorMessage = null;

            if(fieldValidators != null){
                for (FieldValidator validator : fieldValidators) {
                    String result = validator.validate(this);

                    if(result!=null){
                        errors.add(labelText + " " + result);
                        if (firstErrorMessage==null) {
                            firstErrorMessage = "This field " + result;
                        }
                    }
                }
            }

            if (firstErrorMessage!=null) {
                showError(firstErrorMessage);
            } else {
                clearError();
            }
        }
        return errors;
    }

    public void showError(String errorMessage) {
        errorMessageLabel.setText(errorMessage);
        errorMessageLabel.setVisible(errorMessage != null);
        panel.addStyleName("formFieldInError");
    }

    public void clearError() {
        errorMessageLabel.setText(null);
        errorMessageLabel.setVisible(false);
        panel.removeStyleName("formFieldInError");
    }

    public void setInstructions(String instructions) {
        instructionsLabel.setText(instructions);
        instructionsLabel.setVisible(instructions != null);
    }

    protected void modified() {
        if (modifiedListener!=null) {
            modifiedListener.onModification();
        }
    }

    public void setInlineLabel(boolean inline) {
        if (inline) {
            label.addStyleName("formFieldLabelInline");
        }
    }

    public void setInlineField(boolean inline) {
        if (inline) {
            panel.addStyleName("formFieldWrapperInline");
        }
    }

    public void enableValidation() {
        validate = true;
    }
    public void disableValidation() {
        validate = false;
        clearError();
    }

    public boolean isValidationEnabled() {
        return validate;
    }

    @Override
    public void setVisible(boolean b) {
        super.setVisible(b);
        if (b) {
            enableValidation();
        } else {
            disableValidation();
        }
    }

    abstract public void doSetEnabled(boolean b);

    abstract public void setFocus(boolean b);

    abstract public boolean isEnabled();

    public void setEnabled(boolean b) {
        if (b) {
            enableValidation();
        } else {
            disableValidation();
        }
        doSetEnabled(b);
    }

    public Label getLabel() {
        return label;
    }

    public void setLabel(Label label) {
        this.label = label;
    }
}

