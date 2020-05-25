package com.example.parking_app.dynamodb;

import android.os.AsyncTask;
import android.util.Log;

import com.amazonaws.mobileconnectors.dynamodbv2.document.Expression;
import com.amazonaws.mobileconnectors.dynamodbv2.document.Search;
import com.amazonaws.mobileconnectors.dynamodbv2.document.Table;
import com.amazonaws.mobileconnectors.dynamodbv2.document.datatype.Document;
import com.amazonaws.mobileconnectors.dynamodbv2.document.datatype.DynamoDBBool;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.google.gson.Gson;

import java.util.List;

public class SaveUserTask extends AsyncTask<AmazonDynamoDB, Void, Boolean> {


    private User user;


    public SaveUserTask(User aUser){
       user = aUser;
    }


    @Override
    protected Boolean doInBackground(AmazonDynamoDB... aDb) {

        try {
            Table dbTable = Table.loadTable(aDb[0], "users");

            Gson gson = new Gson();

            Document docUser = Document.fromJson(gson.toJson(user));

            dbTable.putItem(docUser);

            return true;

        }

        catch(Exception e){
            Log.e("Dynamo ERROR:", e.toString());
        }

        return false;
    }
}
