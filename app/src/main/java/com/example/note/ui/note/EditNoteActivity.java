package com.example.note.ui.note;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;
import com.example.note.api.API.EditNoteResponse;
import com.example.note.MyApplication;
import com.example.note.R;
import com.example.note.api.API;
import com.example.note.api.APIexception;
import com.example.note.ui.note.NoteActivity;

import static com.example.note.ui.note.NoteActivity.*;

public class EditNoteActivity extends Activity {
    protected EditText editNote;
    protected String title;

    private final String LONG_EXTRA  = "ID";
    private final String INT_EXTRA   = "POSITION";
    protected NoteAdapter noteAdapter;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_note);
        editNote = (EditText) findViewById(R.id.editNote);
        noteAdapter = new NoteAdapter(this, ((MyApplication)getApplication()).getLocalData());
        new GetNoteAsyncTask().execute(new GetNote(((MyApplication)getApplication()).getLocalData().getSessionID(), getIntent().getLongExtra(LONG_EXTRA, -1)));

    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.edit_note_menu, menu);
        return true;
    };

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_edit_note:

                new EditNoteAsyncTask().execute(new EditNote(((MyApplication)getApplication()).getLocalData().getSessionID(), getIntent().getLongExtra(LONG_EXTRA, -1) , editNote.getText().toString()));


                finish();
                break;

            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    };
    public class EditNote{
        private long noteID;
        private String sessionID;
        private String text;

        public EditNote(String _sessionID, long _noteID, String _text) {
            noteID    = _noteID;
            sessionID = _sessionID;
            text      = _text;
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

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        API API = new API();

        @Override
        protected EditNoteResponse doInBackground(EditNote... params) {

            try {
                ((MyApplication)getApplication()).getLocalData().addLocalNoteForIndex(getActionBar().getTitle().toString(), params[0].text, getIntent().getLongExtra(LONG_EXTRA, -1) , getIntent().getIntExtra(INT_EXTRA, -1));
               // Log.d("EDIT_NOTE", );

                noteAdapter.notifyDataSetChanged();
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
               // APIUtils.ToastException(EditNoteActivity.this, apiexception);
            } else {
                switch (result.result) {
                    case 0:
                        noteAdapter.notifyDataSetChanged();
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


     public class EditNoteRequest{
         public long noteID;
         public String sessionID;
         public String text;

         public EditNoteRequest(String _sessionID, long _noteID, String _text){
             noteID 	  = _noteID;
             sessionID = _sessionID;
             text      = _text;
         }
     }
    public class GetNoteAsyncTask extends AsyncTask<GetNote, Void, API.GetNoteResponse> {

        APIexception apiexception;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected API.GetNoteResponse doInBackground(GetNote... params) {
            API API = new API();
            try {
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



            if(result == null){
              //  APIUtils.ToastException(EditNoteActivity.this, apiexception);
            }else{
                switch(result.result) {
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

