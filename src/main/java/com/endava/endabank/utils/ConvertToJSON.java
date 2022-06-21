package com.endava.endabank.utils;

import com.google.gson.Gson;

import java.util.Map;

public class ConvertToJSON {

    public static String body(Map<String,String> information){
        Gson gson = new Gson();
        return gson.toJson(information);
    }



}
