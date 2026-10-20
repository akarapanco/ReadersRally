package com.scaledscholars.readersrally;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import java.util.ArrayList;
import java.util.List;

public class DiscussionForumActivity extends AppCompatActivity {
    private FirebaseFirestore db;
    private List<Post> postList = new ArrayList<>();
    private PostAdapter adapter;
    public static final String EXTRA_GROUP_ID = "groupId";
    private String groupId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discussion_forum);
        db = FirebaseFirestore.getInstance();
        groupId = getIntent().getStringExtra(EXTRA_GROUP_ID);

        RecyclerView recyclerView = findViewById(R.id.postsRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new PostAdapter(postList, this::deletePost);
        recyclerView.setAdapter(adapter);

        Button createPostButton = findViewById(R.id.createPostButton);
        createPostButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, CreatePostActivity.class);
            intent.putExtra(CreatePostActivity.EXTRA_GROUP_ID, groupId);
            startActivity(intent);
        });

        loadPosts();
    }

    @Override
    protected void onResume() {
        super.onResume();
        postList.clear();
        loadPosts();
    }

    private void loadPosts() {
        db.collection("posts")
            .whereEqualTo("groupId", groupId)
            .orderBy("createdAt", Query.Direction.DESCENDING)
            .get()
            .addOnSuccessListener(querySnapshot -> {
                postList.clear();
                for (var doc : querySnapshot) {
                    Post post = new Post(
                        doc.getId(),
                        doc.getString("groupId"),
                        doc.getString("authorId"),
                        doc.getString("genre"),
                        doc.getString("book"),
                        doc.getString("content"),
                        Boolean.TRUE.equals(doc.getBoolean("repliesEnabled")),
                        doc.getLong("createdAt") != null ? doc.getLong("createdAt") : 0
                    );
                    postList.add(post);
                }
                adapter.notifyDataSetChanged();
            })
            .addOnFailureListener(e ->
                Toast.makeText(this, "Failed to load posts", Toast.LENGTH_SHORT).show());
    }

    private void deletePost(Post post) {
        db.collection("posts").document(post.id)
            .delete()
            .addOnSuccessListener(unused -> {
                postList.remove(post);
                adapter.notifyDataSetChanged();
                Toast.makeText(this, "Post deleted", Toast.LENGTH_SHORT).show();
            })
            .addOnFailureListener(e ->
                Toast.makeText(this, "Failed to delete post", Toast.LENGTH_SHORT).show());
    }
}
