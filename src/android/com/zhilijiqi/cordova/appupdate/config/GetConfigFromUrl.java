package com.zhilijiqi.cordova.appupdate.config;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Feng on 2017/9/7.
 */

public class GetConfigFromUrl {

    public static String sendGetRequest(String url){
        try{
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder().url(url).build();
            Response response = client.newCall(request).execute();
            String responseData = response.body().string();
            return responseData;
        }catch(Exception e){
            e.printStackTrace();
        }
        return "";
    }
}
