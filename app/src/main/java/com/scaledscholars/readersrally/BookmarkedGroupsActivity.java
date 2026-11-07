package com.scaledscholars.readersrally;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.ArrayList;
import java.util.List;

public class BookmarkedGroupsActivity extends AppCompatActivity {

    private static final String CURRENT_USER_ID = "O4ONRhlDs0BqdozYLG1t";

    private FirebaseFirestore db;
    private List<Group> bookmarkedGroups = new ArrayList<>();
    private GroupAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookmarked_groups);
        findViewById(R.id.backButton).setOnClickListener(v -> finish());

        db = FirebaseFirestore.getInstance();

        RecyclerView recyclerView = findViewById(R.id.bookmarkedGroupsRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new GroupAdapter(bookmarkedGroups, this::removeBookmark, "Remove Bookmark");
        recyclerView.setAdapter(adapter);

        loadBookmarkedGroups();
    }

    private void loadBookmarkedGroups() {
        db.collection("users").document(CURRENT_USER_ID).get()
            .addOnSuccessListener(userDoc -> {
                List<String> groupIds = (List<String>) userDoc.get("bookmarkedGroupIds");
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
                                bookmarkedGroups.add(group);
                                adapter.notifyDataSetChanged();
                            }
                        });
                }
            });
    }

    private void removeBookmark(Group group) {
        db.collection("users").document(CURRENT_USER_ID)
            .update("bookmarkedGroupIds", FieldValue.arrayRemove(group.getId()))
            .addOnSuccessListener(unused -> {
                bookmarkedGroups.remove(group);
                adapter.notifyDataSetChanged();
            });
    }
}
