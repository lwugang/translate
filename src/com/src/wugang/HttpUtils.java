package com.src.wugang;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by lwg on 2016/6/1.
 */
public class HttpUtils {
    public static void  doAsyncGet(final String urlStr,final OnRequestListener listener){
        new Thread(){
            @Override
            public void run() {
               try {
                   URL url = new URL(urlStr);
                   HttpURLConnection conn = (HttpURLConnection)url.openConnection();
                   conn.setRequestMethod("GET");
                   conn.connect();
                   BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(),"utf-8"));
                   String line ;
                   String result ="";
                   while( (line =br.readLine()) != null ){
                       result += line;
                   }
                   System.out.println(result);
                   if(listener!=null) listener.onRequestCompleted(result);
                   br.close();
               }catch (Exception e){

               }
            }
        }.start();
    }

    public static interface OnRequestListener{
        void onRequestCompleted(String result);
    }
}
