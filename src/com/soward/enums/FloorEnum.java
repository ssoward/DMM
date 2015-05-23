package com.soward.enums;

/**
 * Created by ssoward on 5/23/15.
 */
public enum FloorEnum {
    //this is derived from the location table

   DEFAULT         ("0"  ,"0.0.0.0"     ,"System Defaults"),
   HANDEL_SERVER   ("1"  ,"handel"      ,"Handel Server"),
   BAD_NAME        ("2"  ,"Bad Location","Bad Description"),
   OFFICE          ("3"  ,"office"      ,"Main Office"),
   RECEIVING       ("4"  ,"receiving"   ,"Receiving"),
   WEST_REGISTER   ("5"  ,"regwest"     ,"West Register"),
   INVENTORY       ("6"  ,"inventory"   ,"Inventory Machine"),
   EAST_REGISTER   ("7"  ,"regeast"     ,"East Register"),
   SHIPPING        ("8"  ,"shipping"    ,"Shipping"),
   NORTH_REGISTER  ("9"  ,"regnorth"    ,"North Register"),
   LEHI            ("20" ,"lehi"        ,"Lehi Store"),
   OREM            ("30" ,"Orem"        ,"Orem Store"),
   ASSISTANT       ("40" ,"assistant"   ,"Assistant"),
   BALANCING       ("41" ,"balancing"   ,"Balancing"),
   BUYER           ("42" ,"buyer"       ,"Buyer"),
   BRAHMS          ("43" ,"brahms"      ,"Brahms"),
   FINANCE         ("44" ,"finance"     ,"Finance"),
   LEHI_LAPTOP     ("45" ,"lehilap"     ,"Lehi Laptop"),
   SOUTH_REGISTER  ("46" ,"regsouth"    ,"South Register"),
   SCHOOLS         ("47" ,"schools"     ,"Schools"),
   STUDIOS         ("48" ,"studios"     ,"Studios"),
   OREM_NORTH      ("49" ,"oremnorth"   ,"Orem North"),
   OREM_SOUTH      ("50" ,"oremsouth"   ,"Orem South"),
   ONLINE_SALES    ("99" ,"Online"      ,"Online Web Customers"),
   ANYWHERE        ("100","anywhere"    ,"User can log in from any location");

    private String locationNum;
    private String locationName;
    private String locationDescription;

    FloorEnum(String num, String name, String desc){
        this.locationNum = num;
        this.locationName = name;
        this.locationDescription = desc;
    }

    public static FloorEnum getForName(String reason) {
        for(FloorEnum en: FloorEnum.values()){
            if(en.locationNum.equals(reason)){
                return en;
            }
        }
        return null;
    }

    public String getLocationNum() {
        return locationNum;
    }

    public String getLocationName() {
        return locationName;
    }

    public String getLocationDescription() {
        return locationDescription;
    }
}
