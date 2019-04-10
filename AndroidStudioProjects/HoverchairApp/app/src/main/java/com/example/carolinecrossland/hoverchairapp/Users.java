package com.example.carolinecrossland.hoverchairapp;

import java.util.HashMap;
import java.util.Map;

public class Users {
    public static Map<String, String> users = new HashMap<String, String>() {{
            put("team", "pwd");
            put("dsposeep", "pwd");
            put("bwalker", "pwd");
            put("jfisher", "pwd");
            put("jmartinez", "pwd");
            put("chaggard", "pwd");
            put("ccrossland", "pwd");
            put("team", "pwd");
        }};

    public static boolean login(String username, String password) {
        if(users.containsKey(username) && users.get(username).equals(password)) {
            return true;
        } else {
            return false;
        }
    }

}
