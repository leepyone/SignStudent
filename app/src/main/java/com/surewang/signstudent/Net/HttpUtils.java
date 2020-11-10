package com.surewang.signstudent.Net;

import android.os.Handler;
import android.util.Log;

import java.util.HashMap;
import java.util.Map;


import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class HttpUtils {

    public static int MSG_SUCCESS =1;
    public static int MSG_FALL =0;
    public static void get(String url, HashMap<String,String> params){

    }

    public static void post(final String url, final HashMap<String,String> params, final Handler handler){

        new Thread(){
            @Override
            public void run() {
                String message= null;
                try{
                    final OkHttpClient client = new OkHttpClient();
                    RequestBody formBody = appendBody(params);
                    Request request = new Request.Builder().post(formBody).url(url).build();
                    Response response = client.newCall((request)).execute();
                    if(response.isSuccessful()) {
                        message = response.body().string();
                        Log.d("Login：",message);
                    }else
                        Log.d("Login：","请求失败");
                }catch (Exception e){
                    e.printStackTrace();
                }

                if(message.equals("true"))
                    handler.sendEmptyMessage(MSG_SUCCESS);
                else
                    handler.sendEmptyMessage(MSG_FALL);
            }
        }.start();
    }

    private static RequestBody appendBody(Map<String, String> params) {
        FormBody.Builder body = new FormBody.Builder();
        if (params == null || params.isEmpty()) {
            return body.build();
        }
        for (Map.Entry<String, String> entry : params.entrySet()) {
            body.add(entry.getKey(), entry.getValue());
        }
        return body.build();
    }


}
