package com.example.note.api;

import android.net.Uri;
import android.util.Log;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class API {
    private static String convertInputStreamToString(InputStream inputStream) throws APIexception {
        BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        try {
            while((line = bufferedReader.readLine()) != null)
                result += line;
            inputStream.close();
        } catch (IOException e) {
            throw new APIexception(APIexception.TypeError.ERROR ,e);
        }
        return result;
    }
    public static String GET(String url) throws APIexception{
        InputStream inputStream = null;
        String result = "";
        try {
            Log.d("GET","request: " +url);            // create HttpClient
            HttpClient httpclient = new DefaultHttpClient();
            // make GET request to the given URL
            HttpResponse httpResponse = httpclient.execute(new HttpGet(url));
            // receive response as inputStream
            inputStream = httpResponse.getEntity().getContent();
            // convert inputstream to string
            if(inputStream != null)
                result = convertInputStreamToString(inputStream);
            else
                result = "Did not work!";

        } catch (Exception e) {
            throw new APIexception(APIexception.TypeError.ERROR_CONNECTION ,e);
    }
     finally {
            Log.d("GET","response: "+result);
        }
        return result;
    }
    private  static Uri.Builder createUrlBuilder () {
        return new Uri.Builder()
                .scheme("http")
                .encodedAuthority("notes-androidcoursesdp.rhcloud.com")
                .appendPath("REST");
    }

    public static class LoginResponse {
        public    String sessionID ="";
        public    int result;


        public LoginResponse(JSONObject obj) throws APIexception {
            try {
                sessionID = obj.getString("sessionID");
                result = obj.getInt("result");

            } catch (JSONException e) {
                throw new APIexception(APIexception.TypeError.ERROR_JSON ,e);
            }

        }
        public int getUserCreate(){
            return result;
        }

        public  String getSessionID(){
            return sessionID;
        }



    }
    public LoginResponse login (String l, String p) throws APIexception{
        String rawResponse = GET(createUrlBuilder().appendPath("login")
        .appendQueryParameter("login",l)
        .appendQueryParameter("pass",p)
        .toString());
        LoginResponse response = null;

        try {

            response = new LoginResponse(new JSONObject (rawResponse));
        } catch (JSONException e) {

            throw new APIexception(APIexception.TypeError.ERROR_JSON ,e);
        }

        return response;
    }

    public static class RegistesResponse {
        public int result;

        public RegistesResponse(JSONObject obj) throws APIexception {

            try {
                result = obj.getInt("result");
            } catch (JSONException e) {
                throw new APIexception(APIexception.TypeError.ERROR_JSON ,e);
            }

        }
    }
    public RegistesResponse register (String l, String p) throws APIexception{
        String rawResponse = GET(createUrlBuilder().appendPath("register")
                .appendQueryParameter("login",l)
                .appendQueryParameter("pass",p)
                .toString());
        RegistesResponse response = null;
        try {
            response = new RegistesResponse(new JSONObject (rawResponse));
        } catch (JSONException e) {
            throw new APIexception(APIexception.TypeError.ERROR_JSON,e);
        }
        return response;
    }

    public static class CreateNoteResponse{
        int result;
        long noteID;
        public CreateNoteResponse(JSONObject obj ) throws JSONException{
                noteID = obj.getLong("noteID");
                result = obj.getInt("result");

        }
        public int getCreateNote (){
            return result;
        }
        public long getNoteID(){
            return noteID;
        }

    }
    public CreateNoteResponse putNote(String id,String note,String note_title_note) throws APIexception {
        String rawResponse = GET(createUrlBuilder().appendPath("createNote")
                .appendQueryParameter("sessionID", id)
                .appendQueryParameter("title", note_title_note)
                .appendQueryParameter("content", note)
                .toString());
           CreateNoteResponse response = null;
        try {
            response = new CreateNoteResponse(new JSONObject(rawResponse));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return response;
    }

    public static class GetNoteResponse{
        public int result;
        String title;
        String content;
        public GetNoteResponse(JSONObject obj) throws APIexception {

            try {
                result = obj.getInt("result");
                title = obj.getString("title");
                content = obj.getString("content");
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
        public int getResult(){return  result;}
        public String getTitle(){
            return title;
        }
        public String getContent() {return content;}
    }
    public GetNoteResponse getNote (String  _sessionID, long _noteID) throws APIexception{
        String rawResponse = GET(createUrlBuilder().appendPath("getNote")
                .appendQueryParameter("sessionID", _sessionID)
                .appendQueryParameter("noteID", Long.toString(_noteID))
                .toString());
        GetNoteResponse response = null;
        try {
            response = new GetNoteResponse( new JSONObject(rawResponse));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return response;
    }



    public static class NoteResponse {
      public  String title;
        public String shortContent;
        public long noteID;

        public NoteResponse(JSONObject obj) throws JSONException{
          title = obj.getString("title");
          shortContent = obj.getString("shortContent");
          noteID = obj.getLong("noteID");
       }
    }
    public static class  GetNotesListResponse{
      public  int result;
        ArrayList<NoteResponse> notes ;

        public GetNotesListResponse(JSONObject obj ) throws JSONException {
            result = obj.getInt("result");
            notes = new ArrayList<NoteResponse>();
            JSONArray arr = obj.getJSONArray("notes");
            for(int i=0; i<arr.length(); ++i){
                notes.add(new NoteResponse(arr.getJSONObject(i)));
            }

        }
        public ArrayList<NoteResponse> getNotesArray() {
            return notes;
        }

        }
    public static GetNotesListResponse getNotesList(String _sessionID) throws APIexception{
        String rawResponse = GET(createUrlBuilder().appendPath("getNotesList")
                .appendQueryParameter("sessionID",_sessionID)
                .toString());
        GetNotesListResponse response = null;

        try {
            response = new GetNotesListResponse(new JSONObject(rawResponse));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return response;
    }


    public static class LogoutResponse{
        public int result;
        public LogoutResponse(JSONObject obj) throws APIexception{
            try {
                result = obj.getInt("result");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
    public  LogoutResponse getLogout(String _sessionID)  throws APIexception {
       String rawResponse = GET(createUrlBuilder().appendPath("logout")
               .appendQueryParameter("sessionID", _sessionID)
               .toString());
        LogoutResponse response = null;
        try {
            response = new LogoutResponse(new JSONObject(rawResponse));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return response;
    }

    public static class ChangePasswordResponse{
       public int result;
        public ChangePasswordResponse(JSONObject obj) throws APIexception{
            try {
                result = obj.getInt("result");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
    public ChangePasswordResponse getChangePassword(String _sessionID,String _newPass,String _oldPass )throws APIexception{
        String rawResponse = GET(createUrlBuilder().appendPath("changePassword")
                .appendQueryParameter("sessionID",_sessionID)
                .appendQueryParameter("newPass", _newPass)
                .appendQueryParameter("oldPass", _oldPass)

                .toString());
        ChangePasswordResponse response = null;
        try {
            response = new ChangePasswordResponse(new JSONObject(rawResponse));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d("getChangePassword","oldPass: " +_oldPass);
        Log.d("getChangePassword","newPass: " +_newPass);
        return response;
    }

    public static class EditNoteResponse {
        public int result;
        public EditNoteResponse (JSONObject obj) throws  JSONException{
            result = obj.getInt("result");

        }
    }
    public EditNoteResponse getEditNote(String _sessionID, long _noteID, String _text ) throws APIexception {
        String rawResponse = GET(createUrlBuilder().appendPath("editNote")
                .appendQueryParameter("sessionID",_sessionID)
                .appendQueryParameter("noteID", String.valueOf(_noteID))
                .appendQueryParameter("text", _text)
                .toString());
        EditNoteResponse response = null;
        try {
            response = new EditNoteResponse(new JSONObject(rawResponse));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return response;
    }

    public static class DeleteNoteResponse {
       public int result;
        public DeleteNoteResponse (JSONObject obj) throws JSONException{
            result = obj.getInt("result");
        }
    }
    public DeleteNoteResponse deleteNote(String _sessionID, long _noteID) throws APIexception {
        String rawResponse = GET(createUrlBuilder().appendPath("deleteNote")
                .appendQueryParameter("sessionID", _sessionID)
                .appendQueryParameter("noteID", String.valueOf(_noteID))
                .toString());
        DeleteNoteResponse response=null;
        try {
            response = new DeleteNoteResponse(new JSONObject(rawResponse));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return response;
    }













}
