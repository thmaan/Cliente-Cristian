package com.example.clientealgoritmodecristian;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String server = "Localhost";
        int port = 9092;

        Thread t = new Cliente(server, port);
        t.start();
        t.interrupt();
    }
}