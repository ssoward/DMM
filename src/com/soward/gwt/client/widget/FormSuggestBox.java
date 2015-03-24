package com.soward.gwt.client.widget;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.SuggestOracle;

/**
 * Created by IntelliJ IDEA.
 * User: ssoward
 * Date: 10/29/11
 * Time: 3:59 PM
 * To change this template use File | Settings | File Templates.
 */

public class FormSuggestBox<T extends SuggestOracle.Suggestion> extends FormField<String> {
    private SuggestBox box;

    T selectedSuggestion;
    private CheckBox enablerCheckBox;

    private boolean showEnabler;

    public FormSuggestBox(String id, String labelText, SuggestOracle oracle, ModifiedListener listener, boolean required, FieldValidator... validators) {
        this(id, labelText, null, oracle, listener, required, false, validators);
    }

    public FormSuggestBox(String id, String labelText, String styleName, SuggestOracle oracle, ModifiedListener listener, boolean required, FieldValidator ... validators) {
        this(id, labelText, null, oracle, listener, required, false, validators);
    }

    public FormSuggestBox(String id, String labelText, String styleName, SuggestOracle oracle, ModifiedListener listener, boolean required, boolean showEnabler, FieldValidator ... validators) {
        super(labelText, listener, required, validators);

        this.showEnabler = showEnabler;

        if (styleName!=null) {
            panel.addStyleName(styleName);
        }

        panel.add(label);

        box = new SuggestBox(oracle);
        box.setLimit(10);

        //TODO, how can I use Generics to make this better???
        box.addSelectionHandler(new SelectionHandler<SuggestOracle.Suggestion>() {
            public void onSelection(SelectionEvent<SuggestOracle.Suggestion> suggestionSelectionEvent) {
                selectedSuggestion = (T) suggestionSelectionEvent.getSelectedItem();
//                Window.alert("selectHandler fired");
            }
        });


        box.getElement().setId(id);
        box.setStyleName("formFieldSuggestBox");
        box.addKeyPressHandler(new KeyPressHandler() {
            public void onKeyPress(KeyPressEvent keyPressEvent) {
                modified();
            }
        });

        if (showEnabler) {

            setEnabled(false); // when the enabler is shown, the field MUST be disabled by default

            // create the enabler checkbox
            enablerCheckBox = new CheckBox();
            enablerCheckBox.setStyleName("formFieldEnablerCheckBox");
            enablerCheckBox.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
                public void onValueChange(ValueChangeEvent<Boolean> booleanValueChangeEvent) {
                    if (!booleanValueChangeEvent.getValue()) {
                        setValue(null);
                    }
                    setEnabled(booleanValueChangeEvent.getValue());
                }
            });
            box.getTextBox().addClickHandler(new ClickHandler() {
                public void onClick(ClickEvent event) {
                    if (!isEnabled()) { // if the field is clicked on in the disabled state...
                        setEnabled(true); // enable the field
                        enablerCheckBox.setValue(true); // check the enabler checkbox
                    }
                }
            });

            panel.add(enablerCheckBox);
        }

        panel.add(box);

        panel.add(instructionsLabel);

        panel.add(errorMessageLabel);

        initWidget(panel);
    }

    public void setSuggestionLimit(int limit){
        this.box.setLimit(limit);
    }

    public SuggestBox getSuggestBox() {
        return box;
    }

    public String getValue() {
        String hello = box.getValue();
        box.getTabIndex();
        box.getText();
        return box.getValue().trim().length() != 0 ? box.getValue() : null;
    }

    public void setValue(String value) {
        selectedSuggestion = null;
        box.setValue(value, true);
        //TODO make sure the selected suggestion gets set based on the new string, or rework to use an ID?
    }

    public T getSelectedSuggestion() {
        if (selectedSuggestion != null && selectedSuggestion.getReplacementString() != null && selectedSuggestion.getReplacementString().equals(box.getValue())) {
            return selectedSuggestion;
        }
        return null;
    }

    public boolean isSelectedSuggestion() {
        return getSelectedSuggestion() != null;
    }

    public void setValueModified(String value) {
        setValue(value);
        modified();
    }

    public void setName(String name) {
        box.getTextBox().setName(name);
    }

    public void doSetEnabled(boolean b) {
        box.getTextBox().setEnabled(b);
    }

    public boolean isEnabled() {
        return box.getTextBox().isEnabled();
    }

    public void setSelectedSuggestion(String personName, T personSuggestion) {
        selectedSuggestion = personSuggestion;
        box.setValue(personName, false);
    }

    public void addSelectionHandler(SelectionHandler handler){
        box.addSelectionHandler(handler);
    }

    public boolean isEnablerChecked() {
        if (showEnabler) {
            return enablerCheckBox.getValue();
        }
        return false;
    }

    public void setEnablerChecked(boolean value) {
        if (showEnabler) {
            enablerCheckBox.setValue(value, true);
        }
    }

    public void setFocus(boolean focused) {
        if(showEnabler){
            enablerCheckBox.setFocus(focused);
        }else{
            box.setFocus(focused);
        }
    }
}

