package com.example.parking_app.interfaces;

import com.amazonaws.mobileconnectors.dynamodbv2.document.datatype.Document;

import java.util.List;

public interface Observer {
    void update(List<Document> docs);
}
