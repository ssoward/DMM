package com.soward.gwt.client.widget;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.SuggestOracle;

import java.util.Collections;

/**
 * Created by IntelliJ IDEA.
 * User: ssoward
 * Date: 10/29/11
 * Time: 4:04 PM
 * To change this template use File | Settings | File Templates.
 */

public abstract class AsyncSuggestOracle<T extends SuggestOracle.Suggestion> extends SuggestOracle {
    private static final int DEFAULT_MIN_CHAR = 3;
    private int minChars = DEFAULT_MIN_CHAR;

    public void setMinChars(int minChars) {
        this.minChars = minChars;
    }

    @Override
    public boolean isDisplayStringHTML() {
        return true;
    }

    public abstract void makeRequest(Request request, SuggestAsyncCallback async);

   /* @Override
    public void requestDefaultSuggestions(Request request, Callback callback) {
        Response response = new Response();

        List<Suggestion> suggestions = new ArrayList<Suggestion>();

        GroupPositionGto g = new GroupPositionGto(222222L, "Default 1", 1);
        suggestions.add(new PositionSuggestOracle.PositionSuggestion(g));
        GroupPositionGto g1 = new GroupPositionGto(222223L, "Default 2", 1);
        suggestions.add(new PositionSuggestOracle.PositionSuggestion(g1));
        GroupPositionGto g2 = new GroupPositionGto(222224L, "Default 3", 1);
        suggestions.add(new PositionSuggestOracle.PositionSuggestion(g2));
        GroupPositionGto g3 = new GroupPositionGto(222225L, "Default 4", 1);
        suggestions.add(new PositionSuggestOracle.PositionSuggestion(g3));
        GroupPositionGto g4 = new GroupPositionGto(222226L, "Default 5", 1);
        suggestions.add(new PositionSuggestOracle.PositionSuggestion(g4));

        response.setSuggestions(suggestions);

        callback.onSuggestionsReady(request, response);
    }
*/
    @Override
    public void requestSuggestions(Request request, Callback callback) {
        if (request.getQuery().length() >= minChars) {
            makeRequest(request, new SuggestAsyncCallback(request, callback));
        }
        else {
            //Just return empty response
            callback.onSuggestionsReady(request, new Response(Collections.<T>emptyList()));
        }
    }

    public class SuggestAsyncCallback implements AsyncCallback<Response> {
        private Request request;
        private Callback callback;

        SuggestAsyncCallback(Request request, Callback callback) {
            this.request = request;
            this.callback = callback;
        }

        public void onFailure(Throwable caught) {
            GWT.log("Failed to get suggestions", caught);
            callback.onSuggestionsReady(request, new Response(Collections.<T>emptyList()));
        }

        public void onSuccess(Response response) {
            callback.onSuggestionsReady(request, response);
        }
    }
}

