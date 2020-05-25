package com.example.parking_app.dynamodb;

import android.util.Log;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBAsyncClient;

import java.util.Random;

public class DynamoDBConnector {

    public static AmazonDynamoDB ConnectToDynamoDb() {


        try {
            //Credentials, set accessKey and secretKey
            String accessKey = "AKIAYV55JZMFJNX3TFUK";
            String secretKey = "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";
            BasicAWSCredentials credentials = new BasicAWSCredentials(accessKey,  secretKey) ;
            //Connect to dynamodb
            AmazonDynamoDBAsyncClient ddb = new AmazonDynamoDBAsyncClient(credentials);
            //Set the region
            ddb.setRegion(Region.getRegion(Regions.AP_SOUTHEAST_2));
            //Return the ddb client
            return ddb;
        } catch (Exception e) {
            Log.e("Exception", e.toString());
            Log.e("Dynamo ERROR:", "App failed to connect to dynamo db");
        }

        return null;
    }


    public static int GiveRandomNumber(int aBound){
        Random rand = new Random();
         return rand.nextInt(aBound);
    }

}
