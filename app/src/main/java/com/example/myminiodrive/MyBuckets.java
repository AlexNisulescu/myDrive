package com.example.myminiodrive;

import java.time.ZonedDateTime;
import java.util.List;

public class MyBuckets {

    private String name;
    private ZonedDateTime date;

    public MyBuckets(String name, ZonedDateTime date) {
        this.name = name;
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ZonedDateTime getDate() {
        return date;
    }

    public void setDate(ZonedDateTime date) {
        this.date = date;
    }
}
