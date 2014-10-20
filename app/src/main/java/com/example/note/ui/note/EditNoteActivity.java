package com.example.note.ui.note;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.example.note.MyApplication;
import com.example.note.R;
import com.example.note.api.API;
import com.example.note.api.API.EditNoteResponse;
import com.example.note.api.APIexception;
import com.example.note.model.dataBase.DataBaseContentProvider;
import com.example.note.model.dataBase.UserDataBase;

public class EditNoteActivity extends Activity {

    private final String LONG_EXTRA = "ID";
    private final String INT_EXTRA = "POSITION";
    protected EditText editNote;
    protected String title;
    protected NoteAdapter noteAdapter;

    Cursor c;

    @Override
    protected void onResume() {
        super.onResume();


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_note);
        editNote = (EditText) findViewById(R.id.editNote);

        String[] myContent = {UserDataBase.TableData._ID,
                UserDataBase.TableData.TITLE,
                UserDataBase.TableData.SHORT_CONTENT};
        getContentResolver().query(DataBaseContentProvider.URI_NOTE, myContent, null, null, "_ID");
        getActionBar().setDisplayHomeAsUpEnabled(true);

        new GetNoteAsyncTask().execute(new GetNote(((MyApplication) getApplication()).getLocalData().getSessionID(), getIntent().getLongExtra(LONG_EXTRA, -1)));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.edit_note_menu, menu);
        return true;
    }

    ;

    public void backPreasd() {
        Intent intentLogOut = new Intent(EditNoteActivity.this, NoteActivity.class);
        intentLogOut.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intentLogOut);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_edit_note:
                new EditNoteAsyncTask().execute(new EditNote(((MyApplication) getApplication()).getLocalData().getSessionID(), getIntent().getLongExtra(LONG_EXTRA, -1),
                        editNote.getText().toString()));

                backPreasd();
                finish();
                break;
            case android.R.id.home:
                backPreasd();
                return true;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    ;

    public class GetNote {
        private long noteID;
        private String sessionID;

        public GetNote(String _sessionID, long _noteID) {
            noteID = _noteID;
            sessionID = _sessionID;
        }

        public long getNoteID() {
            return noteID;
        }

        public String getSessionID() {
            return sessionID;
        }
    }

    ;

    public class EditNote {
        private long noteID;
        private String sessionID;
        private String text;

        public EditNote(String _sessionID, long _noteID, String _text) {
            noteID = _noteID;
            sessionID = _sessionID;
            text = _text;
        }

        public long getNoteID() {
            return noteID;
        }

        public String getSessionID() {
            return sessionID;
        }

        public String getText() {
            return text;
        }
    }

    public class EditNoteAsyncTask extends AsyncTask<EditNote, Void, EditNoteResponse> {

        APIexception apiexception;
        API API = new API();
        EditNote request;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected EditNoteResponse doInBackground(EditNote... params) {
            try {
                request = params[0];

                return API.getEditNote(params[0].sessionID, params[0].noteID, params[0].text);
            } catch (APIexception e) {
                apiexception = e;
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(EditNoteResponse result) {
            super.onPostExecute(result);
            if (result == null) {
            } else {
                switch (result.result) {
                    case 0:
                        ContentValues contentValues = new ContentValues();
                        contentValues.put(UserDataBase.TableData._ID, request.getNoteID());
                        contentValues.put(UserDataBase.TableData.TITLE, request.text);


                        getContentResolver().update(DataBaseContentProvider.URI_NOTE, contentValues, UserDataBase.TableData._ID + " = " + request.getNoteID(), null);

                        Toast toast = Toast.makeText(EditNoteActivity.this, "Create note compleate", Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.BOTTOM, 10, 50);
                        toast.show();
                        break;
                    case 2:
                        Toast toast1 = Toast.makeText(EditNoteActivity.this, "problem", Toast.LENGTH_LONG);
                        toast1.setGravity(Gravity.BOTTOM, 10, 50);
                        toast1.show();
                        break;
                    default:
                        Toast toast2 = Toast.makeText(EditNoteActivity.this, "error", Toast.LENGTH_LONG);
                        toast2.setGravity(Gravity.BOTTOM, 10, 50);
                        toast2.show();
                        break;
                }
            }
        }
    }

    public class EditNoteRequest {
        public long noteID;
        public String sessionID;
        public String text;

        public EditNoteRequest(String _sessionID, long _noteID, String _text) {
            noteID = _noteID;
            sessionID = _sessionID;
            text = _text;
        }
    }

    public class GetNoteAsyncTask extends AsyncTask<GetNote, Void, API.GetNoteResponse> {
        APIexception apiexception;
        GetNote request;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected API.GetNoteResponse doInBackground(GetNote... params) {
            API API = new API();
            try {
                request = params[0];
                return API.getNote(params[0].sessionID, params[0].noteID);
            } catch (APIexception e) {
                apiexception = e;
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(API.GetNoteResponse result) {
            super.onPostExecute(result);
            if (result == null) {

            } else {
                switch (result.result) {
                    case 0:
                        getActionBar().setTitle(result.getTitle());
                        editNote.setText(result.getContent());

                        Toast toast = Toast.makeText(EditNoteActivity.this, "Create note compleate", Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.BOTTOM, 10, 50);
                        toast.show();
                        break;
                    case 2:
                        Toast toast1 = Toast.makeText(EditNoteActivity.this, "problems", Toast.LENGTH_LONG);
                        toast1.setGravity(Gravity.BOTTOM, 10, 50);
                        toast1.show();
                        break;
                    default:
                        Toast toast2 = Toast.makeText(EditNoteActivity.this, "error", Toast.LENGTH_LONG);
                        toast2.setGravity(Gravity.BOTTOM, 10, 50);
                        toast2.show();
                        break;
                }
            }
        }
    }
}
