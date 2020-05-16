package com.example.parking_app;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class PaymentActivity extends AppCompatActivity {

    private BookingParcel bookingParcel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        //Toolbar configuration
        Toolbar myToolbar = findViewById(R.id.app_toolbar);
        myToolbar.setTitle(R.string.app_name);
        myToolbar.setLogo(R.drawable.ic_launcher_foreground);
        myToolbar.setTitleTextColor(getResources().getColor(R.color.colorWhite));
        setSupportActionBar(myToolbar);

        //Getting intent from BookingActivity
        Intent paymentIntent = getIntent();

        //Get the parcel stored in the intent
        bookingParcel  = paymentIntent.getParcelableExtra("BOOKINGPARCEL");




    }
}
