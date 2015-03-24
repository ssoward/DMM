package com.soward.reports;

import java.util.Collection;
import java.util.Map;

public interface ReportCollection {

    public Collection getData(Map<String, Object> parameters);

}
