package com.csci571.forecast.dummy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ImagesInfo {

  public static final List<String> ITEMS = new ArrayList<String>();


    /*private static final int COUNT = 8;

    static {
        String imageUri = "https://upload.wikimedia.org/wikipedia/commons/thumb/8/86/Bostonstraight.jpg/220px-Bostonstraight.jpg";
        // Add some sample items.
        for (int i = 1; i <= COUNT; i++) {
            addItem(imageUri);
        }
    }*/

  public static void addItem(String item) {
    ITEMS.add(item);
  }
}
