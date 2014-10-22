package com.example.note.ui.note;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.AsyncTaskLoader;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.note.api.API;
import com.example.note.MyApplication;
import com.example.note.R;
import com.example.note.api.APIexception;
import com.example.note.ui.login.MainActivity;

import java.io.Serializable;

public class ChangePasswordActivity extends Activity implements OnClickListener {
    static API API;
    private final String KEY_FOR_CHANGE_PASS = "KEY_FOR_CHANGE_PASS";
    public LoaderManager.LoaderCallbacks<API.ChangePasswordResponse> changePasswordResponseLoaderCallbacks = new LoaderManager.LoaderCallbacks<API.ChangePasswordResponse>() {

        @Override
        public Loader<API.ChangePasswordResponse> onCreateLoader(int id, Bundle args) {
            return new ChangeUserPasswordLoader(ChangePasswordActivity.this, (ChangeUserPassword) args.getSerializable(KEY_FOR_CHANGE_PASS));
        }

        @Override
        public void onLoadFinished(Loader<API.ChangePasswordResponse> loader, API.ChangePasswordResponse data) {
            Toast toast = Toast.makeText(ChangePasswordActivity.this, "Cheng password complite", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.BOTTOM, 10, 50);
            toast.show();

            Intent intent = new Intent(ChangePasswordActivity.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }

        @Override
        public void onLoaderReset(Loader<API.ChangePasswordResponse> loader) {

        }
    };
    protected NoteAdapter noteAdapter;
    private EditText oldPassword;
    private EditText newPassword;
    private EditText reenterNewPassword;
    private Button enterButton;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_password_activity);
        API = new API();

        oldPassword = (EditText) findViewById(R.id.oldPassword);
        newPassword = (EditText) findViewById(R.id.newPassword);
        reenterNewPassword = (EditText) findViewById(R.id.reenterPassword);

        enterButton = (Button) findViewById(R.id.buttonEnterChenge);
        enterButton.setOnClickListener(this);
        getActionBar().setDisplayHomeAsUpEnabled(true);



    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case android.R.id.home:
                Intent intentLogOut = new Intent(ChangePasswordActivity.this, NoteActivity.class);
                intentLogOut.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intentLogOut);
                return true;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        final String SESSION_ID = ((MyApplication) getApplication()).getLocalData().getSessionID();
        final String OLD_PASSWORD = oldPassword.getText().toString();
        final String NEW_PASSWORD = newPassword.getText().toString();
        final String REENTER_NEW_PASSWORD = reenterNewPassword.getText().toString();
        Bundle changeBundle = new Bundle();
        ChangeUserPassword changeUserPassword = new ChangeUserPassword(SESSION_ID, OLD_PASSWORD, NEW_PASSWORD);
        changeBundle.putSerializable(KEY_FOR_CHANGE_PASS, changeUserPassword);

        if (NEW_PASSWORD.equals(REENTER_NEW_PASSWORD)) {
            getLoaderManager().initLoader(1, changeBundle, changePasswordResponseLoaderCallbacks).forceLoad();
           /* new MyAsyncTask().execute(new ChangeUserPassword(SESSION_ID, OLD_PASSWORD, NEW_PASSWORD));*/
        } else {
            Toast toast = Toast.makeText(this, "Data entry errors", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.BOTTOM, 10, 50);
            toast.show();
        }
    }

    public static class ChangeUserPasswordLoader extends AsyncTaskLoader<API.ChangePasswordResponse> {
        ChangeUserPassword changeUserPassword;

        public ChangeUserPasswordLoader(Context context, ChangeUserPassword changeUserPassword) {
            super(context);
            this.changeUserPassword = changeUserPassword;
        }

        @Override
        public API.ChangePasswordResponse loadInBackground() {
            try {
                return API.getChangePassword(changeUserPassword.getSessionID(), changeUserPassword.getNewPassword(), changeUserPassword.getOldPassword());
            } catch (APIexception apIexception) {
                apIexception.printStackTrace();
            }
            return null;
        }
    }

    public class ChangeUserPassword implements Serializable {
        private String sessionID;
        private String oldPassword;
        private String newPassword;

        ChangeUserPassword(String _sessionID, String _oldPassword, String _newPassword) {
            sessionID = _sessionID;
            oldPassword = _oldPassword;
            newPassword = _newPassword;
        }

        public String getSessionID() {
            return sessionID;
        }

        public String getOldPassword() {
            return oldPassword;
        }

        public String getNewPassword() {
            return newPassword;
        }
    }

    public class MyAsyncTask extends AsyncTask<ChangeUserPassword, Void, API.ChangePasswordResponse> {

        APIexception apiexception;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            oldPassword.setEnabled(false);
            newPassword.setEnabled(false);
            reenterNewPassword.setEnabled(false);

            enterButton.setEnabled(false);


        }

        @Override
        protected com.example.note.api.API.ChangePasswordResponse doInBackground(ChangeUserPassword... params) {


            try {
                return API.getChangePassword(params[0].getSessionID(), params[0].getNewPassword(), params[0].getOldPassword());
            } catch (APIexception apIexception) {
                apIexception.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(com.example.note.api.API.ChangePasswordResponse result) {
            super.onPostExecute(result);

            oldPassword.setEnabled(true);
            newPassword.setEnabled(true);
            reenterNewPassword.setEnabled(true);
            enterButton.setEnabled(true);


            if (result != null) {
                switch (result.result) {
                    case 0:

                        Toast toast = Toast.makeText(ChangePasswordActivity.this, "Cheng password complite", Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.BOTTOM, 10, 50);
                        toast.show();

                        Intent intent = new Intent(ChangePasswordActivity.this, MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);


                        break;

                    case 2:

                        Toast toast1 = Toast.makeText(ChangePasswordActivity.this, "Come up a new password", Toast.LENGTH_LONG);
                        toast1.setGravity(Gravity.BOTTOM, 10, 50);
                        toast1.show();

                        break;

                }
            } else {
                Toast toast1 = Toast.makeText(ChangePasswordActivity.this, "Exception", Toast.LENGTH_LONG);
                toast1.setGravity(Gravity.BOTTOM, 10, 50);
                toast1.show();
            }
        }
    }
}
