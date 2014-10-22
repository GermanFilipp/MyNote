package com.example.note.ui.note;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.AsyncTaskLoader;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;

import com.example.note.MyApplication;
import com.example.note.R;
import com.example.note.api.API;
import com.example.note.api.APIexception;
import com.example.note.model.dataBase.DataBaseContentProvider;
import com.example.note.model.dataBase.UserDataBase;

import java.io.Serializable;

public class NewNoteActivity extends Activity {
    public static API API;
    public String KEY_FOR_NOTE_CREATE = "KEY_FOR_NOTE_CREATE";
    public LoaderManager.LoaderCallbacks<API.CreateNoteResponse> createNoteResponseLoaderCallbacks = new LoaderManager.LoaderCallbacks<API.CreateNoteResponse>() {


        NoteCreate request;

        @Override
        public Loader<API.CreateNoteResponse> onCreateLoader(int id, Bundle args) {


            request = (NoteCreate) args.getSerializable(KEY_FOR_NOTE_CREATE);
            return new NoteCreateLoader(NewNoteActivity.this, (NoteCreate) args.getSerializable(KEY_FOR_NOTE_CREATE));

        }

        @Override
        public void onLoadFinished(Loader<API.CreateNoteResponse> loader, API.CreateNoteResponse data) {


            ContentValues contentValues = new ContentValues();
            contentValues.put(UserDataBase.TableData.TITLE, request.getTitile());
            contentValues.put(UserDataBase.TableData.SHORT_CONTENT, request.getContent());
            contentValues.put(UserDataBase.TableData._ID, data.getNoteID()  /* request.getSessionID()*/);
            getContentResolver().insert(DataBaseContentProvider.URI_NOTE, contentValues);

            Intent intentLogOut = new Intent(NewNoteActivity.this, NoteActivity.class);
            intentLogOut.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intentLogOut);
        }

        @Override
        public void onLoaderReset(Loader<API.CreateNoteResponse> loader) {

        }
    };
    protected EditText textNote;
    protected EditText titleNote;
    //protected Note note = new Note();
    protected NoteAdapter noteAdapter;
    private Cursor c;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_note_activity);
        API = new API();
        String[] myContent = {UserDataBase.TableData._ID,
                UserDataBase.TableData.TITLE,
                UserDataBase.TableData.SHORT_CONTENT};
        c = (getContentResolver().query(DataBaseContentProvider.URI_NOTE, myContent, null, null, "_ID"));
        noteAdapter = new NoteAdapter(this, c);

        textNote = (EditText) findViewById(R.id.textNote);
        titleNote = (EditText) findViewById(R.id.titleNote);
        getActionBar().setDisplayHomeAsUpEnabled(true);


    }

    @Override
    protected void onResume() {
        super.onResume();
        noteAdapter.swapCursor(c);
        noteAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.new_note_menu, menu);
        return super.onCreateOptionsMenu(menu);

    }

    @Override

    public boolean onOptionsItemSelected(MenuItem item) {
        final String NOTE_TITLE_NOTE = titleNote.getText().toString();
        final String NOTE = textNote.getText().toString();
        switch (item.getItemId()) {
            case R.id.action_save_new_note:

                Bundle bndl = new Bundle();
                NoteCreate noteCreate = new NoteCreate(((MyApplication) getApplication()).getLocalData().getSessionID(), NOTE_TITLE_NOTE, NOTE);
                bndl.putSerializable(KEY_FOR_NOTE_CREATE, noteCreate);
                getLoaderManager().initLoader(2, bndl, createNoteResponseLoaderCallbacks).forceLoad();
                //new MyAsyncTask().execute(new  NoteCreate(((MyApplication)getApplication()).getLocalData().getSessionID(), NOTE_TITLE_NOTE, NOTE));


                return true;

            case android.R.id.home:
                Intent intentLogOut = new Intent(NewNoteActivity.this, NoteActivity.class);
                intentLogOut.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intentLogOut);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public static class NoteCreateLoader extends AsyncTaskLoader<API.CreateNoteResponse> {

        public NoteCreate noteCreate;

        public NoteCreateLoader(Context context, NoteCreate noteCreate) {
            super(context);
            this.noteCreate = noteCreate;
        }


        @Override
        public API.CreateNoteResponse loadInBackground() {
            try {
                return API.putNote(noteCreate.getSessionID(), noteCreate.getContent(), noteCreate.getTitile());
            } catch (APIexception apIexception) {
                apIexception.printStackTrace();
            }
            return null;
        }
    }

    public class NoteCreate implements Serializable {
        private String sessionID;
        private String title;
        private String content;

        NoteCreate(String _sessionID, String _title, String _content) {
            sessionID = _sessionID;
            title = _title;
            content = _content;
        }

        public String getTitile() {
            return title;
        }

        public String getContent() {
            return content;
        }

        public String getSessionID() {
            return sessionID;
        }
    }



   /* public class MyAsyncTask extends AsyncTask<NoteCreate, Void, com.example.note.api.API.CreateNoteResponse> {

        NoteCreate request;
        APIexception apiexception;



        @Override
        protected com.example.note.api.API.CreateNoteResponse doInBackground(NoteCreate... params) {


            try {
                request = params[0];
                return API.putNote(params[0].getSessionID(), params[0].getContent(), params[0].getTitile());
            } catch (APIexception apIexception) {
                apIexception.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(com.example.note.api.API.CreateNoteResponse result) {
            super.onPostExecute(result);



            if(result != null){
                switch(result.getCreateNote()) {
                    case 0:

                        ContentValues contentValues = new ContentValues();
                        contentValues.put(UserDataBase.TableData.TITLE, request.getTitile());
                        contentValues.put(UserDataBase.TableData.SHORT_CONTENT, request.getContent());
                        contentValues.put(UserDataBase.TableData._ID, result.getNoteID()  *//* request.getSessionID()*//*);
                        getContentResolver().insert(DataBaseContentProvider.URI_NOTE, contentValues);
                        noteAdapter.swapCursor(c);


                        Intent intentLogOut = new Intent(NewNoteActivity.this, NoteActivity.class);
                        intentLogOut.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intentLogOut);

                        break;

                    case 1:
                        Toast toast1 = Toast.makeText(NewNoteActivity.this, "Fail create note", Toast.LENGTH_LONG);
                        toast1.setGravity(Gravity.BOTTOM, 10, 50);
                        toast1.show();
                        break;

                }
            }else {
                Toast toast1 = Toast.makeText(NewNoteActivity.this, "Exception",Toast.LENGTH_LONG);
                toast1.setGravity(Gravity.BOTTOM, 10, 50);
                toast1.show();
            }
        }
    }*/
}
