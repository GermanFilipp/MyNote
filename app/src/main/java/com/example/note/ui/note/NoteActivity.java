package com.example.note.ui.note;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import com.example.note.api.API.DeleteNoteResponse;
import com.example.note.MyApplication;
import com.example.note.R;
import com.example.note.api.API;
import com.example.note.api.APIexception;
import com.example.note.model.Note;
import com.example.note.ui.login.MainActivity;

import java.util.ArrayList;
import java.util.Arrays;

public class NoteActivity extends Activity {
    protected NoteAdapter noteAdapter;
    protected Button buttonDelete;
    protected ListView lv;
    public API API = new API();
    private static final String LONG_EXTRA = "ID";
    private static final String INT_EXTRA  = "POSITION";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.note_activity);

        new MyNotesListAsyncTask().execute(new  NotesList(((MyApplication)getApplication()).getLocalData().getSessionID()));

        noteAdapter = new NoteAdapter(this, ((MyApplication)getApplication()).getLocalData());

        //buttonDelete = (Button) findViewById(R.id.buttonDelete);

        lv = (ListView) findViewById(R.id.list);
        lv.setAdapter(noteAdapter);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View itemClicked, int position, long id) {
                Intent intent  = new Intent(NoteActivity.this, EditNoteActivity.class);

                intent.putExtra(INT_EXTRA, position);
                intent.putExtra(LONG_EXTRA, id);

                startActivity(intent);
            }
        });

        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                // new DeleteAsyncTask().execute(new Delete(((MyApplication)getApplication()).getLocalData().getSessionID(), id));
                new DeleteAsyncTask().execute(new DeleteRequest(((MyApplication)getApplication()).getLocalData().getSessionID(), id));
                new MyNotesListAsyncTask().execute(new NotesList(((MyApplication) getApplication()).getLocalData().getSessionID()));

                return true;
            }
        });
    }

    public class NotesList {
        private String sessionID;

        NotesList(String _sessionID) {
            sessionID = _sessionID;
        }

        public String getSessionID() {
            return sessionID;
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
                            ArrayList<Note> mNotes = new ArrayList<Note>();

                            for(com.example.note.api.API.NoteResponse item: result.getNotesArray()) {

                             Note note = new Note();
                                note.setID(item.noteID);
                                note.setDescription(item.shortContent);
                                note.setTitle(item.title);
                                mNotes.add(note);
                            }
                            if(noteAdapter != null) {
                                noteAdapter.notifyDataSetChanged();
                            }
                            ((MyApplication) getApplication()).getLocalData().setmNotes(mNotes);
                        }//.setmNotes(mNotes)

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

    @Override
    protected void onResume() {
        noteAdapter.notifyDataSetChanged();
        super.onResume();
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
    public class DeleteRequest{
        private String sessionID;
        private long noteId;

        public DeleteRequest(String _sessionID, long id){
            sessionID = _sessionID;
            noteId    = id;
        }

        public String getSessionID() {
            return sessionID;
        }

        public long getNoteID() {
            return noteId;
        }
    }

    public class DeleteAsyncTask extends AsyncTask<DeleteRequest, Void,DeleteNoteResponse> {

        APIexception apiexception;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected  DeleteNoteResponse doInBackground(DeleteRequest... params) {

            try {
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



            if(result != null){
                switch(result.result) {
                    case 0:
                        noteAdapter.notifyDataSetChanged();
                        break;

                    case 2:
                        Toast toast1 = Toast.makeText(NoteActivity.this, "Some problems",Toast.LENGTH_LONG);
                        toast1.setGravity(Gravity.BOTTOM, 10, 50);
                        toast1.show();

                        break;

                }
            }else {
                Toast toast1 = Toast.makeText(NoteActivity.this, "Exception",Toast.LENGTH_LONG);
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
    public void ClicButton(View v) {
        Toast.makeText(this, "Delete note", Toast.LENGTH_SHORT).show();

    }
}



