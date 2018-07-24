package com.matthewtimmons.upcomingeventsapp.authorization;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.matthewtimmons.upcomingeventsapp.R;
import com.matthewtimmons.upcomingeventsapp.manager.FirebaseAuthHelper;

import java.util.Arrays;
import java.util.List;

public class AuthorizeUserActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authorize);

        Fragment signUpFragment = new SignUpFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.authorization_fragment_container, signUpFragment).commit();
    }
}
