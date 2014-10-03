package com.example.note.model.dataBase;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class UserDataBaseHelper extends SQLiteOpenHelper implements UserDataBase.BaseColumns {

    public static final String DATABASE_NAME = "mydatabase.db";
    private static final int VERSION_INITIAL = 1;
    private static final int DATABASE_VERSION = VERSION_INITIAL;



    public interface Tables {
        String TABLE_DATA = "notes";

    }

    private static final String CREATE_TABLE_DATA = "CREATE TABLE "
            + Tables.TABLE_DATA + " ("
            + UserDataBase.TableData._ID + " INTEGER PRIMARY KEY, "
            + UserDataBase.TableData.TITLE + " TEXT, "
            + UserDataBase.TableData.SHORT_CONTENT + " TEXT)";


    private static final String DROP_TABLE = "DROP TABLE IF EXISTS ";

    public UserDataBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
       db.execSQL(CREATE_TABLE_DATA);
       // onCreate(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DROP_TABLE + Tables.TABLE_DATA);
        onCreate(db);
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        onUpgrade(db,0,DATABASE_VERSION);
    }
}
