package com.soward.enums;

/**
 * Created by ssoward on 3/7/15.
 */
public enum ProductWeight {

    Wa("$00.00 - $02 = 2  oz"),
    Wb("$02.01 - $04 = 3  oz"),
    Wc("$04.01 - $06 = 5  oz"),
    Wd("$06.00 - $08 = 8  oz"),
    We("$08.01 - $10 = 9  oz"),
    Wf("$10.01 - $12 = 11 oz"),
    Wg("$12.01 - $14 = 13 oz"),
    Wh("$14.01 - $16 = 1  lb"),
    Wi("$16.01 - $18 = 2 lbs"),
    Wj("$18.01 - $20 = 2 lbs"),
    Wk("$20.01 - $22 = 2 lbs"),
    Wl("$22.01 - $24 = 3 lbs"),
    Wm("$24.01 - $26 = 4 lbs"),
    Wn("$26.01 - $28 = 4 lbs"),
    Wo("$28.01 - $30 = 5 lbs"),
    Wp("$30.01 - $32 = 5 lbs"),
    Wq("$32.01 - $50 = 6 lbs"),
    Wr("B&H $00.00 - $25 = is 13 oz"),
    Ws("B&H $25.01 - $33 = is 1  lb"),
    Wt("B&H $33.01 - $45 = is 2 lbs"),
    Wu("B&H $45.01 - $60 = is 3 lbs"),
    Wv("B&H $60.01       = is 4 lbs");

    private final String desc;

    ProductWeight(String des){
        this.desc = des;
    }

    public String getDesc() {
        return desc;
    }
}
