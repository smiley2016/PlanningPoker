package com.example.admin.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.example.admin.R;
import com.example.admin.fragment.HomeFragment;
import com.example.admin.util.FragmentNavigation;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class AdminActivity extends AppCompatActivity {
    private static final String TAG = AdminActivity.class.getName();
    private FirebaseUser user;
    private DrawerLayout drawerLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle toogle = new ActionBarDrawerToggle(this, drawerLayout,toolbar,
                R.string.Open, R.string.Close);

        drawerLayout.addDrawerListener(toogle);
        toogle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                int id = menuItem.getItemId();
                switch (id){
                    case R.id.create_session:
                        FragmentNavigation.getInstance(AdminActivity.this).showCreateSessionFragment();
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    case R.id.sessions:
                        if(FragmentNavigation.getInstance(AdminActivity.this)
                                .getCurrentragment() instanceof HomeFragment){
                            drawerLayout.closeDrawer(GravityCompat.START);
                            break;
                        }
                        FragmentNavigation.getInstance(AdminActivity.this).showHomeFragment();
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    case R.id.sign_out:
                        FirebaseAuth.getInstance().signOut();
                        FragmentNavigation.getInstance(AdminActivity.this).showLoginFragment();
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;

                }
                return true;
            }
        });

        FragmentNavigation.getInstance(this).showHomeFragment();

    }

    @Override
    protected void onStart() {
        Log.e(TAG, "onStart: ");
        super.onStart();
        user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            FragmentNavigation.getInstance(AdminActivity.this).showLoginFragment();
        } else {
            FragmentNavigation.getInstance(AdminActivity.this).showHomeFragment();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (user != null) {
            FirebaseAuth.getInstance().signOut();
            finishAffinity();
        }
    }

    @Override
    public void onBackPressed() {
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }else{
            super.onBackPressed();
        }
    }
}
