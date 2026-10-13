package com.scaledscholars.readersrally;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

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
    }
}
