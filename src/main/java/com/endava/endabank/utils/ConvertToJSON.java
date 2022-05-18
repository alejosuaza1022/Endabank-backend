package com.endava.endabank.utils;

import com.google.gson.Gson;

import java.util.Map;

public class ConvertToJSON {

    public static String bodyJSON(Map<String,String> credentials){
        Gson gson=new Gson();
        return gson.toJson(credentials);
    }
}
