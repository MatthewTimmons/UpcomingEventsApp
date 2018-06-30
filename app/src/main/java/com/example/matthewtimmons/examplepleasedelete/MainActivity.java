package com.example.matthewtimmons.examplepleasedelete;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    ViewPager viewPager = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        viewPager = findViewById(R.id.pager);
        FragmentManager fragmentManager = getSupportFragmentManager();
        viewPager.setAdapter(new Adapter(fragmentManager));

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                    switch (menuItem.getItemId()) {
                        case R.id.action_concerts:
                            Toast.makeText(MainActivity.this, "Concerts clicked", Toast.LENGTH_SHORT).show();
                            break;
                        case R.id.action_games:
                            Toast.makeText(MainActivity.this, "Games clicked", Toast.LENGTH_SHORT).show();
                            break;
                        case R.id.action_movies:
                            Toast.makeText(MainActivity.this, "Movies clicked", Toast.LENGTH_SHORT).show();
                            break;
                    }
                    return true;
            }
        });
    }
}
