package com.example.note.ui.login;


import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.note.MyApplication;
import com.example.note.R;
import com.example.note.api.API;
import com.example.note.api.APIexception;
import com.example.note.ui.note.NoteActivity;
import com.example.note.utils.UiUtils;

public class LoginFragment extends Fragment implements View.OnClickListener {

    private static final String PREF_SETTINGS = "Settings";
    private static final String PREF_SETTINGS_LOGIN = "Login";
    API api = new API();
    MyAsyncTask mt;
    private EditText LogText;
    private EditText PassText;
    private Button Login;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance) {
        return inflater.inflate(R.layout.log_frag, container, false);

    }


    @Override
    public void onViewCreated(View view, Bundle saveInstanceState) {
        super.onViewCreated(view, saveInstanceState);
        LogText = (EditText) view.findViewById(R.id.logText);
        PassText = (EditText) view.findViewById(R.id.passText);
        Login = (Button) view.findViewById(R.id.button1);
        if (saveInstanceState == null) {
            SharedPreferences preferences = getActivity().getSharedPreferences(PREF_SETTINGS, Context.MODE_PRIVATE);
            String stringPreference = preferences.getString("login", "");
            LogText.setText(stringPreference);
            if (!TextUtils.isEmpty(LogText.getText())) {
                PassText.requestFocus();
            } else {
                LogText.requestFocus();
            }
        }
        Login.setOnClickListener(this);
    }

    @Override
    public void onClick(View arg0) {


        final String LOGIN = LogText.getText().toString();
        final String PASS = PassText.getText().toString();

        if (LOGIN.equals("") || PASS.equals("")) {
            Toast toast = Toast.makeText(getActivity(), "Введите логин или пароль", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.BOTTOM, 10, 50);
            toast.show();
        } else {
            new MyAsyncTask().execute(new LoginRequest(LOGIN, PASS));
        }
    }

    private void saveLastLogin() {
        final String LOGIN = LogText.getText().toString();
        final String PASSWORD = LogText.getText().toString();

        SharedPreferences.Editor editor = getActivity().getSharedPreferences(PREF_SETTINGS_LOGIN, Context.MODE_PRIVATE).edit();

        editor.putString("login", LOGIN);
        editor.putString("password", PASSWORD);
        editor.commit();
    }

    public static class LoginRequest {

        String login = "";
        String pass = "";

        public LoginRequest(String l, String p) {
            login = l;
            pass = p;

        }
    }


    public class MyAsyncTask extends AsyncTask<LoginRequest, Void, API.LoginResponse> {

        APIexception excep;

        @Override
        protected void onPostExecute(API.LoginResponse result) {
            super.onPostExecute(result);
            Login.setEnabled(true);
            if (result != null) {
                if (result.getUserCreate() == 0) {
                    ((MyApplication) getActivity().getApplication()).getLocalData().setSessionID(result.sessionID);
                    Toast toast = Toast.makeText(getActivity(), "Received", Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.BOTTOM, 10, 50);
                    toast.show();
                    // getContentResolver().delete(DataBaseContentProvider.URI_NOTE, null, null);
                    Intent intent = new Intent(getActivity(), NoteActivity.class);
                    startActivity(intent);

                }
                if (result.getUserCreate() == 1) {
                    Toast toast1 = Toast.makeText(getActivity(), "failed", Toast.LENGTH_LONG);
                    toast1.setGravity(Gravity.BOTTOM, 10, 50);
                    toast1.show();
                }
            } else {
                UiUtils.showToastByApiException(getActivity(), excep);

            }

        }

        @Override
        protected API.LoginResponse doInBackground(LoginRequest... params) {
            try {
                return new API().login(params[0].login, params[0].pass);
            } catch (APIexception apIexception) {
                apIexception.printStackTrace();
                excep = apIexception;
                return null;
            }
        }

    }
}
