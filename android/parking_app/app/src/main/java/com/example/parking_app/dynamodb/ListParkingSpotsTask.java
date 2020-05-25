package com.example.parking_app.dynamodb;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.amazonaws.mobileconnectors.dynamodbv2.document.Expression;
import com.amazonaws.mobileconnectors.dynamodbv2.document.QueryFilter;
import com.amazonaws.mobileconnectors.dynamodbv2.document.Search;
import com.amazonaws.mobileconnectors.dynamodbv2.document.Table;
import com.amazonaws.mobileconnectors.dynamodbv2.document.datatype.Document;
import com.amazonaws.mobileconnectors.dynamodbv2.document.datatype.DynamoDBBool;
import com.amazonaws.mobileconnectors.dynamodbv2.document.datatype.Primitive;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ComparisonOperator;
import com.example.parking_app.R;
import com.example.parking_app.adapter.ParkingSpotsAdapter;
import com.example.parking_app.interfaces.BaseSubject;
import com.example.parking_app.interfaces.Observer;

import java.util.ArrayList;
import java.util.List;

public class ListParkingSpotsTask extends AsyncTask<AmazonDynamoDB, Void, List<Document> > implements BaseSubject {


    private ArrayList<Observer> observers;

    private List<Document> parkingSpots;

    public ListParkingSpotsTask(){
        observers = new ArrayList<>();
    }

    @Override
    protected List<Document> doInBackground(AmazonDynamoDB... aDb) {
        try {
            Table dbTable = Table.loadTable(aDb[0], "parking_spot");


            Expression expression = new Expression();
            expression.setExpressionStatement("isBooked = :isBooked");
            expression.withExpressionAttibuteValues(":isBooked", new DynamoDBBool(false));
            Search searchResult = dbTable.scan(expression);

            return searchResult.getAllResults();
        }

        catch(Exception e){
            Log.e("Dynamo ERROR:", e.toString());
        }

        return null;
    }

    protected void onPostExecute(List<Document> aParkingSpots) {


        if (aParkingSpots != null){
            parkingSpots = aParkingSpots;
            notifyObservers();
        }

    }


    @Override
    public void registerObserver(Observer observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(Observer observer) {
        observers.remove(observers.indexOf(observer));
    }

    @Override
    public void notifyObservers() {
        for (Observer observer :  observers) {
            observer.update(parkingSpots);
        }
    }
}
