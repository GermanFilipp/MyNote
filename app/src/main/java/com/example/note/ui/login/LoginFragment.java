package com.example.note.ui.login;


import android.app.Fragment;
import android.app.LoaderManager;
import android.content.AsyncTaskLoader;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
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

public class LoginFragment extends Fragment implements View.OnClickListener {

    private static final String PREF_SETTINGS = "Settings";
    private static final String PREF_SETTINGS_LOGIN = "Login";
    private final String KEY_FOR_LOGIN = "KEY_FOR_LOGIN";
    public LoaderManager.LoaderCallbacks<API.LoginResponse> loginResponseLoaderCallbacks = new LoaderManager.LoaderCallbacks<API.LoginResponse>() {
        LoginRequest request;

        @Override
        public Loader<API.LoginResponse> onCreateLoader(int id, Bundle args) {
            request = args.getParcelable(KEY_FOR_LOGIN);
            return new LoginAsyncTaskLoader(getActivity(), (LoginRequest) args.getParcelable(KEY_FOR_LOGIN));
        }

        @Override
        public void onLoadFinished(Loader<API.LoginResponse> loader, API.LoginResponse data) {
            if (data.getUserCreate() == 0) {
                ((MyApplication) getActivity().getApplication()).getLocalData().setSessionID(data.sessionID);
                Toast toast = Toast.makeText(getActivity(), "Received", Toast.LENGTH_LONG);
                toast.setGravity(Gravity.BOTTOM, 10, 50);
                toast.show();
                // getContentResolver().delete(DataBaseContentProvider.URI_NOTE, null, null);
                Intent intent = new Intent(getActivity(), NoteActivity.class);
                startActivity(intent);

            }
            if (data.getUserCreate() == 1) {
                Toast toast1 = Toast.makeText(getActivity(), "failed", Toast.LENGTH_LONG);
                toast1.setGravity(Gravity.BOTTOM, 10, 50);
                toast1.show();
            }
        }

        @Override
        public void onLoaderReset(Loader<API.LoginResponse> loader) {

        }
    };
    API api = new API();
    //MyAsyncTask mt;
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
            Bundle loginBundle = new Bundle();
            LoginRequest loginRequest = new LoginRequest(LOGIN, PASS);
            loginBundle.putParcelable(KEY_FOR_LOGIN, loginRequest);
            getLoaderManager().initLoader(1, loginBundle, loginResponseLoaderCallbacks).forceLoad();
            // new MyAsyncTask().execute(new LoginRequest(LOGIN, PASS));
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

    public static class LoginAsyncTaskLoader extends AsyncTaskLoader<API.LoginResponse> {
        public LoginRequest loginRequest;

        public LoginAsyncTaskLoader(Context context, LoginRequest loginRequest) {
            super(context);
            this.loginRequest = loginRequest;
        }


        @Override
        public API.LoginResponse loadInBackground() {
            try {
                return new API().login(loginRequest.login, loginRequest.pass);
            } catch (APIexception apIexception) {
                apIexception.printStackTrace();
            }
            return null;
        }
    }

    public static class LoginRequest implements Parcelable {

        public final Creator<LoginRequest> CREATOR = new Parcelable.Creator<LoginRequest>() {

            @Override
            public LoginRequest createFromParcel(Parcel source) {
                return new LoginRequest(source);
            }

            @Override
            public LoginRequest[] newArray(int size) {
                return new LoginRequest[size];
            }
        };
        String login = "";
        String pass = "";

        public LoginRequest(String l, String p) {
            login = l;
            pass = p;

        }

        public LoginRequest(Parcel source) {
            source.writeString(login);
            source.writeString(pass);
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(login);
            dest.writeString(pass);
        }
    }



   /* public class MyAsyncTask extends AsyncTask<LoginRequest, Void, API.LoginResponse> {

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

    }*/
}
