package com.example.parking_app.interfaces;

public interface BaseSubject {
    public void registerObserver(Observer observer);
    public void removeObserver(Observer observer);
    public void notifyObservers();
}
