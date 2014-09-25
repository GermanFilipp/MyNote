package com.example.note;

import android.app.Application;


import com.example.note.model.LocalData;

public class MyApplication extends Application {
	LocalData ld = new LocalData();

	@Override
	public void onCreate() {
		super.onCreate();

	}

	public LocalData getLocalData() {
		return ld;
	}

}
