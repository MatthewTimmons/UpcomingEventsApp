package com.matthewtimmons.upcomingeventsapp.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.matthewtimmons.upcomingeventsapp.R;
import com.matthewtimmons.upcomingeventsapp.manager.Firestore;

public class SettingsActivity extends AppCompatActivity {
    TextView questionConfirmation;
    Button deleteAccountButton, confirmButton, cancelButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        deleteAccountButton = findViewById(R.id.delete_account_button);

        deleteAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                android.support.v7.app.AlertDialog.Builder dialogBuilder = new android.support.v7.app.AlertDialog.Builder(SettingsActivity.this);
                View dialogView = getLayoutInflater().inflate(R.layout.dialog_confirm, null);

                confirmButton = dialogView.findViewById(R.id.confirm_button);
                cancelButton = dialogView.findViewById(R.id.cancel_button);
                questionConfirmation = dialogView.findViewById(R.id.question_confirmation);
                questionConfirmation.setText("Are you sure you want to delete your account? This will permanently delete your account and all of your custom events.");

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
                        Toast.makeText(SettingsActivity.this, "Account deletion soon", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}
