package com.matthewtimmons.upcomingeventsapp.authorization;

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

public class SignUpFragment extends Fragment {
    FirebaseAuth firebaseAuth;
    String email;
    String password;
    EditText emailEditTextView;
    EditText passwordEditTextView;
    Button signUpButton;
    TextView signInLinkTextView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.fragment_sign_up, container, false);
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        firebaseAuth = firebaseAuth.getInstance();
        emailEditTextView = view.findViewById(R.id.sign_up_email_edittext);
        passwordEditTextView = view.findViewById(R.id.sign_up_password_edittext);
        signUpButton = view.findViewById(R.id.sign_up_button);
        signInLinkTextView = view.findViewById(R.id.sign_in_link);

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                email = emailEditTextView.getText().toString().trim();
                password = passwordEditTextView.getText().toString().trim();

                firebaseAuth.createUserWithEmailAndPassword(email, password);

            }
        });
        signInLinkTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment signInFragment = new SignInFragment();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.authorization_fragment_container, signInFragment).commit();
            }
        });
    }
}
