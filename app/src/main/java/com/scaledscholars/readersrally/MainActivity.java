package com.scaledscholars.readersrally;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

/* MAIN ACTIVITY = STARTUP PAGE
    Displays app title text and "Log in" and "Sign up" buttons
 */

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        /* MYGROUPS - activity for button - move me to new file!
        Button myGroupsButton = findViewById(R.id.myGroupsButton);
        myGroupsButton.setOnClickListener(v -> {
            startActivity(new Intent(this, MyGroupsActivity.class));
        });*/

        // Log In Button - 30 SEPT - NAVIGATES STRAIGHT TO HOME PAGE
        Button loginButton = findViewById(R.id.loginButton);
        loginButton.setOnClickListener(v -> {
                startActivity(new Intent(this, HomePage.class));
        });
    }
}
