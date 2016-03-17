package com.example.rd7773.roposo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by rd7773 on 3/17/2016.
 */
public class StaticInstance {

    public static HashMap<String , UserProfile> usersMap = new HashMap<>();
    public static List<Story> storyList = new ArrayList<>();
    static boolean isCursorAdapter = true;


}
