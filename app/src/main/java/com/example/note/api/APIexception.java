package com.example.note.api;

/**
 * Created by gera on 08.09.2014.
 */
public class APIexception extends Exception{



    public enum TypeError {ERROR_CONNECTION, ERROR_SERVER , ERROR_JSON ,ERROR};

    final TypeError typeOfError;

    public TypeError getError(){
        return  typeOfError;
    }

    public APIexception(TypeError tOfEr, Throwable throwable) {
        super(throwable);
        typeOfError = tOfEr;
    }



}

