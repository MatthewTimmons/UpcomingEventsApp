package com.matthewtimmons.upcomingeventsapp.authorization;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.CollectionReference;
import com.matthewtimmons.upcomingeventsapp.R;
import com.matthewtimmons.upcomingeventsapp.activities.MainActivity;
import com.matthewtimmons.upcomingeventsapp.manager.FirebaseAuthHelper;
import com.matthewtimmons.upcomingeventsapp.manager.Firestore;
import com.matthewtimmons.upcomingeventsapp.models.User;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class AuthorizeUserActivity extends AppCompatActivity {
    FirebaseAuth firebaseAuth;
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

        if (firebaseAuth.getCurrentUser() != null) {
                finish();
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
        }

        activityConstraintLayout = findViewById(R.id.authorization_activity);

//        Fragment signUpFragment = new SignUpFragment();
//        getSupportFragmentManager().beginTransaction().add(R.id.authorization_fragment_container, signUpFragment).commit();

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
