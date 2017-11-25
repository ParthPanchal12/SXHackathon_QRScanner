package com.example.parthdp.sxhackathon_qrscanner;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    //View Objects
    private Button buttonScan;
    private Button mapButton;
    private TextView details;

    //qr code scanner object
    private IntentIntegrator qrScan;

    String userName;
    String address;
    String uuid="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buttonScan = (Button) findViewById(R.id.qrScanButton);
        mapButton = (Button) findViewById(R.id.mapTaskButton);
        details = (TextView) findViewById(R.id.deliveryTextField);

        //intializing scan object
        qrScan = new IntentIntegrator(this);

        //attaching onclick listener
        buttonScan.setOnClickListener(this);
        mapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                String[] addresses = new String[2];
//                addresses[0]=address;
//                addresses[1]=userName;
//                AsyncTaskForAddress asyncTaskForAddress = (AsyncTaskForAddress) new AsyncTaskForAddress(new AsyncTaskForAddress.AsyncResponse() {
//                    @Override
//                    public void processFinishAddress(String output) {
//
////                        System.out.println(output);
//                        anotherMethod(output);
//
//                    }
//                }).execute(addresses);

                anotherMethod("12.9799375,77.6957357 ("+userName+")");

            }
        });
    }

    public void anotherMethod(String output){
        String strUri = "http://maps.google.com/maps?q=loc:" + output;
        Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(strUri));

        intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");

        startActivity(intent);
    }

    //Getting the scan results
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            //if qrcode has nothing in it
            if (result.getContents() == null) {
                Toast.makeText(this, "Result Not Found", Toast.LENGTH_LONG).show();
            } else {

                    uuid = result.getContents();
                    otherMethod();
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
    @Override
    public void onClick(View view) {
        qrScan.initiateScan();




    }

    public void otherMethod(){
        String[] addresses = new String[1];
        String urlCustomer = "http://10.68.58.48:5000/getorderdetails?uuid="+uuid;
        addresses[0] = urlCustomer;

        AsyncTaskForAPI asyncTaskForAPI = (AsyncTaskForAPI) new AsyncTaskForAPI(new AsyncTaskForAPI.AsyncResponse() {
            @Override
            public void processFinish(String output) {

                try{
                    //setting values to textviews
                    String detailText = "";
                    JSONObject obj = new JSONObject(output);
                    detailText+= "Customer Name: "+ obj.getString("customer_name") +"\n";
                    detailText+= "Order Id: "+ obj.getString("order_id") +"\n";
                    detailText+= "Product Name: "+ obj.getString("product_name") +"\n";
                    detailText+= "Phone Number: "+ obj.getString("phone_number") +"\n";
                    detailText+= "Address : "+ obj.getString("address") +"\n";
                    address = obj.getString("address");
                    userName =  obj.getString("customer_name");

                    details.setText(detailText);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }).execute(addresses);
        mapButton.setVisibility(View.VISIBLE);

    }

}
