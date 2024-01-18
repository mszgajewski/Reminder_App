package com.mszgajewski.reminderapp;

import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.mszgajewski.reminderapp.databinding.ActivityNotificationMessageBinding;

public class NotificationMessage extends AppCompatActivity {

    ActivityNotificationMessageBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityNotificationMessageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Bundle bundle = getIntent().getExtras();
        binding.tvMessage.setText(bundle.getString("message"));
    }
}
