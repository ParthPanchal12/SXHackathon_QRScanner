package com.example.parthdp.sxhackathon_qrscanner;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class HomeScreenActivity extends AppCompatActivity {

    Button customerApp,deliveryApp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_screen);
         customerApp = (Button) findViewById(R.id.customerAppButton);
         deliveryApp = (Button) findViewById(R.id.deliveryAppButton);

         customerApp.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 Intent myIntent = new Intent(HomeScreenActivity.this, CustomerScreen.class);
                 HomeScreenActivity.this.startActivity(myIntent);

             }
         });

         deliveryApp.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 Intent myIntent = new Intent(HomeScreenActivity.this, MainActivity.class);

                 HomeScreenActivity.this.startActivity(myIntent);

             }
         });


    }
}
