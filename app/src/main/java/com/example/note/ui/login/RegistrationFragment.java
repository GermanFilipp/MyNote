package com.example.note.ui.login;

import android.app.Fragment;
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

import com.example.note.api.API;
import com.example.note.R;
import com.example.note.api.APIexception;
import com.example.note.utils.UiUtils;

public class RegistrationFragment extends Fragment implements View.OnClickListener {

    MyAsyncTask mt;
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
            new MyAsyncTask().execute(new LoginRequest(LOGIN, PASS));


//			Intent intent = new Intent(getActivity(), MainActivity.class);
//		    startActivity(intent);
        }

        Registration.setEnabled(true);
        Registration.setEnabled(false);

        new MyAsyncTask().execute(new LoginRequest(LOGIN, PASS));


    }

    public static class LoginRequest {

        String login = "";
        String pass = "";

        public LoginRequest(String l, String p) {
            login = l;
            pass = p;

        }
    }

    public class MyAsyncTask extends AsyncTask<LoginRequest, Void, API.RegistesResponse> {
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
    }


}
