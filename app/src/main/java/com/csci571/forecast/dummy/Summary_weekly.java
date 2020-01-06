package com.csci571.forecast.dummy;

import java.util.ArrayList;
import java.util.List;

public class Summary_weekly {

    public static final List<Weekly> ITEMS = new ArrayList<Weekly>();

    public static void addItem(Weekly item) {
        ITEMS.add(item);
    }
}

