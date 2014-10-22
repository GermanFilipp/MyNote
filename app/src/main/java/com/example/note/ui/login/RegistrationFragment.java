package com.example.note.ui.login;

import android.app.Fragment;
import android.app.LoaderManager;
import android.content.AsyncTaskLoader;
import android.content.Context;
import android.content.Loader;
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

import com.example.note.R;
import com.example.note.api.API;
import com.example.note.api.APIexception;

public class RegistrationFragment extends Fragment implements View.OnClickListener {

    private final String KEY_FOR_LOGIN = "KEY_FOR_LOGIN";
    public LoaderManager.LoaderCallbacks<API.RegistesResponse> registesResponseLoaderCallbacks = new LoaderManager.LoaderCallbacks<API.RegistesResponse>() {

        LoginRequest request;

        @Override
        public Loader<API.RegistesResponse> onCreateLoader(int id, Bundle args) {
            request = (LoginRequest) args.getParcelable(KEY_FOR_LOGIN);
            return new RegisterLoader(getActivity(), (LoginRequest) args.getParcelable(KEY_FOR_LOGIN));
        }

        @Override
        public void onLoadFinished(Loader<API.RegistesResponse> loader, API.RegistesResponse data) {
            if (data.result == 0) {
                Toast toast = Toast.makeText(getActivity(), "Received", Toast.LENGTH_LONG);
                toast.setGravity(Gravity.BOTTOM, 10, 50);
                toast.show();
                Registration.setEnabled(true);
            }
            if (data.result == 1) {
                Toast toast = Toast.makeText(getActivity(), "failed", Toast.LENGTH_LONG);
                toast.setGravity(Gravity.BOTTOM, 10, 50);
                toast.show();
                Registration.setEnabled(true);
            }
        }

        @Override
        public void onLoaderReset(Loader<API.RegistesResponse> loader) {

        }
    };
    // MyAsyncTask mt;
    API api = new API();
    MainActivity m = new MainActivity();
    private EditText LogText;
    private EditText PassText;
    private EditText RepeatPassText;
    private Button Registration;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstance) {
        return inflater.inflate(R.layout.register_frag, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle saveInstanceState) {
        super.onViewCreated(view, saveInstanceState);

        LogText = (EditText) view.findViewById(R.id.logText);
        PassText = (EditText) view.findViewById(R.id.passText);
        RepeatPassText = (EditText) view.findViewById(R.id.repeatPassText);
        Registration = (Button) view.findViewById(R.id.button1);
        Registration.setOnClickListener(this);

    }

    public void onClick(View arg0) {
        final String LOGIN = LogText.getText().toString();
        final String PASS = PassText.getText().toString();
        final String REPEATPASS = RepeatPassText.getText().toString();
        if (PASS.equals(REPEATPASS) && !TextUtils.isEmpty(LOGIN) && !TextUtils.isEmpty(PASS) && !TextUtils.isEmpty(REPEATPASS)) {


            Bundle loginBundle = new Bundle();
            LoginRequest loginRequest = new LoginRequest(LOGIN, PASS);
            loginBundle.putParcelable(KEY_FOR_LOGIN, loginRequest);
            getLoaderManager().initLoader(1, loginBundle, registesResponseLoaderCallbacks).forceLoad();
            // new MyAsyncTask().execute(new LoginRequest(LOGIN, PASS));


//			Intent intent = new Intent(getActivity(), MainActivity.class);
//		    startActivity(intent);
        }

        Registration.setEnabled(true);
        Registration.setEnabled(false);

        //new MyAsyncTask().execute(new LoginRequest(LOGIN, PASS));


    }

    public static class RegisterLoader extends AsyncTaskLoader<API.RegistesResponse> {
        LoginRequest loginRequest;

        public RegisterLoader(Context context, LoginRequest loginRequest) {
            super(context);
            this.loginRequest = loginRequest;
        }

        @Override
        public API.RegistesResponse loadInBackground() {
            try {
                return new API().register(loginRequest.login, loginRequest.pass);
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

  /*  public class MyAsyncTask extends AsyncTask<LoginRequest, Void, API.RegistesResponse> {
        APIexception excep;

        @Override
        protected API.RegistesResponse doInBackground(LoginRequest... params) {


            try {
                return new API().register(params[0].login, params[0].pass);
            } catch (APIexception apIexception) {
                apIexception.printStackTrace();
                excep = apIexception;
            }
            return null;
        }


        protected void onPostExecute(API.RegistesResponse result) {
            super.onPostExecute(result);


            if (result != null) {
                if (result.result == 0) {
                    Toast toast = Toast.makeText(getActivity(), "Received", Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.BOTTOM, 10, 50);
                    toast.show();
                    Registration.setEnabled(true);
                }
                if (result.result == 1) {
                    Toast toast = Toast.makeText(getActivity(), "failed", Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.BOTTOM, 10, 50);
                    toast.show();
                    Registration.setEnabled(true);
                }

            } else {
                UiUtils.showToastByApiException(getActivity(), excep);
            }


        }
    }*/


}
