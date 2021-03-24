package com.example.myminiodrive;

import java.util.List;

public class Bucket {

    private String name;
    private int objectsCount;
    private List<Objects> objectsList;

    public Bucket(String name, int objectsCount, List<Objects> objectsList) {
        this.name = name;
        this.objectsCount = objectsCount;
        this.objectsList = objectsList;
    }

    public Bucket(String name, int objectsCount) {
        this.name = name;
        this.objectsCount = objectsCount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getObjectsCount() {
        return objectsCount;
    }

    public void setObjectsCount(int objectsCount) {
        this.objectsCount = objectsCount;
    }

    public List<Objects> getObjectsList() {
        return objectsList;
    }

    public void setObjectsList(List<Objects> objectsList) {
        this.objectsList = objectsList;
    }

}
