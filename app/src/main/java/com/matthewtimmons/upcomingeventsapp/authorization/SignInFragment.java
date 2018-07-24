package com.matthewtimmons.upcomingeventsapp.authorization;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.matthewtimmons.upcomingeventsapp.R;
import com.matthewtimmons.upcomingeventsapp.activities.MainActivity;
import com.matthewtimmons.upcomingeventsapp.manager.FirebaseAuthHelper;

public class SignInFragment extends Fragment {
    FirebaseAuth firebaseAuth;
    FirebaseAuth.AuthStateListener authStateListener;
    EditText emailEditTextView;
    EditText passwordEditTextView;
    Button signInButton;
    TextView signUpLinkTextView;
    String email;
    String password;

    public void newInstance() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.fragment_sign_in, container, false);
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        firebaseAuth = FirebaseAuth.getInstance();

        emailEditTextView = view.findViewById(R.id.sign_in_email_edittext);
        passwordEditTextView = view.findViewById(R.id.sign_in_password_edittext);
        signInButton = view.findViewById(R.id.sign_in_button);
        signUpLinkTextView = view.findViewById(R.id.sign_up_link);

        email = emailEditTextView.getText().toString().trim();
        password = passwordEditTextView.getText().toString().trim();

        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() != null) {
                    Intent intent = new Intent(getActivity(), MainActivity.class);
                    startActivity(intent);
                }
            }
        };

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                email = emailEditTextView.getText().toString().trim();
                password = passwordEditTextView.getText().toString().trim();

                FirebaseAuthHelper.startSignIn(getContext(), firebaseAuth, email, password);
            }
        });

        signUpLinkTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment signUpFragment = new SignUpFragment();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.authorization_fragment_container, signUpFragment).commit();
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        firebaseAuth.addAuthStateListener(authStateListener);
    }
}
