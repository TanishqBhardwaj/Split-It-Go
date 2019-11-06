package com.example.SplitItGo;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

public class HomeActivity extends AppCompatActivity {

    private NavigationView mNavigationView;
    private DrawerLayout mDrawerLayout;
    BottomNavigationView bottomNavigationView;
    Toolbar toolbar;
    ActionBarDrawerToggle actionBarDrawerToggle;
    PreferenceUtils pref;
    TextView textViewUsername;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        pref = new PreferenceUtils(getApplicationContext());
        mNavigationView = findViewById(R.id.navigation_view);
        mDrawerLayout = findViewById(R.id.drawer_layout);
        toolbar = findViewById(R.id.toolbar);
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        View headerView = mNavigationView.getHeaderView(0);
        textViewUsername= headerView.findViewById((R.id.textUsername));
        actionBarDrawerToggle = new ActionBarDrawerToggle(HomeActivity.this, mDrawerLayout, toolbar,
                R.string.app_name, R.string.app_name);
        mDrawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        textViewUsername.setText(pref.getKeyUsername());

        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                int id = menuItem.getItemId();
                switch (id) {
                    case R.id.nav_logout :
                        pref.logoutUser();
                        Intent intent = new Intent(HomeActivity.this, MainActivity.class);
                        startActivity(intent);
                        break;
                }
                return true;
            }
        });

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.nav_bottom_home:
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_frame_home, new HomeFragment()).commit();
                        break;
                    case R.id.nav_bottom_groups:
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_frame_home, new GroupsFragment()).commit();
                        break;
                    case R.id.nav_bottom_activity:
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_frame_home, new ActivityFragment()).commit();
                        break;
                }
                return true;
            }
        });
    }
}