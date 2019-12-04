package com.example.planningpoker.util;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.planningpoker.R;
import com.example.planningpoker.activity.MainActivity;
import com.example.planningpoker.fragment.JoinSessionFragment;
import com.example.planningpoker.fragment.QuestionFragment;
import com.example.planningpoker.fragment.StatisticsFragment;
import com.example.planningpoker.fragment.VoteFragment;

public class FragmentNavigation {

    private static FragmentNavigation sInstance;
    private FragmentManager mFragmentManager;
    private FragmentTransaction mFragmentTransaction;
    private Integer fragmentHolder;

    private FragmentNavigation(Context context) {
        fragmentHolder = R.id.fragment_holder;
        if (context != null) {
            mFragmentManager = ((MainActivity) context).getSupportFragmentManager();
        }
    }

    public static FragmentNavigation getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new FragmentNavigation(context);
        }
        return sInstance;
    }

    private void replaceFragment(Fragment fragment, Boolean addToBackStack) {
        mFragmentTransaction = mFragmentManager.beginTransaction();
        mFragmentTransaction.replace(fragmentHolder, fragment, fragment.getTag());
        if (addToBackStack) {
            mFragmentTransaction.addToBackStack(fragment.getTag());
        }
        mFragmentTransaction.commit();
    }

    public Fragment getCurrentragment(){
        if(mFragmentManager.getBackStackEntryCount() > 0){
            return mFragmentManager.getFragments().get(mFragmentManager.getFragments().size()-1);
        }
        return null;
    }

    public void showJoinSessionFragment(){
        JoinSessionFragment fragment = new JoinSessionFragment();
        replaceFragment(fragment, false);
    }

    public void showQuestionFragment(Bundle bundle){
        QuestionFragment fragment = new QuestionFragment();
        fragment.setArguments(bundle);
        replaceFragment(fragment, true);
    }

    public void showVoteFragment(Bundle bundle) {
        VoteFragment fragment = new VoteFragment();
        fragment.setArguments(bundle);
        replaceFragment(fragment, true);
    }

    public void showStatisticsFragment(Bundle bundle) {
        StatisticsFragment fragment = new StatisticsFragment();
        fragment.setArguments(bundle);
        replaceFragment(fragment, true);
    }
}
