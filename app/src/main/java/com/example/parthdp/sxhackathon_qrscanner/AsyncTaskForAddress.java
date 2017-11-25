package com.example.parthdp.sxhackathon_qrscanner;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by parthdp on 11/23/17.
 */




public class AsyncTaskForAddress extends AsyncTask<String, String, String>
{

    public interface AsyncResponse {
        void processFinishAddress(String output);
    }

    public AsyncResponse delegate = null;

    public AsyncTaskForAddress(AsyncResponse delegate){
        this.delegate = delegate;
    }


    @Override
    protected void onPreExecute() {
        super.onPreExecute();

    }
    @Override
    protected String doInBackground(String... params) {

        //this method will be running on background thread so don't update UI frome here
        //do your long running http tasks here,you dont want to pass argument and u can access the parent class' variable url over here

        //initiating the qr code scan
        String result = "";
        String address = params[0];
//        address= "2870, Opp. Soul Space Arena, Doddanekundi Main Road, Chinappa Layout, Mahadevapura, Ferns City, Doddanekkundi, Bengaluru, Karnataka 560037";
        String SERVICE_URL = "https://maps.google.com/maps/api/geocode/json?address="+address+"&sensor=false&key=AIzaSyDmMf6CaVPQ97urEnJqD0IrVdBj4Nf_53k";
        HttpURLConnection conn = null;
        final StringBuilder json = new StringBuilder();
        try {
            // Connect to the web service
            URL url = new URL(SERVICE_URL);
            conn = (HttpURLConnection) url.openConnection();
            InputStreamReader in = new InputStreamReader(conn.getInputStream());
            // Read the JSON data into the StringBuilder
            int read;
            char[] buff = new char[1024];
            while ((read = in.read(buff)) != -1) {
                json.append(buff, 0, read);
            }
            System.out.println(json.toString());
            JSONObject jq = new JSONObject(json.toString());
            JSONArray jsonArray=(JSONArray)jq.get("results");
            JSONObject jsonObject = jsonArray.getJSONObject(0);
            JSONObject j1 = (JSONObject) jsonObject.get("geometry");
            JSONObject j2 = (JSONObject) j1.get("location");
            Double lat = (Double) j2.get("lat");
            System.out.println(lat);
            Double long1 = (Double) j2.get("lng");
            System.out.println(long1);
            result = lat +","+long1+" ("+params[1]+")";

        } catch (IOException e) {
            System.out.println("error");
        } catch (JSONException e) {
            e.printStackTrace();
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }

        return result;
    }

    @Override
    protected void onPostExecute(String result) {

        super.onPostExecute(result);
        delegate.processFinishAddress(result);

    }

}
