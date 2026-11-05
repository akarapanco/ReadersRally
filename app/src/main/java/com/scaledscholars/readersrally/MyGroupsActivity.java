package com.scaledscholars.readersrally;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.auth.FirebaseAuth;

public class MyGroupsActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_groups);
        Button enrolledButton = findViewById(R.id.enrolledGroupsButton);
        Button bookmarkedButton = findViewById(R.id.bookmarkedGroupsButton);
        Button joinRequestsButton = findViewById(R.id.joinRequestsButton);
        enrolledButton.setOnClickListener(v -> {
            startActivity(new Intent(this, EnrolledGroupsActivity.class));
        });
        bookmarkedButton.setOnClickListener(v -> {
            startActivity(new Intent(this, BookmarkedGroupsActivity.class));
        });
        joinRequestsButton.setOnClickListener(v -> {
            startActivity(new Intent(this, JoinRequestsActivity.class));
        });

        ImageButton settingsButton = findViewById(R.id.settingsButton);
        settingsButton.setOnClickListener(v -> {
            View content = getLayoutInflater().inflate(R.layout.dialog_settings, null);
            AlertDialog dialog = new MaterialAlertDialogBuilder(this).setView(content).create();
            content.findViewById(R.id.closeButton).setOnClickListener(b -> dialog.dismiss());

            content.findViewById(R.id.signOutButton).setOnClickListener(b -> {
                dialog.dismiss();
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            });
            dialog.show();
        });
    }
}
