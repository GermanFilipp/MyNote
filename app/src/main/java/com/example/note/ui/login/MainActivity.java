package com.example.note.ui.login;

import android.app.ActionBar;
import android.app.ActionBar.TabListener;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;

import com.example.note.R;

public class MainActivity extends Activity implements TabListener {

    private static final String	STATE_SELECTED_NAVIGATION_ITEM	= "selected_navigation_item";

    private final Fragment		mFragments[]					= new Fragment[2];

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        if (savedInstanceState == null) {
            mFragments[0] = new LoginFragment();
            mFragments[1] = new RegistrationFragment();
        } else {
            if (savedInstanceState.containsKey(LoginFragment.class.getSimpleName())) {
                mFragments[0] = getFragmentManager().getFragment(savedInstanceState, LoginFragment.class.getSimpleName());
            } else {
                mFragments[0] = new LoginFragment();
            }
            if (savedInstanceState.containsKey(RegistrationFragment.class.getSimpleName())) {
                mFragments[1] = getFragmentManager().getFragment(savedInstanceState, RegistrationFragment.class.getSimpleName());
            } else {
                mFragments[1] = new RegistrationFragment();
            }
        }

        setContentView(R.layout.activity_main);

        final ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        actionBar.addTab(actionBar.newTab().setText(getResources().getString(R.string.Login)).setTabListener(this));
        actionBar.addTab(actionBar.newTab().setText(getResources().getString(R.string.Registration)).setTabListener(this));
    }

    protected void onSaveInstanceState(Bundle outState){
        super.onSaveInstanceState(outState);
        outState.putInt(STATE_SELECTED_NAVIGATION_ITEM, getActionBar().getSelectedNavigationIndex());
        if (mFragments[0].isAdded()) {
            getFragmentManager().putFragment(outState, LoginFragment.class.getSimpleName(), mFragments[0]);
        }
        if (mFragments[1].isAdded()) {
            getFragmentManager().putFragment(outState, RegistrationFragment.class.getSimpleName(), mFragments[1]);
        }
    }

    protected void onRestoreInstanceState(Bundle savedInstanceState){
        super.onRestoreInstanceState(savedInstanceState);

        if (savedInstanceState.containsKey(STATE_SELECTED_NAVIGATION_ITEM)) {
            getActionBar().setSelectedNavigationItem(savedInstanceState.getInt(STATE_SELECTED_NAVIGATION_ITEM));
        }
    }

    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft){
        for (int i = 0; i < mFragments.length; ++i) {
            if (i == tab.getPosition()) {
                if (!mFragments[i].isAdded()) {
                    ft.add(R.id.activity_name, mFragments[i]);
                }
                if (mFragments[i].isDetached()) {
                    ft.attach(mFragments[i]);
                }
            } else {
                if (!mFragments[i].isDetached()) {
                    ft.detach(mFragments[i]);
                }
            }
        }
    }

    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction){
    }

    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction){
    }
}
