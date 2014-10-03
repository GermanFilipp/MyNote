package com.example.note;

import android.app.Application;
import android.database.sqlite.SQLiteDatabase;


import com.example.note.model.LocalData;
import com.example.note.model.dataBase.UserDataBaseHelper;

public class MyApplication extends Application {
	LocalData ld = new LocalData();
    UserDataBaseHelper dataBaseHelper;
    SQLiteDatabase sqLiteDatabase;
/*    public SQLiteDatabase getSqLiteDatabase(){
        return sqLiteDatabase;
    }*/
	@Override
	public void onCreate() {
		super.onCreate();

	}

	public LocalData getLocalData() {
		return ld;
	}

}
