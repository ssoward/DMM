package com.soward.object;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ssoward on 4/18/15.
 */
public class ProductSold {
    Map<Long, Long> yearsCount;
    Long id;

    public ProductSold(){
        yearsCount = new HashMap<Long, Long>();
    }

    public Map<Long, Long> getYearsCount() {
        return yearsCount;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
