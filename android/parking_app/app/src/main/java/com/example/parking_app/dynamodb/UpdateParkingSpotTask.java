package com.example.parking_app.dynamodb;

import android.os.AsyncTask;
import android.util.Log;

import com.amazonaws.mobileconnectors.dynamodbv2.document.QueryFilter;
import com.amazonaws.mobileconnectors.dynamodbv2.document.Search;
import com.amazonaws.mobileconnectors.dynamodbv2.document.Table;
import com.amazonaws.mobileconnectors.dynamodbv2.document.UpdateItemOperationConfig;
import com.amazonaws.mobileconnectors.dynamodbv2.document.datatype.Document;
import com.amazonaws.mobileconnectors.dynamodbv2.document.datatype.Primitive;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ComparisonOperator;
import com.amazonaws.services.dynamodbv2.model.ReturnValue;
import com.amazonaws.services.dynamodbv2.model.UpdateItemRequest;
import com.amazonaws.services.dynamodbv2.model.UpdateItemResult;
import com.example.parking_app.BookingParcel;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
            userMap.put("ts_payment", new AttributeValue().withS(user.getTsPayment()));
            userMap.put("user_carplate_number", new AttributeValue().withS(user.getUserCarPlate()));
            userMap.put("user_id", new AttributeValue().withN(user.getUserId()));
            userMap.put("user_hasMadePayment", new AttributeValue().withBOOL(user.isUserHasMadePayment()));
            userMap.put("user_name", new AttributeValue().withS(user.getUserName()));
            userMap.put("user_phone", new AttributeValue().withS(user.getUserPhone()));


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
