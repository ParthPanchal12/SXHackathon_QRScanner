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

public class CustomerScreen extends AppCompatActivity implements View.OnClickListener{

    Button qr;
     TextView details;
    //qr code scanner object
    private IntentIntegrator qrScan;
    String uuid="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_screen);
        qr = (Button)findViewById(R.id.qrScanCustomer);
        qr.setOnClickListener(this);
        details = (TextView) findViewById(R.id.customerTextField);
        qrScan = new IntentIntegrator(this);


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
                    //converting the data to json
                    uuid = result.getContents();
                    System.out.println(uuid);
                    otherMethod();
                    return;
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
    @Override
    public void onClick(View view) {
        qrScan.initiateScan();

//        System.out.println(uuid);
//       this.otherMethod();


    }

    public void otherMethod(){

        System.out.println(uuid+"-----");

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

                    details.setText(detailText);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }).execute(addresses);
    }
}
