package com.example.note.model.dataBase;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by gera on 25.09.2014.
 */
   public final class UserDataBase {

            public UserDataBase() {}

            public interface BaseColumns {
                String _ID = "_id";
            }

            public interface TableData extends BaseColumns {
                String TITLE = "title";
                String SHORT_CONTENT = "short_content";
            }


        }



