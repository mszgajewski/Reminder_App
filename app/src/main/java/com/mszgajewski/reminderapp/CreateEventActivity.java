package com.mszgajewski.reminderapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.speech.RecognizerIntent;
import android.widget.TimePicker;
import android.widget.Toast;

import com.mszgajewski.reminderapp.Database.DatabaseClass;
import com.mszgajewski.reminderapp.Database.EntityClass;
import com.mszgajewski.reminderapp.databinding.ActivityCreateEventBinding;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class CreateEventActivity extends AppCompatActivity implements View.OnClickListener {

    ActivityCreateEventBinding binding;
    String timeToNotify;
    DatabaseClass databaseClass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCreateEventBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.recordIV.setOnClickListener(this);
        binding.btnTime.setOnClickListener(this);
        binding.btnDate.setOnClickListener(this);
        binding.btnDone.setOnClickListener(this);
        databaseClass = DatabaseClass.getDatabase(getApplicationContext());
    }

    @Override
    public void onClick(View v) {
        if (v == binding.recordIV) {
            recordSpeech();
        } else if (v == binding.btnTime) {
            selectTime();
        } else if (v == binding.btnDate) {
            selectDate();
        } else {
            submit();
        }
    }

    private void selectDate() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                binding.btnDate.setText(day + "-" + (month + 1) + "-" + year);
            }
        }, year, month, day);
        datePickerDialog.show();
    }

    private void selectTime() {
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                timeToNotify = hourOfDay + ":" + minute;
                binding.btnTime.setText(FormatTime(hourOfDay,minute));
            }
        }, hour, minute, true);
        timePickerDialog.show();
    }

    public String FormatTime(int hourOfDay, int minute) {

        String time = "";
        String formattedMinute;

        if (minute / 10 == 0) {
            formattedMinute = "0" + minute;
        } else {
            formattedMinute = "" + minute;
        }
        if (hourOfDay == 0) {
            time = "12" + ":" + formattedMinute;
        } else if (hourOfDay < 12) {
            time = hourOfDay + ":" + formattedMinute;
        } else if (hourOfDay == 12) {
            time = "12" + ":" + formattedMinute ;
        } else {
            int temp = hourOfDay - 12;
            time = hourOfDay + ":" + formattedMinute;
        }
        return time;
    }

    private void recordSpeech() {

        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, "en-US");

        try {
            startActivityForResult(intent,1);
        } catch (Exception e) {
            Toast.makeText(this, "Nie można rozpoznać mowy", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == RESULT_OK && data !=null) {
                ArrayList<String> text = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                binding.textMessage.setText(text.get(0));
            }
        }
    }

    private void submit() {
        String text = binding.textMessage.getText().toString().trim();
        if (text.isEmpty()) {
            Toast.makeText(this, "Proszę wpisać lub powiedzieć treść do zapisania", Toast.LENGTH_SHORT).show();
        } else {
            if (binding.btnTime.getText().toString().equals("Wybierz godzinę") || binding.btnTime.getText().toString().equals("Wybierz datę")){
                Toast.makeText(this, "Proszę wybrać datę i godzinę", Toast.LENGTH_SHORT).show();
            } else {
                EntityClass entityClass = new EntityClass();
                String value = binding.textMessage.getText().toString().trim();
                String date = binding.btnDate.getText().toString().trim();
                String time = binding.btnTime.getText().toString().trim();

                entityClass.setEventDate(date);
                entityClass.setEventName(value);
                entityClass.setEventTime(time);
                databaseClass.EventDao().insertAll(entityClass);
                setAlarm(value, date, time);
            }
        }
    }

    private void setAlarm(String text, String date, String time) {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(getApplicationContext(), AlarmBroadcast.class);
        intent.putExtra("event",text);
        intent.putExtra("time", time);
        intent.putExtra("date", date);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(),0,intent,PendingIntent.FLAG_ONE_SHOT);
        String dateAndTime = date + " " + timeToNotify;
        DateFormat formatter = new SimpleDateFormat("d-M-yyyy hh:mm");
        try {
            Date date1 = formatter.parse(dateAndTime);
            alarmManager.set(AlarmManager.RTC_WAKEUP, date1.getTime(),pendingIntent);
            Toast.makeText(this, "Alarm ustawiony", Toast.LENGTH_SHORT).show();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        finish();
    }
}