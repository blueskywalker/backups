package com.sysomos.grid.tools.backup.twitter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by kkim on 9/1/15.
 */
public class TweetDetector {
    static final List<String> compliance = new ArrayList<String>();
    static Gson gson = new Gson();
    static Type stringStringMap = new TypeToken<Map<String, String>>(){}.getType();

    static {
        compliance.add("delete");
        compliance.add("scrub_geo");
        compliance.add("event");
        compliance.add("disconnect");
        compliance.add("warning");
        compliance.add("user_withheld");
        compliance.add("status_withheld");
        compliance.add("limit");
    }


    public static boolean isCompliance(String msg) {
        Map<String,String> map = gson.fromJson(msg,stringStringMap);
        for(String tag : compliance) {
            if(map.containsKey(tag))
                return true;
        }
        return false;
    }
}
