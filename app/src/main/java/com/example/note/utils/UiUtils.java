package com.example.note.utils;

import android.content.Context;
import android.view.Gravity;
import android.widget.Toast;

import com.example.note.api.APIexception;

/**
 * Created by gera on 11.09.2014.
 */
public final class UiUtils {

    public static void showToastByApiException(Context context, APIexception excep){
        switch(excep.getError()) {
            case ERROR_JSON:
            Toast toast = Toast.makeText(context, "ошибка связанная с json", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.BOTTOM, 10, 50);
            toast.show();
            break;
            case ERROR_CONNECTION:
                Toast toast1 = Toast.makeText(context, "ошибка соединения", Toast.LENGTH_SHORT);
                toast1.setGravity(Gravity.BOTTOM, 10, 50);
                toast1.show();
                break;
            case ERROR_SERVER:
                Toast toast2 = Toast.makeText(context, "ошибка сервера", Toast.LENGTH_SHORT);
                toast2.setGravity(Gravity.BOTTOM, 10, 50);
                toast2.show();
                break;
            case ERROR:
                Toast toast3 = Toast.makeText(context, "неизвестная ошибка", Toast.LENGTH_SHORT);
                toast3.setGravity(Gravity.BOTTOM, 10, 50);
                toast3.show();
                break;
        }

    }


    private UiUtils(){

    }
}
