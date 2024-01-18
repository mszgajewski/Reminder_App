package com.mszgajewski.reminderapp.Database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "myTable")
public class EntityClass {

    @PrimaryKey(autoGenerate = true)
    int id;

    @ColumnInfo(name = "eventname")
    String eventName;

    @ColumnInfo(name = "eventdate")
    String eventDate;

    @ColumnInfo(name = "eventtime")
    String eventTime;

    public String getEventDate() {
        return eventDate;
    }

    public void setEventDate(String eventDate) {
        this.eventDate = eventDate;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getEventTime() {
        return eventTime;
    }

    public void setEventTime(String eventTime) {
        this.eventTime = eventTime;
    }
}