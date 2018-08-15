package com.matthewtimmons.upcomingeventsapp.activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.matthewtimmons.upcomingeventsapp.manager.CircleTransform;
import com.matthewtimmons.upcomingeventsapp.adapters.EventPagerAdapter;
import com.matthewtimmons.upcomingeventsapp.R;
import com.matthewtimmons.upcomingeventsapp.authorization.AuthorizeUserActivity;
import com.matthewtimmons.upcomingeventsapp.controllers.UserController;
import com.matthewtimmons.upcomingeventsapp.models.CurrentUserSingleton;
import com.matthewtimmons.upcomingeventsapp.models.User;
import com.squareup.picasso.Picasso;

public class MainActivity extends AppCompatActivity {
    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;
    FragmentPagerAdapter pagerAdapter;
    DrawerLayout navigationDrawerView;
    ViewPager viewPager;
    Toolbar toolbar;
    NavigationView navigationView;
    BottomNavigationView bottomNavigationView;
    ImageView profilePhotoImageView;
    String currentUserId, currentUserDisplayname;
    User currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        currentUserId = firebaseAuth.getCurrentUser().getUid();
        currentUserDisplayname = firebaseAuth.getCurrentUser().getDisplayName();

        CurrentUserSingleton.getInstance(currentUserId);

        navigationView = findViewById(R.id.navigation_view);
        navigationDrawerView = findViewById(R.id.nav_drawer);
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        viewPager = findViewById(R.id.pager);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        pagerAdapter = new EventPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(pagerAdapter);

        View navHeader = navigationView.getHeaderView(0);
        profilePhotoImageView = navHeader.findViewById(R.id.nav_bar_profile_photo);

        // TODO DO I need to make the call to the signed in user now and then wrap everything else in that?
        // Or maybe just move that as far down as possible, so I only wrap what's necessary?

        profilePhotoImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    Intent intentToProfileViewActivity = new Intent(MainActivity.this, ProfileViewActivity.class);
                    intentToProfileViewActivity.putExtra(User.CURRENT_USER_ID, currentUserId);
                    intentToProfileViewActivity.putExtra(User.CURRENT_USER_OBJECT, CurrentUserSingleton.currentUserObject);
                    startActivity(intentToProfileViewActivity);
            }
        });

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
                                                             @Override
                                                             public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                                                                 switch (menuItem.getItemId()) {
                                                                     case R.id.nav_drawer_favorites:
                                                                         Intent intentToDrawersFavorites = new Intent(MainActivity.this, FavoritesActivity.class);
                                                                         startActivity(intentToDrawersFavorites);
                                                                         return true;
                                                                     case R.id.nav_drawer_shared_games:
                                                                         Intent intentToSharedGames = new Intent(MainActivity.this, SharedGamesActivity.class);
                                                                         startActivity(intentToSharedGames);
                                                                         return true;
                                                                     case R.id.nav_drawer_friends:
                                                                         Intent intentToFriends = new Intent(MainActivity.this, FriendsListActivity.class);
                                                                         startActivity(intentToFriends);
                                                                         return true;
                                                                     case R.id.nav_drawer_sign_out:
                                                                         final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(MainActivity.this);
                                                                         View dialogView = getLayoutInflater().inflate(R.layout.dialog_confirm, null);
                                                                         Button confirmButton = dialogView.findViewById(R.id.confirm_button);
                                                                         Button cancelButton = dialogView.findViewById(R.id.cancel_button);

                                                                         dialogBuilder.setView(dialogView);
                                                                         final AlertDialog alertDialog = dialogBuilder.create();
                                                                         alertDialog.show();

                                                                         cancelButton.setOnClickListener(new View.OnClickListener() {
                                                                             @Override
                                                                             public void onClick(View view) {
                                                                                 alertDialog.cancel();
                                                                             }
                                                                         });

                                                                         confirmButton.setOnClickListener(new View.OnClickListener() {
                                                                             @Override
                                                                             public void onClick(View view) {
                                                                             CurrentUserSingleton.clearCurrentUserSingleton();
                                                                             firebaseAuth.signOut();
                                                                             Intent intentToStartSignInPage = new Intent(MainActivity.this, AuthorizeUserActivity.class);
                                                                             intentToStartSignInPage.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                                                             startActivity(intentToStartSignInPage);
                                                                             finish();
                                                                             }
                                                                         });
                                                                         return true;
                                                                     case R.id.nav_drawer_add_events_activity:
                                                                         Intent intentToAddEventsActivity = new Intent(MainActivity.this, AddEventsActivity.class);
                                                                         startActivity(intentToAddEventsActivity);
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

        UserController.getUser(currentUserId, new UserController.GetUserListener() {
            @Override
            public void onUserRetrieved(User user) {
                try {
                    Picasso.get().load(user.getProfilePhotoURL()).transform(new CircleTransform()).error(R.drawable.ic_default_profile_photo).into(profilePhotoImageView);
                } catch (NullPointerException e) {
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.navbar_menu, menu);
        FirebaseFirestore.getInstance().document("users/" + currentUserId).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                TextView displayNameTextView = findViewById(R.id.current_user_displayname);
                String displayName = documentSnapshot.getString("displayName");
                displayNameTextView.setText("Welcome, " + displayName);
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.nav_item_hamburger:
                navigationDrawerView.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
