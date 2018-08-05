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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.matthewtimmons.upcomingeventsapp.CircleTransform;
import com.matthewtimmons.upcomingeventsapp.R;
import com.matthewtimmons.upcomingeventsapp.activities.MainActivity;
import com.matthewtimmons.upcomingeventsapp.manager.Firestore;
import com.matthewtimmons.upcomingeventsapp.models.User;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

import static com.firebase.ui.auth.AuthUI.getApplicationContext;

public class SignUpFragment extends Fragment {
    FirebaseAuth firebaseAuth;
    String email;
    String password;
    String displayName;
    EditText emailEditTextView;
    EditText passwordEditTextView;
    EditText displayNameEditTextView;
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
        firebaseAuth = FirebaseAuth.getInstance();
        getActivity().findViewById(R.id.authorization_activity).setBackgroundColor(getResources().getColor(R.color.colorAccent));
        displayNameEditTextView = view.findViewById(R.id.sign_up_displayname_edittext);
        emailEditTextView = view.findViewById(R.id.sign_up_email_edittext);
        passwordEditTextView = view.findViewById(R.id.sign_up_password_edittext);
        signUpButton = view.findViewById(R.id.sign_up_button);
        signInLinkTextView = view.findViewById(R.id.sign_in_link);


        signUpButton.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                displayName = displayNameEditTextView.getText().toString().trim();
                                                email = emailEditTextView.getText().toString().trim();
                                                password = passwordEditTextView.getText().toString().trim();

                                                registerUserWithEmail(view, email, password, displayName);
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

    public void registerUserWithEmail(final View view, String email, String password, final String displayName) {
        // Validate input fields
        if (TextUtils.isEmpty(email)){
            Toast.makeText(view.getContext(), "Email address is Empty", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(password)){
            Toast.makeText(view.getContext(), "Password is Empty", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(displayName)){
            Toast.makeText(view.getContext(), "Display name is Empty", Toast.LENGTH_SHORT).show();
            return;
        }

        // Register the user and begin the Main Activity if successful
        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                try {
                    if (task.isSuccessful()) {
                        setUpUserInDatabase();
                        getActivity().finish();
                        Intent intent = new Intent(getContext(), MainActivity.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(view.getContext(), "Couldn't register, try again", Toast.LENGTH_SHORT).show();
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
    }

    public void setUpUserInDatabase() {
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        if (currentUser != null) {
            // First time signing up
            EditText displayNameTextView = getView().findViewById(R.id.sign_up_displayname_edittext);
            String displayName = displayNameTextView.getText().toString().trim();
            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder().setDisplayName(displayName).build();
            currentUser.updateProfile(profileUpdates);

            String currentUserId = currentUser.getUid();
            Map<String, Object> blankUser = User.getBlankUserValues();
            blankUser.put("displayName", displayName);
//                currentUser.put("metadata", currentUser);
            CollectionReference currentUserDocRef = Firestore.collection("users");
            CollectionReference currentUserAuthDocRef = Firestore.collection("usersAuth");
            currentUserDocRef.document(currentUserId).set(blankUser);
            currentUserAuthDocRef.document(currentUserId).set(currentUser);
            Toast.makeText(getContext(), "Account set up successfully", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getContext(), "Something went wrong, creating your account", Toast.LENGTH_SHORT).show();
        }
    }
}
