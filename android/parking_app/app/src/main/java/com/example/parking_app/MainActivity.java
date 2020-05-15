package com.example.parking_app;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;



public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar myToolbar = findViewById(R.id.app_toolbar);
        myToolbar.setTitle(R.string.app_name);
        myToolbar.setLogo(R.drawable.ic_launcher_foreground);
        myToolbar.setTitleTextColor(getResources().getColor(R.color.colorWhite));
        setSupportActionBar(myToolbar);

    }
}
