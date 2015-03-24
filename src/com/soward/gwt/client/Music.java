package com.soward.gwt.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.*;
import com.google.gwt.user.datepicker.client.DateBox;
import com.soward.gwt.client.util.TemplateManager;
import com.soward.gwt.client.widget.CustomerSuggestOracle;
import com.soward.gwt.client.widget.FormSuggestBox;

public class Music implements EntryPoint {
    private HTMLPanel template;
    private Button button;
    private Label label;
    private DateBox datePicker;
    private FormSuggestBox<CustomerSuggestOracle.CustomerSuggestion> customer;


    private TextBox text;

    public void onModuleLoad() {
        TemplateManager.loadTemplates();

        template = new HTMLPanel(TemplateManager.getHTMLTemplate("locationRegisterSales"));

        button = new Button("Click me");
        button.addStyleName("btn");
        label = new Label();
        text = new TextBox();
        datePicker = new DateBox();
        CustomerSuggestOracle customerSuggestOracle = new CustomerSuggestOracle();
        customer = new FormSuggestBox<CustomerSuggestOracle.CustomerSuggestion>("ID", "SomeName", customerSuggestOracle, null, false);



        button.addClickHandler(new ClickHandler() {
            public void onClick(ClickEvent event) {
                if (label.getText().equals("")) {
                    String input = text.getValue();
                    SortService.App.getInstance().getMessage(input, new MyAsyncCallback());
                } else {
                    label.setText("");
                    text.setVisible(true);
                    text.setValue("");
                    button.setText("Click me");
                }
            }
        });

//        template.add(button    , "_button"    );
//        template.add(label     , "_label"     );
//        template.add(text      , "_text"      );
//        template.add(datePicker, "_datePicker");

        RootPanel.get("_button").add(button);
        RootPanel.get("_label").add(label);
        RootPanel.get("_text").add(text);
        RootPanel.get("_datePicker").add(datePicker);
        RootPanel.get("_suggest").add(customer);

//        Element templates = RootPanel.getBodyElement();
//        String t = templates.getInnerHTML();
//
//        RootPanel.get("registerSales"     ).add(template);
//        RootPanel.get("runReport"     ).add(button);

//        RootPanel.get("messageContent").add(label);
//        RootPanel.get("resultsContent").add(text);
//        RootPanel.get("calendar"      ).add(datePicker);
    }

    private class MyAsyncCallback implements AsyncCallback<String> {


        public void onSuccess(String result) {
            label.getElement().setInnerHTML(result);
            button.setText("Clear");
            //label.removeStyleName(label.getStyleName());
            text.setVisible(false);
        }

        public void onFailure(Throwable throwable) {
            button.setText("Clear");
            text.setVisible(false);
            //label.removeStyleName(label.getStyleName());
            label.setText("Failed to receive answer from server!");
        }
    }

}

