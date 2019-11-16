package com.example.admin.util;

import android.app.Activity;
import android.content.Context;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.admin.R;
import com.example.admin.activity.AdminActivity;
import com.example.admin.fragment.CreateSessionFragment;
import com.example.admin.fragment.ForgotDataFragment;
import com.example.admin.fragment.LoginFragment;

public class FragmentNavigation {
    private static FragmentNavigation sInstance;
    private FragmentManager mFragmentManager;
    private FragmentTransaction mFragmentTransaction;
    private Integer fragmentHolder;

    private FragmentNavigation(Context context) {
        fragmentHolder = R.id.fragment_holder;
        if(context != null){
            mFragmentManager = ((AdminActivity) context).getSupportFragmentManager();
        }
    }

    public static FragmentNavigation getInstance(Context context){
        if( sInstance == null){
            sInstance = new FragmentNavigation(context);
        }
        return sInstance;
    }

    private void replaceFragment(Fragment fragment, Boolean addToBackStack){
        mFragmentTransaction =  mFragmentManager.beginTransaction();
        mFragmentTransaction.replace(fragmentHolder, fragment, fragment.getTag());
        if(addToBackStack){
            mFragmentTransaction.addToBackStack(fragment.getTag());
        }
        mFragmentTransaction.commit();
    }

    public void showLoginFragment(){
        LoginFragment fragment = new LoginFragment();
        replaceFragment(fragment, false);
    }

    public void showCreateSessionFragment(){
        CreateSessionFragment fragment = new CreateSessionFragment();
        replaceFragment(fragment, true);
    }

    public void showForgotDataFragment(){
        ForgotDataFragment fragment = new ForgotDataFragment();
        replaceFragment(fragment, true);
    }
}
