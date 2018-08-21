package com.matthewtimmons.upcomingeventsapp.authorization;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.matthewtimmons.upcomingeventsapp.R;
import com.matthewtimmons.upcomingeventsapp.activities.MainActivity;
import com.matthewtimmons.upcomingeventsapp.models.UserManager;

public class SignInFragment extends Fragment {
    FirebaseAuth.AuthStateListener authStateListener;
    FirebaseAuth firebaseAuth;
    FirebaseUser currentUser;
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

        getActivity().findViewById(R.id.authorization_activity).setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        emailEditTextView = view.findViewById(R.id.sign_in_email_edittext);
        passwordEditTextView = view.findViewById(R.id.sign_in_password_edittext);
        signInButton = view.findViewById(R.id.sign_in_button);
        signUpLinkTextView = view.findViewById(R.id.sign_up_link);

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                email = emailEditTextView.getText().toString().trim();
                password = passwordEditTextView.getText().toString().trim();

                loginUser(email, password);
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

    public void loginUser(final String email, final String password) {
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(getContext(), "Both fields must be filled", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(getContext(), "Both fields must be filled", Toast.LENGTH_SHORT).show();
            return;
        }
        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                                UserManager.getInstance().setCurrentUser();
                                getActivity().finish();
                                startActivity(new Intent(getContext(), MainActivity.class));
                            } else {
                                Toast.makeText(getContext(), "Couldn't login", Toast.LENGTH_SHORT).show();
                            }
                        }
                });
    }
}
