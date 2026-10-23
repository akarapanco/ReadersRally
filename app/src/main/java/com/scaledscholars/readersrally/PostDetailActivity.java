package com.scaledscholars.readersrally;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PostDetailActivity extends AppCompatActivity {
    public static final String EXTRA_POST_ID = "postId";
    public static final String EXTRA_BOOK = "book";
    public static final String EXTRA_TITLE = "title";
    public static final String EXTRA_AUTHOR = "authorName";
    public static final String EXTRA_CONTENT = "content";
    public static final String EXTRA_REPLIES_ENABLED = "repliesEnabled";
    private static final String CURRENT_USER_ID = "O4ONRhlDs0BqdozYLG1t";

    private FirebaseFirestore db;
    private String postId;
    private List<Reply> replyList = new ArrayList<>();
    private ReplyAdapter adapter;
    private EditText replyEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_detail);
        findViewById(R.id.backButton).setOnClickListener(v -> finish());
        db = FirebaseFirestore.getInstance();

        postId = getIntent().getStringExtra(EXTRA_POST_ID);
        boolean repliesEnabled = getIntent().getBooleanExtra(EXTRA_REPLIES_ENABLED, false);

        String title = getIntent().getStringExtra(EXTRA_TITLE);
        String book = getIntent().getStringExtra(EXTRA_BOOK);
        String author = getIntent().getStringExtra(EXTRA_AUTHOR);
        ((TextView) findViewById(R.id.titleText)).setText(title != null && !title.isEmpty() ? title : book);
        ((TextView) findViewById(R.id.bookText)).setText(book == null ? "" : "Reading: " + book);
        ((TextView) findViewById(R.id.authorText)).setText(author == null || author.isEmpty() ? "" : "Posted by " + author);
        ((TextView) findViewById(R.id.contentText)).setText(getIntent().getStringExtra(EXTRA_CONTENT));

        RecyclerView recyclerView = findViewById(R.id.repliesRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ReplyAdapter(replyList, this::deleteReply);
        recyclerView.setAdapter(adapter);

        replyEditText = findViewById(R.id.replyEditText);
        Button sendReplyButton = findViewById(R.id.sendReplyButton);

        if (repliesEnabled) {
            sendReplyButton.setOnClickListener(v -> addReply());
        } else {
            findViewById(R.id.replyBar).setVisibility(View.GONE);
            findViewById(R.id.repliesOffText).setVisibility(View.VISIBLE);
        }

        loadReplies();
    }

    private void loadReplies() {
        db.collection("replies")
            .whereEqualTo("postId", postId)
            .get()
            .addOnSuccessListener(querySnapshot -> {
                replyList.clear();
                for (var doc : querySnapshot) {
                    replyList.add(new Reply(
                        doc.getId(),
                        doc.getString("postId"),
                        doc.getString("authorId"),
                        doc.getString("content"),
                        doc.getLong("createdAt") != null ? doc.getLong("createdAt") : 0
                    ));
                }
                Collections.sort(replyList, (a, b) -> Long.compare(a.createdAt, b.createdAt));
                adapter.notifyDataSetChanged();
            })
            .addOnFailureListener(e ->
                Toast.makeText(this, "Failed to load replies", Toast.LENGTH_SHORT).show());
    }

    private void addReply() {
        String content = replyEditText.getText().toString().trim();
        if (content.isEmpty()) {
            Toast.makeText(this, "Write something first", Toast.LENGTH_SHORT).show();
            return;
        }

        Map<String, Object> replyData = new HashMap<>();
        replyData.put("postId", postId);
        replyData.put("authorId", CURRENT_USER_ID);
        replyData.put("content", content);
        replyData.put("createdAt", System.currentTimeMillis());

        db.collection("replies")
            .add(replyData)
            .addOnSuccessListener(ref -> {
                replyEditText.setText("");
                loadReplies();
            })
            .addOnFailureListener(e ->
                Toast.makeText(this, "Failed to add reply", Toast.LENGTH_SHORT).show());
    }

    private void deleteReply(Reply reply) {
        db.collection("replies").document(reply.id)
            .delete()
            .addOnSuccessListener(unused -> {
                replyList.remove(reply);
                adapter.notifyDataSetChanged();
                Toast.makeText(this, "Reply deleted", Toast.LENGTH_SHORT).show();
            })
            .addOnFailureListener(e ->
                Toast.makeText(this, "Failed to delete reply", Toast.LENGTH_SHORT).show());
    }
}
