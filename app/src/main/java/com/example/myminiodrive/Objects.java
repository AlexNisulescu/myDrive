package com.example.myminiodrive;

import java.time.ZonedDateTime;

public class Objects {

    private String name;
    private long size;
    private ZonedDateTime date;

    public Objects(String name, long size, ZonedDateTime date) {
        this.name = name;
        this.size = size;
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSize() {
        return String.valueOf(size);
    }

    public void setSize(long size) {
        this.size = size;
    }

    public String getDate() {
        return String.valueOf(date);
    }

    public void setDate(ZonedDateTime date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "Objects{" +
                "size=" + size +
                '}';
    }
}
