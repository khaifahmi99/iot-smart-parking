package com.example.parking_app;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.appcompat.widget.Toolbar;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.mobileconnectors.dynamodbv2.document.datatype.Document;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBAsyncClient;
import com.example.parking_app.dynamodb.DynamoDBConnector;
import com.example.parking_app.dynamodb.ListParkingSpotsTask;
import com.example.parking_app.dynamodb.UpdateParkingSpotTask;
import com.example.parking_app.dynamodb.User;
import com.example.parking_app.interfaces.Observer;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;


public class BookingActivity extends AppCompatActivity implements Observer, AdapterView.OnItemSelectedListener {

    private String tsBooking;
    private String parkingSpotId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);

        //Get the list of not booked parking spots
        AmazonDynamoDB ddb = DynamoDBConnector.ConnectToDynamoDb();
        ListParkingSpotsTask task = new ListParkingSpotsTask();
        task.registerObserver(this);
        task.execute(ddb);


        //Toolbar configuration
        Toolbar myToolbar = findViewById(R.id.app_toolbar);
        myToolbar.setTitle(R.string.app_name);
        myToolbar.setLogo(R.drawable.ic_launcher_foreground);
        myToolbar.setTitleTextColor(getResources().getColor(R.color.colorWhite));
        setSupportActionBar(myToolbar);

        //Set timestamp spinner
        setTimeStampSpinner();


    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    public void onResume() {
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

        //Generate a random user id
        int userId = DynamoDBConnector.GiveRandomNumber(100000);


        //Create a intent that starts PaymentActivity
        Intent paymentActivityIntent = new Intent(this, PaymentActivity.class);

        //Store the input values in the BookingParcel (see BookingParcel class)
        BookingParcel bookingParcel = new BookingParcel(parkingSpotId, String.valueOf(userId), name, carPlate, phone, tsBooking);

        //Store the BookingParcel object in the intent
        paymentActivityIntent.putExtra("BOOKINGPARCEL", bookingParcel);

        //Connect to dynamodb
        AmazonDynamoDB ddb = DynamoDBConnector.ConnectToDynamoDb();



        //Create a user and fill the user with the retrieved input
        User user = new User(String.valueOf(userId), carPlate, name, phone, false, "");

        //Update the parking_spot table based on the booking
        UpdateParkingSpotTask task = new UpdateParkingSpotTask(parkingSpotId, user, "Booking", tsBooking);
        task.execute(ddb);

        //Finally, switch to the PaymentActivity
        startActivity(paymentActivityIntent);
    }



    public void setTimeStampSpinner() {

        //This array is for the list of timestamps for booking
        ArrayList<String> bookingTimeStamps = new ArrayList<>();

        //Get the current date and time
        Calendar calendar = Calendar.getInstance();
        Date currentDate = new Date();
        calendar.setTime(currentDate);

        //Add one hour ahead to the current time
        calendar.add(Calendar.HOUR_OF_DAY, 1);

        //Convert to timestamp and save in to the array list
        Date OneHourAhead = calendar.getTime();
        Timestamp tsOneAhead = new Timestamp(OneHourAhead.getTime());
        bookingTimeStamps.add(tsOneAhead.toString());

        //Add two hours to the current time
        calendar.setTime(tsOneAhead);
        calendar.add(Calendar.HOUR_OF_DAY, 1);

        //Convert to timestamp and save in to the array list
        Date TwoHoursAhead = calendar.getTime();
        Timestamp tsTwoAhead = new Timestamp(TwoHoursAhead.getTime());
        bookingTimeStamps.add(tsTwoAhead.toString());

        //Finally, setup the spinner for the booking timestamps
        Spinner spinner = findViewById(R.id.spinner_timestamp);
        spinner.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, bookingTimeStamps));
        spinner.setOnItemSelectedListener(this);

    }


    @Override
    public void update(List<Document> docs) {

        //Convert the dynamodb data into parking spot ids
        ArrayList<String> parkingSpots = new ArrayList<>();

        for (Document d : docs)
            parkingSpots.add(d.get("parking_spot_id").asString());

        //Setup the spinner
        Spinner spinner = findViewById(R.id.spinner_parking_spots);
        spinner.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, parkingSpots));
        spinner.setOnItemSelectedListener(this);


    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        //on selection get the item selected for the each spinner
        switch (parent.getId()) {
            case R.id.spinner_parking_spots:
                parkingSpotId = (String) parent.getItemAtPosition(position);
                break;
            case R.id.spinner_timestamp:
                tsBooking = (String) parent.getItemAtPosition(position);
                break;
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
