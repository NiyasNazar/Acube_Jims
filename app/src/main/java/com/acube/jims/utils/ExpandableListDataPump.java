package com.acube.jims.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ExpandableListDataPump {
    public static HashMap<String, List<String>> getData() {
        HashMap<String, List<String>> expandableListDetail = new HashMap<String, List<String>>();

        List<String> category = new ArrayList<String>();
        category.add("Gold");
        category.add("Diamond");


        List<String> football = new ArrayList<String>();
        football.add("Brazil");
        football.add("Spain");
        football.add("Germany");
        football.add("Netherlands");
        football.add("Italy");

        List<String> basketball = new ArrayList<String>();
        basketball.add("United States");
        basketball.add("Spain");
        basketball.add("Argentina");
        basketball.add("France");
        basketball.add("Russia");

        expandableListDetail.put("Category", category);
        expandableListDetail.put("Color", football);
        expandableListDetail.put("Karat", basketball);
        expandableListDetail.put("Shape", basketball);
        expandableListDetail.put("Certified by", basketball);
        return expandableListDetail;
    }
}
