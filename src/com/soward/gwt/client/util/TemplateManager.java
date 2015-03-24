package com.soward.gwt.client.util;

import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.RootPanel;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: ssoward
 * Date: 10/28/11
 * Time: 8:54 PM
 * To change this template use File | Settings | File Templates.
 */
public class TemplateManager {
    private static final String NAME_SPACE_HTML_TEMPLATE = "template_";

        private static Map<String,Object> map = new HashMap<String,Object>();

    public static Object get(String key) {
        return map.get(key);
    }

    public static void put(String key, Object value) {
        map.put(key, value);
    }

    public static void putHTMLTemplate(String key, String template) {
        put(NAME_SPACE_HTML_TEMPLATE + key, template);
    }

    public static String getHTMLTemplate(String key) {
        return (String)get(NAME_SPACE_HTML_TEMPLATE + key);
    }

    public static void loadTemplates() {
        Element templates = DOM.getElementById("htmlTemplates");
        for (int i=0; i < templates.getChildNodes().getLength(); i++) {
            Element elem = ((Element)templates.getChildNodes().getItem(i));
            String str = elem.getInnerHTML();
            if (elem.getId() != null) {
                TemplateManager.putHTMLTemplate(elem.getId(), elem.getInnerHTML());
            }
        }
        //DOM.removeChild(RootPanel.getBodyElement(), DOM.getElementById("htmlTemplates"));
    }
}
