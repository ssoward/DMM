package com.soward.object;

import java.util.Map;

/**
 * Created by ssoward on 4/27/15.
 */

/***
 * @map map<productNum, Map>
 */
public class DailySalesCache {
    Long id;
    Map<String, Map> map;

    public DailySalesCache(Long id, Map<String, Map> map) {
        this.id = id;
        this.map = map;
    }

    public DailySalesCache(){}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Map<String, Map> getMap() {
        return map;
    }

    public void setMap(Map<String, Map> map) {
        this.map = map;
    }
}
