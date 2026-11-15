
package com.scaledscholars.readersrally;
import android.os.Bundle;
import android.widget.Toast;
import android.widget.TextView;
import com.google.firebase.firestore.FirebaseFirestore;

import androidx.appcompat.app.AppCompatActivity;

public class GroupRosterActivity extends AppCompatActivity
{
    private String groupId;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_group_roster);

        groupId = getIntent().getStringExtra("groupId");

        if (groupId == null
         || groupId.isEmpty())
        {
            Toast.makeText(this, "Group not found", Toast.LENGTH_SHORT).show();
            finish();
        }

        findViewById(R.id.backButton).setOnClickListener(v -> finish());

        if (groupId == null || groupId.isEmpty()) { return; }
        TextView rosterText = findViewById(R.id.rosterText);
        FirebaseFirestore.getInstance().collection("users")
            .whereArrayContains("enrolledGroupIds", groupId).get()
            .addOnSuccessListener(snapshot -> {
                StringBuilder names = new StringBuilder();
                for (com.google.firebase.firestore.DocumentSnapshot doc : snapshot.getDocuments()) {
                    String name = doc.getString("username");
                    if (name == null || name.isEmpty()) { name = doc.getString("email"); }
                    if (name != null) { names.append(name).append("\n"); }
                }
                rosterText.setText(names.length() == 0 ? "No members yet" : names.toString().trim());
            })
            .addOnFailureListener(e -> Toast.makeText(this, "Could not load members: " + e.getMessage(), Toast.LENGTH_LONG).show());
    }


}
