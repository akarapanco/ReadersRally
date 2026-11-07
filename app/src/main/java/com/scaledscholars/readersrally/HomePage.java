package com.scaledscholars.readersrally;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.auth.FirebaseAuth;

public class HomePage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home_page);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // MYGROUPS - activity for button - move me to new file!
        Button myGroupsButton = findViewById(R.id.myGroupsButton);
        myGroupsButton.setOnClickListener(v -> {
            startActivity(new Intent(this, MyGroupsActivity.class));
        });

        // BROWSEGROUPS - activity for button
        Button browseGroupsButton = findViewById(R.id.browseGroupsButton);

        browseGroupsButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, BrowseGroupsActivity.class);
            startActivity(intent);
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