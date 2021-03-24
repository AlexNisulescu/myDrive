package com.example.myminiodrive;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface UsersDao {

    @Insert
    void insert(Users user);

    @Query("select * from users")
    List<Users> getAll();

    @Query("delete from users")
    void deleteAll();

    @Delete
    void delete(Users user);

    @Update
    void update(Users user);

    @Query("select * from users where username=:uname and password=:pass")
    List<Users> getUsersByNameAndPassword(String uname, String pass);
}
