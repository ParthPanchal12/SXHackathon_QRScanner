package com.example.parthdp.sxhackathon_qrscanner;

import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by parthdp on 11/24/17.
 */

public class AsyncTaskForAPI extends AsyncTask<String, String, String>
{

    public interface AsyncResponse {
        void processFinish(String output);
    }

    public AsyncTaskForAPI.AsyncResponse delegate = null;

    public AsyncTaskForAPI(AsyncTaskForAPI.AsyncResponse delegate){
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

        String address = params[0];
        String SERVICE_URL = address;
        HttpURLConnection conn = null;
        final StringBuilder json = new StringBuilder();
        try {
            // Connect to the web service
            URL url = new URL(SERVICE_URL);
            conn = (HttpURLConnection) url.openConnection();
//            OutputStreamWriter ow =new OutputStreamWriter(conn.getOutputStream());
//            JSONObject msg = new JSONObject();
//            ow.write(msg.toString());
//            ow.flush();
//            ow.close();
            InputStreamReader in = new InputStreamReader(conn.getInputStream());
            // Read the JSON data into the StringBuilder
            int read;
            char[] buff = new char[1024];
            while ((read = in.read(buff)) != -1) {
                json.append(buff, 0, read);
            }
            System.out.println(json.toString());
            in.close();

        } catch (IOException e) {
            System.out.println("error");
        }  finally {
            if (conn != null) {
                conn.disconnect();
            }
        }

        return json.toString();
    }

    @Override
    protected void onPostExecute(String result) {


        super.onPostExecute(result);
        delegate.processFinish(result);

    }

}
