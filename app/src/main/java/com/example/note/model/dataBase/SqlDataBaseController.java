package com.example.note.model.dataBase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.note.model.dataBase.UserDataBase.TableData;
import java.sql.SQLException;

/**
 * Created by gera on 28.09.2014.
 */
public class SqlDataBaseController {
    public SQLiteDatabase dataBase;
    public UserDataBaseHelper dataBaseHelper;
    public Context context;


    public interface IdataBaseTable {
        String DATA_BASE_TABLE = "dataBaseTable";
    }

    public SqlDataBaseController(Context _context){
        context = _context;
    }

    public SqlDataBaseController open()throws SQLException{
        dataBaseHelper = new UserDataBaseHelper(context);
        dataBase = dataBaseHelper.getWritableDatabase();
        return this;
    }

    public void closeDataBase(){
        dataBaseHelper.close();
    }

    public void addData(String titel,String shortContent,long id){
        ContentValues values = new ContentValues();
        values.put(TableData.TITLE,titel);
        values.put(TableData.SHORT_CONTENT,shortContent);
        values.put(TableData._ID,id);
        dataBase.replace(IdataBaseTable.DATA_BASE_TABLE,null,values);
    }
    public Cursor getData(){

        String [] allTable = new String[]{TableData._ID,TableData.TITLE,TableData.SHORT_CONTENT};


        Cursor cursor = dataBase.query(IdataBaseTable.DATA_BASE_TABLE,allTable, null, null, null, null, null);
        if(cursor != null){
            cursor.moveToFirst();
        }
        return cursor;
    }
    public int updateData(long noteID,String title,String shortContent){
        ContentValues contentValuesUpdate = new ContentValues();
        contentValuesUpdate.put(TableData.TITLE,title);
        contentValuesUpdate.put(TableData.SHORT_CONTENT,shortContent);
        int updt = dataBase.update(IdataBaseTable.DATA_BASE_TABLE,contentValuesUpdate,TableData._ID+"="+noteID,null);
        return updt;
    }

    public void deleteData(long noteID){
        dataBase.delete(IdataBaseTable.DATA_BASE_TABLE,TableData._ID+"="+noteID,null);
    }
}
