package com.soward.gwt.client.widget;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.google.gwt.user.client.ui.SuggestOracle;

/**
 * Created by IntelliJ IDEA.
 * User: ssoward
 * Date: 10/29/11
 * Time: 4:02 PM
 * To change this template use File | Settings | File Templates.
 */
public class CustomerSuggestOracle extends AsyncSuggestOracle<CustomerSuggestOracle.CustomerSuggestion>{

    public Long userId;
    public Long groupId;

    public CustomerSuggestOracle(){

    }
    public CustomerSuggestOracle(Long uid, Long gl){
        this.userId = uid;
        this.groupId = gl;
    }

    @Override
    public void makeRequest(SuggestOracle.Request request, SuggestAsyncCallback async) {
        if(request!=null){
            String hh = request.getQuery();
            if(hh!=null){
               //got here!!!
            }
        }
//        ReportServiceRpcAsync reportService = ReportServiceRpc.App.getInstance();
//        reportService.getGroupsForUserReports(userId, groupId, request, async);
    }


    public static class CustomerSuggestion implements IsSerializable, SuggestOracle.Suggestion{
//        private GroupGto group;

        public CustomerSuggestion() {}

//        public GroupSuggestion(GroupGto group) {
//            this.group = group;
//        }
//
//        public GroupGto getGroup() {
//            return group;
//        }

        public String getDisplayString() {
            String html = "<div id='group-suggestion'><span>" +
//                    group.getName() +
                    "</span></div>";
            return html;
        }

        public String getReplacementString() {
            return "";
        }
    }
}
