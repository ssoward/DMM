package com.soward.enums;

/**
 * Created by IntelliJ IDEA.
 * User: ssoward
 * Date: 2/2/12
 * Time: 9:38 AM
 * To change this template use File | Settings | File Templates.
 */
public enum SearchTypeEnum {
    ACCOUNTNAME   (1l, "Account Name"),
    EMAIL         (2l, "Email"),
    ZIPCODE       (3l, "Zip Code"),
    ACCOUNTNUMBER (4l, "Account Number"),
    CITY          (5l, "City"),
    STATE         (6l, "State");

    private Long id;
    private String name;

    SearchTypeEnum(Long id, String name){
        this.id = id;
        this.name = name;

    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public static SearchTypeEnum valueForName(String searchType) {
        for(SearchTypeEnum en: SearchTypeEnum.values()){
            if(en.getName().equals(searchType)){
                return en;
            }
        }
        return null;
    }
}
