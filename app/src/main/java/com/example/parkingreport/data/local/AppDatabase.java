package com.example.parkingreport.data.local;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.parkingreport.data.local.dao.UserDao;
import com.example.parkingreport.data.local.entities.User;

@Database(entities = {User.class}, version = 2, exportSchema = false)
public abstract class AppDatabase  extends RoomDatabase {
    private static volatile AppDatabase INSTANCE;

    public static AppDatabase getInstance(Context context){
        System.out.println(context);
        if(INSTANCE == null){
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    AppDatabase.class, "app_database")
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }

        }
        return INSTANCE;
    }

    public abstract UserDao getUserDao();

}