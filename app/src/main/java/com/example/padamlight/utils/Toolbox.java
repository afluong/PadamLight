package com.example.padamlight.utils;

import com.example.padamlight.Suggestion;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Just a toolbox to define little utils you may need
 */
public class Toolbox {

    public static List<String> formatHashmapToList(HashMap<String, Suggestion> hashmap) {
        final List<String> list = new ArrayList<>();
        for (String key : hashmap.keySet()) {
            list.add(key);
        }
        return list;
    }
}
