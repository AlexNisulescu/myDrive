package com.example.myminiodrive;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Users.class}, version = 1, exportSchema = false)
public abstract class UsersDB extends RoomDatabase {

    private final static String DB_NAME="users.db";
    private static UsersDB instance;

    public static UsersDB getInstance(Context cnt){
        if (instance==null){
            instance= Room.databaseBuilder(cnt, UsersDB.class, DB_NAME)
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }


    public abstract UsersDao getUsersDao();
}
