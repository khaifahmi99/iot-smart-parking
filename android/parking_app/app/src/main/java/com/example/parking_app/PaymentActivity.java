package com.example.parking_app;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.format.Time;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.example.parking_app.dynamodb.DynamoDBConnector;
import com.example.parking_app.dynamodb.UpdateParkingSpotTask;
import com.example.parking_app.dynamodb.User;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

public class PaymentActivity extends AppCompatActivity {

    private BookingParcel bookingParcel;
    private Timestamp tsBooking;
    private Timestamp tsPayment;
    private double fee;

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


        //Timestamp
        assert bookingParcel != null;
        tsBooking = Timestamp.valueOf(bookingParcel.getTimestamp());

        //Booking id
        int bookingId = DynamoDBConnector.GiveRandomNumber(100000);

        //Text views
        TextView textBooking = findViewById(R.id.text_booking);
        TextView textName = findViewById(R.id.text_name);
        TextView textPhone = findViewById(R.id.text_phone);
        TextView textPlate = findViewById(R.id.text_carplate);
        TextView textTime = findViewById(R.id.text_timestamp);


        //Set Text views
        String message = "Your booking id is: " + bookingId;
        textBooking.setText(message);
        textName.setText(bookingParcel.getName());
        textPhone.setText(bookingParcel.getPhone());
        textPlate.setText(bookingParcel.getCarPlate());
        textTime.setText(tsBooking.toString());




    }

    public void showPaymentWindow(View view) {
        //Show payment window
        RelativeLayout paymentWindow = findViewById(R.id.payment_window);
        paymentWindow.setVisibility(View.VISIBLE);
        RelativeLayout bookingWindow = findViewById(R.id.booking_window);
        bookingWindow.setVisibility(View.GONE);

        //Set the text fee and text fee dates
        TextView textFee = findViewById(R.id.text_fee);
        TextView textFeeDates = findViewById(R.id.text_fee_dates);


        //Modifry the booking timestamp, to simulate the duration of parking
        Calendar calendar = Calendar.getInstance();
        Date bookingDate = new Date(tsBooking.getTime());
        calendar.setTime(bookingDate);

        //Generate random minutes
        int minutes = DynamoDBConnector.GiveRandomNumber(100);
        calendar.add(Calendar.MINUTE, minutes);
        Date paymentDate = calendar.getTime();

        //Calculate payment
        tsPayment = new Timestamp(paymentDate.getTime());

        fee = 0.0;

        if (tsPayment.after(tsBooking)){
            fee = calculateParkingFee(tsPayment, tsBooking);
        } else {
            fee = calculateParkingFee(tsBooking, tsPayment);
        }

        //Set the text fields with data
        String feeMessage = "Parking fee: " + fee;
        String feeDateMessage = "Booking time: " + tsBooking.toString() + "\nExit time: " + tsPayment.toString();
        textFee.setText(feeMessage);
        textFeeDates.setText(feeDateMessage);





    }

    //Close payment window
    public void closePaymentWindow(View view) {
        RelativeLayout paymentWindow = findViewById(R.id.payment_window);
        paymentWindow.setVisibility(View.GONE);
        RelativeLayout bookingWindow = findViewById(R.id.booking_window);
        bookingWindow.setVisibility(View.VISIBLE);

    }

    //Calculate parking fee
    public double calculateParkingFee(Timestamp aTsAfter, Timestamp aTsBefore){

        long millisecond = aTsAfter.getTime() - aTsBefore.getTime() ;
        int seconds = (int) millisecond / 1000;
        int minutes = seconds / 60;

        return  1.50 * (double) minutes;

    }

    public void processPayment(View view)  {

        //Connect to dynamodb
        AmazonDynamoDB ddb = DynamoDBConnector.ConnectToDynamoDb();

        User user = new User(bookingParcel.getUserId(), bookingParcel.getCarPlate(), bookingParcel.getName(), bookingParcel.getPhone()
                ,true, tsPayment.toString());

        //Update the parking spot table based on the payment
        UpdateParkingSpotTask task = new UpdateParkingSpotTask(bookingParcel.getParkingSpotId(), user, "Payment");
        task.execute(ddb);

        //Create a new intent to BookingActivity as payment has been done
        Intent bookingActivityIntent = new Intent(this, BookingActivity.class);

        startActivity(bookingActivityIntent);
    }
}
