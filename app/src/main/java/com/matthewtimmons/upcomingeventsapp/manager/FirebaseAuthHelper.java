package com.matthewtimmons.upcomingeventsapp.manager;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.matthewtimmons.upcomingeventsapp.authorization.AuthorizeUserActivity;

public class FirebaseAuthHelper {

    public static void createAccountByEmail(String email, String password) {

    }
    public static void startSignIn(Context context, final FirebaseAuth firebaseAuth, String email, String password) {
        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            Toast.makeText(context, "Email address and password cannot be empty", Toast.LENGTH_SHORT).show();
        } else {
            firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (!task.isSuccessful()) {

                    }
                }
            });
        }
    }
}
