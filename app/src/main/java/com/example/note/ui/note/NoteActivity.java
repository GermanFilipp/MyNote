package com.example.note.ui.note;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;

import android.widget.ListView;
import android.widget.Toast;

import com.example.note.MyApplication;
import com.example.note.R;
import com.example.note.api.API;
import com.example.note.api.API.DeleteNoteResponse;
import com.example.note.api.APIexception;
import com.example.note.model.dataBase.DataBaseContentProvider;
import com.example.note.model.dataBase.UserDataBase;

import com.example.note.ui.login.MainActivity;

public class NoteActivity extends Activity {
    private static final String LONG_EXTRA = "ID";
    private static final String INT_EXTRA = "POSITION";

    public API API = new API();


    protected NoteAdapter noteAdapter;
    protected ListView lv;
    protected AlertDialog.Builder alertDialog;
    Context context;
    long lastDeleteId;
    private Cursor c;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.note_activity);
        new MyNotesListAsyncTask().execute(new NotesList(((MyApplication) getApplication()).getLocalData().getSessionID()));

        String buttonOK = "Ок";
        String buttonCancel = "Отмена";
        String title = "Удаление";
        String message = "Удалить запись?";

        context = NoteActivity.this;
        alertDialog = new AlertDialog.Builder(context);
        alertDialog.setTitle(title);  // заголовок
        alertDialog.setMessage(message); // сообщение
        alertDialog.setPositiveButton(buttonOK, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int arg1) {
                new DeleteAsyncTask().execute(new DeleteRequest(((MyApplication) getApplication()).getLocalData().getSessionID(), lastDeleteId));
               /* Toast.makeText(context, "Вы сделали правильный выбор",
                        Toast.LENGTH_LONG).show();*/
            }
        });
        alertDialog.setNegativeButton(buttonCancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int arg1) {
                /*Toast.makeText(context, "Возможно вы правы", Toast.LENGTH_LONG)
                        .show();*/
            }
        });
        alertDialog.setCancelable(true);
        alertDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            public void onCancel(DialogInterface dialog) {
                Toast.makeText(context, "Вы ничего не выбрали",
                        Toast.LENGTH_LONG).show();
            }
        });


        String[] myContent = {UserDataBase.TableData._ID,
                UserDataBase.TableData.TITLE,
                UserDataBase.TableData.SHORT_CONTENT};
        c = getContentResolver().query(DataBaseContentProvider.URI_NOTE, myContent, null, null, "_ID");
        noteAdapter = new NoteAdapter(this, c);

        lv = (ListView) findViewById(R.id.list);

        lv.setAdapter(noteAdapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View itemClicked, int position, long id) {

                Intent intent = new Intent(NoteActivity.this, EditNoteActivity.class);
                intent.putExtra(INT_EXTRA, position);
                intent.putExtra(LONG_EXTRA, id);
                startActivity(intent);


            }


        });

        noteAdapter.setOnDeleteClickListener(new NoteAdapter.OnDeleteItemListner() {
            @Override
            public void onItemDeleteClick(long id) {
                lastDeleteId = id;
                alertDialog.show();

            }
        });
    }

    @Override
    protected void onResume() {

        super.onResume();

        noteAdapter.notifyDataSetChanged();
        noteAdapter.swapCursor(c);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_changePassword:
                Intent intentChangePassword = new Intent(this, ChangePasswordActivity.class);
                startActivity(intentChangePassword);
                return true;
            case R.id.action_logOut:
                API = new API();
                new MyAsyncTask().execute(new LogOut(((MyApplication) getApplication()).getLocalData().getSessionID()));
                return true;
            case R.id.action_add:
                Intent intentAdd = new Intent(this, NewNoteActivity.class);
                startActivity(intentAdd);

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    public class NotesList {
        private String sessionID;

        NotesList(String _sessionID) {
            sessionID = _sessionID;
        }


    }

    public class MyNotesListAsyncTask extends AsyncTask<NotesList, Void, API.GetNotesListResponse> {

        APIexception apiexception;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();


        }

        @Override
        protected API.GetNotesListResponse doInBackground(NotesList... params) {


            try {

                return API.getNotesList(params[0].sessionID);
            } catch (APIexception apIexception) {
                apIexception.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(API.GetNotesListResponse result) {
            super.onPostExecute(result);


            if (result != null) {
                switch (result.result) {
                    case 0:

                        if (result.getNotesArray() != null) {

                            ContentValues[] contentValues = new ContentValues[result.getNotesArray().size()];
                            for (int i = 0; i < contentValues.length; i++) {
                                contentValues[i] = new ContentValues();
                                contentValues[i].put(UserDataBase.TableData._ID, result.getNotesArray().get(i).noteID);
                                contentValues[i].put(UserDataBase.TableData.TITLE, result.getNotesArray().get(i).title);
                                contentValues[i].put(UserDataBase.TableData.SHORT_CONTENT, result.getNotesArray().get(i).shortContent);
                            }

                            getContentResolver().bulkInsert(DataBaseContentProvider.URI_NOTE, contentValues);
                        }

                        break;

                    case 1:

                        if (result.getNotesArray() == null) {
                            Toast toast1 = Toast.makeText(NoteActivity.this, "You can create new note :)", Toast.LENGTH_LONG);
                            toast1.setGravity(Gravity.BOTTOM, 10, 50);
                            toast1.show();
                        }
                        break;

                }
            } else {
                Toast toast1 = Toast.makeText(NoteActivity.this, "Exception", Toast.LENGTH_LONG);
                toast1.setGravity(Gravity.BOTTOM, 10, 50);
                toast1.show();
            }
        }
    }

    public class DeleteRequest {
        private String sessionID;
        private long noteId;

        public DeleteRequest(String _sessionID, long id) {
            sessionID = _sessionID;
            noteId = id;
        }

        public String getSessionID() {
            return sessionID;
        }

        public long getNoteID() {
            return noteId;
        }
    }

    public class DeleteAsyncTask extends AsyncTask<DeleteRequest, Void, DeleteNoteResponse> {
        DeleteRequest request;
        APIexception apiexception;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected DeleteNoteResponse doInBackground(DeleteRequest... params) {

            try {
                request = params[0];
                return API.deleteNote(params[0].getSessionID(), params[0].getNoteID());
            } catch (APIexception e) {
                apiexception = e;

                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(DeleteNoteResponse result) {
            super.onPostExecute(result);


            if (result != null) {
                switch (result.result) {
                    case 0:
                        getContentResolver().delete(DataBaseContentProvider.URI_NOTE, UserDataBase.TableData._ID + " = " + request.getNoteID(), null);
                        noteAdapter.notifyDataSetChanged();
                        noteAdapter.swapCursor(c);
                        break;

                    case 2:
                        Toast toast1 = Toast.makeText(NoteActivity.this, "Some problems", Toast.LENGTH_LONG);
                        toast1.setGravity(Gravity.BOTTOM, 10, 50);
                        toast1.show();

                        break;

                }
            } else {
                Toast toast1 = Toast.makeText(NoteActivity.this, "Exception", Toast.LENGTH_LONG);
                toast1.setGravity(Gravity.BOTTOM, 10, 50);
                toast1.show();
            }
        }
    }

    public class LogOut {
        private String sessionID;

        LogOut(String _sessionID) {
            sessionID = _sessionID;
        }

        public String getSessionID() {
            return sessionID;
        }
    }

    public class MyAsyncTask extends AsyncTask<LogOut, Void, API.LogoutResponse> {

        API apiexception;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected API.LogoutResponse doInBackground(LogOut... params) {


            try {
                return API.getLogout(params[0].sessionID);
            } catch (APIexception apIexception) {
                apIexception.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(API.LogoutResponse result) {
            super.onPostExecute(result);


            if (result != null) {
                switch (result.result) {
                    case 0:

                        Intent intentLogOut = new Intent(NoteActivity.this, MainActivity.class);
                        startActivity(intentLogOut);

                        Toast toast = Toast.makeText(NoteActivity.this, "Log Out Success", Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.BOTTOM, 10, 50);
                        toast.show();
                        break;

                    case 1:
                        Toast toast1 = Toast.makeText(NoteActivity.this, "Log Out not compleate", Toast.LENGTH_LONG);
                        toast1.setGravity(Gravity.BOTTOM, 10, 50);
                        toast1.show();

                        break;

                }
            } else {
                Toast toast1 = Toast.makeText(NoteActivity.this, "Exception", Toast.LENGTH_LONG);
                toast1.setGravity(Gravity.BOTTOM, 10, 50);
                toast1.show();
            }
        }
    }
}



