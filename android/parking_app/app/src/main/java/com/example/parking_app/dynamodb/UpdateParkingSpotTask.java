package com.example.parking_app.dynamodb;

import android.os.AsyncTask;
import android.util.Log;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ReturnValue;
import com.amazonaws.services.dynamodbv2.model.UpdateItemRequest;
import com.amazonaws.services.dynamodbv2.model.UpdateItemResult;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

public class UpdateParkingSpotTask extends AsyncTask<AmazonDynamoDB, Void, Boolean> {


    private User user;
    private String parkingSpotId;
    private String flag;
    private String tsBooking;




    public UpdateParkingSpotTask(String aParkingSpotId, User aUser, String aFlag){
       user = aUser;
       parkingSpotId = aParkingSpotId;
       flag = aFlag;
    }

    public UpdateParkingSpotTask(String aParkingSpotId, User aUser, String aFlag, String aTsBooking){
        user = aUser;
        parkingSpotId = aParkingSpotId;
        flag = aFlag;
        tsBooking = aTsBooking;
    }

    @Override
    protected Boolean doInBackground(AmazonDynamoDB... aDb) {
        try {

            //Key
            HashMap<String, AttributeValue> key = new HashMap<>();
            key.put("parking_spot_id", new AttributeValue().withS(parkingSpotId));

            //User map
            HashMap<String, AttributeValue> userMap = new HashMap<String, AttributeValue>();
            userMap.put("ts_payment", new AttributeValue().withS(user.getTs_payment()));
            userMap.put("user_carplate_number", new AttributeValue().withS(user.getUser_carplate_number()));
            userMap.put("user_id", new AttributeValue().withN(String.valueOf(user.getUser_id())));
            userMap.put("user_hasMadePayment", new AttributeValue().withBOOL(user.isUser_hasMadePayment()));
            userMap.put("user_name", new AttributeValue().withS(user.getUser_name()));
            userMap.put("user_phone", new AttributeValue().withS(user.getUser_phone()));


            //Expression
            String expression = "";
            Map<String, AttributeValue> expressionAttributeValues = new HashMap<>();

            if (flag.equals("Booking")){
                expressionAttributeValues.put(":isBooked", new AttributeValue().withBOOL(true));
                expressionAttributeValues.put(":ts_booking", new AttributeValue().withS(tsBooking));
                expression = "set isBooked = :isBooked, ts_booking = :ts_booking, #user = :user";
            } else {

                expression = "set #user = :user";
            }


            expressionAttributeValues.put(":user", new AttributeValue().withM(userMap));
            Map<String, String> expressionAttributeNames =  new HashMap<>();
            expressionAttributeNames.put("#user", "user");


            UpdateItemRequest updateItemRequest = new UpdateItemRequest()
                    .withTableName("parking_spot").withKey(key)
                    .withExpressionAttributeNames(expressionAttributeNames)
                    .withUpdateExpression(expression)
                    .withExpressionAttributeValues(expressionAttributeValues)
                    .withReturnValues(ReturnValue.ALL_NEW);

            UpdateItemResult result = aDb[0].updateItem(updateItemRequest);
            Gson gson = new Gson();
            String x = gson.toJson(result);
            //Check it out
            Log.e("RESULT :", x);

            return true;
        }

        catch(Exception e){
            Log.e("Dynamo ERROR:", e.toString());
        }

        return false;
    }

    protected void onPostExecute(Boolean result) {
        if (result){
            Log.i("TABLE",   "Success");
        }

    }

}
