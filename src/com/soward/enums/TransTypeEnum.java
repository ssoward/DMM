package com.soward.enums;

/**
 * Created by IntelliJ IDEA.
 * User: ssoward
 * Date: 2/15/12
 * Time: 9:06 PM
 * To change this template use File | Settings | File Templates.
 */
public enum TransTypeEnum {
    NOT_FOUND("NF", "Not Found"),
    ONLINE("00", "Online"),
    DROP("DS", "Drop"),
    HOLD_BIN("HB", "Hold Bin"),
    NORMAL("NS", "Normal"),
    SMA("SMA", "SheetMusicAuth"),
    WEB("WS", "Web");

    public String code;
    public String label;

    TransTypeEnum(String c, String l){
        this.code = c;
        this.label = l;
    }


    public static TransTypeEnum getValueForCode(String transType) {
        for(TransTypeEnum t: TransTypeEnum.values()){
            if(t.code.equals(transType)){
                return t;
            }
        }
        return null;
    }
    public static void main(String arg[]){
        System.out.println(TransTypeEnum.getValueForCode("NS"));
    }
}
