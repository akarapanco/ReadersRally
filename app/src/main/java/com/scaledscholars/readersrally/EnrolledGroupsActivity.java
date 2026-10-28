package com.scaledscholars.readersrally;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.ArrayList;
import java.util.List;

public class EnrolledGroupsActivity extends AppCompatActivity {

    private static final String CURRENT_USER_ID = "O4ONRhlDs0BqdozYLG1t";

    private FirebaseFirestore db;
    private List<Group> enrolledGroups = new ArrayList<>();
    private GroupAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enrolled_groups);

        db = FirebaseFirestore.getInstance();

        RecyclerView recyclerView = findViewById(R.id.enrolledGroupsRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new GroupAdapter(enrolledGroups, this::leaveGroup, "Leave Group");
        recyclerView.setAdapter(adapter);

        loadEnrolledGroups();
    }

    private void loadEnrolledGroups() {
        db.collection("users").document(CURRENT_USER_ID).get()
            .addOnSuccessListener(userDoc -> {
                List<String> groupIds = (List<String>) userDoc.get("enrolledGroupIds");
                if (groupIds == null || groupIds.isEmpty()) return;

                for (String groupId : groupIds) {
                    db.collection("groups").document(groupId).get()
                        .addOnSuccessListener(groupDoc -> {
                            if (groupDoc.exists()) {
                                Group group = new Group(
                                    groupDoc.getId(),
                                    groupDoc.getString("name"),
                                    groupDoc.getString("bookTitle")
                                );
                                enrolledGroups.add(group);
                                adapter.notifyDataSetChanged();
                            }
                        });
                }
            });
    }

    private void leaveGroup(Group group) {
        db.collection("users").document(CURRENT_USER_ID)
            .update("enrolledGroupIds", FieldValue.arrayRemove(group.getId()))
            .addOnSuccessListener(unused -> {
                enrolledGroups.remove(group);
                adapter.notifyDataSetChanged();
            });
    }
}
