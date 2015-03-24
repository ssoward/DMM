package com.soward.enums;

/**
 * Created by IntelliJ IDEA.
 * User: ssoward
 * Date: 1/3/12
 * Time: 9:30 PM
 * To change this template use File | Settings | File Templates.
 */
public enum Department {
    DEFAULT              ( "0", "0 Default"),
    SHEET_MUSIC          ( "1", "Sheet Music"),
    CHORAL               ( "2", "Choral"),
    KARAOKE              ( "3", "Karaoke"),
    STUDIOS              ( "4", "Studios"),
    RECITAL_HALL         ( "5", "Recital Hall"),
    SOFTWARE             ( "6", "Software"),
    INSTRUMENT           ( "7", "Instrument"),
    INSTRUMENT_ACCESSORY ( "8", "Instrument Accessory"),
    ACCESSORY            ( "9", "Accessory"),
    BLANK1               ( "A", ""),
    BAND                 ( "B", "Band"),
    STRING_ORCHESTRA     ( "C", "String Orchestra"),
    CANDY                ( "D", "Candy"),
    BLANK2               ( "E", ""),
    FEDERATION           ( "F", "Federation"),
    GIFT_CERTIFICATES    ( "G", "Gift Certificates"),
    HANDLING             ( "H", "Handling"),
    CD                   ( "I", "CD"),
    PIANO_GALLERY        ( "L", "Piano Gallery"),
    MUSIC_SERVICES       ( "M", "Music Services"),
    ORGAN_SCHOOL         ( "O", "Organ School"),
    POSTAGE              ( "P", "postage"),
    INSTRUMENT_RENTALS   ( "R", "Instrument Rentals"),
    SYMPHONY             ( "S", "Symphony"),
    HANDLING_TAXABLE     ( "T", "Handling - Taxable"),
    DAYVIOLINS           ( "V", "DAY VIOLINS");

    public String code;
    public String name;

    Department(String c, String n){
        this.code = c;
        this.name = n;
    }

    public static String getNameForCat(String cat){
        if(cat!=null){
            String c = cat.charAt(0)+"";
            for(Department e: values()){
                if(c.toUpperCase().equals(e.code)){
                    return e.getName();
                }
            }

        }
        return null;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    public static void main(String args[]){
        System.out.println(Department.getNameForCat("1"));
        System.out.println(Department.getNameForCat("0"));
        System.out.println(Department.getNameForCat("v"));
    }
}
