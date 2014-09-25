/*
package com.example.note.api;

import android.os.AsyncTask;

import com.example.note.remote.DataBaseUsers;
import com.example.note.remote.RemouteUser;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
@Deprecated

public class APIold {




	public boolean setUser(String login, String password) {
        boolean userCreate = false;
        ArrayList<RemouteUser> mUsers = DataBaseUsers.getInstance().getUsers();

        try {
            TimeUnit.SECONDS.sleep(3);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (mUsers.isEmpty()) { DataBaseUsers.getInstance().setUsers(new RemouteUser(login, password));
			userCreate = true;
		} else {
			for (int i = 0; i < mUsers.size(); i++) {
				if (!mUsers.get(i).login.equals(login)) { DataBaseUsers.getInstance().setUsers(new RemouteUser(login, password));
					userCreate = true;
				}
			}
		}

		return userCreate;
	}

	public boolean checkUser(String login, String password) {
		boolean userExists = false;
		ArrayList<RemouteUser> mUsers = DataBaseUsers.getInstance().getUsers();

        try {
            TimeUnit.SECONDS.sleep(3);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        for (int i = 0; i < mUsers.size(); i++) {
			if (mUsers.get(i).login.equals(login)&& mUsers.get(i).password.equals(password)) {
				userExists = true;
			}
		}

		return userExists;
	}

	public boolean chengPassword(String login, String password,
			String oldPassword) {
		boolean chengPassword = false;
		ArrayList<RemouteUser> mUsers = DataBaseUsers.getInstance().getUsers();
		for (int i = 0; i < mUsers.size(); i++) {
			if (mUsers.get(i).login.equals(login)&& mUsers.get(i).password.equals(oldPassword)) {
				DataBaseUsers.getInstance().setUsersChengPassword(i, new RemouteUser(login, password));
				chengPassword = true;
			}
		}
		return chengPassword;
	}

	public void putNote(String LOGIN, String NOTE, String NOTE_TITLE_NOTE) {
		DataBaseUsers.getInstance().setNote(LOGIN, NOTE, NOTE_TITLE_NOTE);
	}

    class MyAsyncTaskSetUser extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {

            try {
                TimeUnit.SECONDS.sleep(3);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }


        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
        }

    }
}
*/
