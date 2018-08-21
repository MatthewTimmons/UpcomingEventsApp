package com.matthewtimmons.upcomingeventsapp.authorization;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.matthewtimmons.upcomingeventsapp.manager.CircleTransform;
import com.matthewtimmons.upcomingeventsapp.R;
import com.matthewtimmons.upcomingeventsapp.activities.MainActivity;
import com.matthewtimmons.upcomingeventsapp.models.UserManager;
import com.squareup.picasso.Picasso;

public class AuthorizeUserActivity extends AppCompatActivity {
    FirebaseAuth firebaseAuth;
    ImageView appLogoImageView;
    ConstraintLayout activityConstraintLayout;

    FirebaseAuth.AuthStateListener mAuthListener = new FirebaseAuth.AuthStateListener() {
        @Override
        public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
//            Toast.makeText(AuthorizeUserActivity.this, "You were signed in successfully", Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authorize);
        firebaseAuth = FirebaseAuth.getInstance();

//        // TODO: Make sure current user has been set before beginning main activity
//        if (firebaseAuth.getCurrentUser() != null) {
//            UserManager.getInstance(firebaseAuth.getCurrentUser().getUid());
//            Handler handler = new Handler();
//            handler.postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    finish();
//                    Intent intent = new Intent(AuthorizeUserActivity.this, MainActivity.class);
//                    startActivity(intent);
//                }
//            }, 2000);
//        }

        if (firebaseAuth.getCurrentUser() != null) {
            UserManager.getInstance().setCurrentUser();
            finish();
            Intent intent = new Intent(AuthorizeUserActivity.this, MainActivity.class);
            startActivity(intent);
        }

        appLogoImageView = findViewById(R.id.horizon_events_logo);
        activityConstraintLayout = findViewById(R.id.authorization_activity);

        try {
            Picasso.get().load(R.drawable.horizion).transform(new CircleTransform()).into(appLogoImageView);
        } catch (NullPointerException e) {
            Toast.makeText(this, "Could not present logo", Toast.LENGTH_SHORT).show();
        }

        Fragment signInFragment = new SignInFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.authorization_fragment_container, signInFragment).commit();
    }

    @Override
    protected void onResume() {
        super.onResume();
        firebaseAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(mAuthListener != null){
            firebaseAuth.removeAuthStateListener(mAuthListener);
        }
    }
}
