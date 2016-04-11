package com.bignerdranch.android.twitchraffle;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import java.io.IOException;

import java.io.BufferedReader;
import java.net.URL;

/**
 * Created by Anthony on 4/10/2016.
 */
public class HTTPImpl {
    HttpClient mClient;

    public HTTPImpl(){
        try{
            HttpClient httpClient = HttpClientBuilder.create().build();
        }
    }
    public static String getData() throws Exception {
        BufferedReader in = null;
        String data = null;
        HttpGet request = new HttpGet();
        URL website = new URL("http://www.twitter.com");

        return data;
    }
}
