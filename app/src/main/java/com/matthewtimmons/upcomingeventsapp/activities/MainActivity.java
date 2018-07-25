package com.matthewtimmons.upcomingeventsapp.activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;
import com.matthewtimmons.upcomingeventsapp.adapters.EventPagerAdapter;
import com.matthewtimmons.upcomingeventsapp.R;
import com.matthewtimmons.upcomingeventsapp.authorization.AuthorizeUserActivity;
import com.matthewtimmons.upcomingeventsapp.authorization.SignUpFragment;
import com.matthewtimmons.upcomingeventsapp.manager.Firestore;
import com.squareup.picasso.Picasso;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;


public class MainActivity extends AppCompatActivity {
    FirebaseAuth firebaseAuth;
    ViewPager viewPager;
    FragmentPagerAdapter pagerAdapter;
    BottomNavigationView bottomNavigationView;
    DrawerLayout drawerLayout;
    String currentUserDisplayname;
    String currentUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        firebaseAuth = FirebaseAuth.getInstance();

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        viewPager = findViewById(R.id.pager);
        pagerAdapter = new EventPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(pagerAdapter);

        //TODO: Change current user to be dynamic
        currentUserId = firebaseAuth.getCurrentUser().getUid();
        currentUserDisplayname = firebaseAuth.getCurrentUser().getDisplayName();


        // Set up nav drawer
        drawerLayout = findViewById(R.id.nav_drawer);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        NavigationView navigationView = findViewById(R.id.navigation_view);

        View navHeader = navigationView.getHeaderView(0);
        final ImageView profilePhotoImageView = navHeader.findViewById(R.id.nav_bar_profile_photo);

        profilePhotoImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentToProfileViewActivity = new Intent(MainActivity.this, ProfileViewActivity.class);
                intentToProfileViewActivity.putExtra("CURRENT_USER", currentUserId);
                startActivity(intentToProfileViewActivity);
            }
        });

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
                                                             @Override
                                                             public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
//                                                                 Toast.makeText(MainActivity.this, menuItem.toString(), Toast.LENGTH_SHORT).show();
                                                                 switch (menuItem.getItemId()) {
                                                                     case R.id.nav_drawer_favorites:
                                                                         Intent intentToDrawersFavorites = new Intent(MainActivity.this, FavoritesActivity.class);
                                                                         startActivity(intentToDrawersFavorites);
                                                                         return true;
                                                                     case R.id.nav_drawer_shared_games:
                                                                         Intent intentToSharedGames = new Intent(MainActivity.this, SharedGamesActivity.class);
                                                                         intentToSharedGames.putExtra("CURRENT_USER", currentUserId);
                                                                         startActivity(intentToSharedGames);
                                                                         return true;
                                                                     case R.id.nav_drawer_friends:
                                                                         Intent intentToFriends = new Intent(MainActivity.this, FriendsListActivity.class);
                                                                         startActivity(intentToFriends);
                                                                         return true;
                                                                     case R.id.nav_drawer_sign_out:
                                                                         firebaseAuth.signOut();
                                                                         Intent intentToStartSignInPage = new Intent(MainActivity.this, AuthorizeUserActivity.class);
                                                                         startActivity(intentToStartSignInPage);
                                                                         return true;
                                                                 }
                                                                 return true;
                                                             }
                                                         });




                bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                        switch (menuItem.getItemId()) {
                            case R.id.action_concerts:
                                viewPager.setCurrentItem(EventPagerAdapter.INDEX_CONCERTS);
                                break;
                            case R.id.action_games:
                                viewPager.setCurrentItem(EventPagerAdapter.INDEX_GAMES);
                                break;
                            case R.id.action_movies:
                                viewPager.setCurrentItem(EventPagerAdapter.INDEX_MOVIES);
                                break;
                        }
                        return true;
                    }
                });

        // Selects the current menu icon when swiping left or right
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                View view;
                switch(i) {
                    case 0:
                        view = bottomNavigationView.findViewById(R.id.action_concerts);
                        view.performClick();
                        break;
                    case 1:
                        view = bottomNavigationView.findViewById(R.id.action_games);
                        view.performClick();
                        break;
                    case 2:
                        view = bottomNavigationView.findViewById(R.id.action_movies);
                        view.performClick();
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });

        FirebaseFirestore.getInstance().document("users/" + currentUserId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                try {
                    Picasso.get().load(task.getResult().get("profilePhotoURL").toString()).error(R.drawable.ic_default_profile_photo).into(profilePhotoImageView);
                } catch (NullPointerException e) {

                }
            }
        });
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.navbar_menu, menu);
        TextView displayNameTextView = findViewById(R.id.current_user_displayname);
        if (displayNameTextView != null) { displayNameTextView.setText("Welcome, " + currentUserDisplayname); }

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.nav_item_hamburger:
                drawerLayout.openDrawer(GravityCompat.START);
//                Toast.makeText(this, "Drawer clicked", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.nav_drawer_favorites:
//                Toast.makeText(MainActivity.this, "You clicked on Favorites", Toast.LENGTH_LONG).show();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
