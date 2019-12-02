package com.smartenergy;

import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Conexao {
    static HttpURLConnection urlConnection = null;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static String getDados(String urlUsuario){

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(urlUsuario)
                .build();

        try{
            Response response = client.newCall(request).execute();

            return response.body().string();

        } catch (IOException error){
            return null;
        }

    }
}
