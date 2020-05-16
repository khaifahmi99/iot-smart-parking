package com.example.parking_app;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.widget.Toolbar;

import java.sql.Time;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Date;


public class BookingActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);


        //Toolbar configuration
        Toolbar myToolbar = findViewById(R.id.app_toolbar);
        myToolbar.setTitle(R.string.app_name);
        myToolbar.setLogo(R.drawable.ic_launcher_foreground);
        myToolbar.setTitleTextColor(getResources().getColor(R.color.colorWhite));
        setSupportActionBar(myToolbar);

    }



    @RequiresApi(api = Build.VERSION_CODES.O)
    public void onResume(){
        super.onResume();


    }

    public void book(View view) {

        //Get the input fields values
        EditText editUsername = findViewById(R.id.edit_user_name);
        EditText editCarPlate = findViewById(R.id.edit_car_plate);
        EditText editPhone = findViewById(R.id.edit_user_phone);

        String name = editUsername.getText().toString();
        String carPlate = editCarPlate.getText().toString();
        String phone = editPhone.getText().toString();

        //Date for the timestamp
        Date date = new Date();
        Timestamp timestamp = new Timestamp(date.getTime());

        //Create a intent that starts PaymentActivity
        Intent paymentActivityIntent = new Intent(this, PaymentActivity.class);

        //Store the input values in the BookingParcel (see BookingParcel class)
        BookingParcel bookingParcel = new BookingParcel(name, carPlate, phone, timestamp.getTime());

        //Store the BookingParcel object in the intent
        paymentActivityIntent.putExtra("BOOKINGPARCEL", bookingParcel);

        //Finally, start the activity
        startActivity(paymentActivityIntent);
    }
}
