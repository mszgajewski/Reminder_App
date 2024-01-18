package com.mszgajewski.reminderapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.mszgajewski.reminderapp.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.createEvent.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == binding.createEvent) {
            goToCreateEventActivity();
        }
    }

    private void goToCreateEventActivity(){
        Intent intent = new Intent(getApplicationContext(),CreateEventActivity.class);
        startActivity(intent);
    }
}