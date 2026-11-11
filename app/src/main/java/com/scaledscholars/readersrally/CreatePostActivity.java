package com.scaledscholars.readersrally;

import android.os.Bundle;
import android.text.InputFilter;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.HashMap;
import java.util.Map;

public class CreatePostActivity extends AppCompatActivity {
    public static final String EXTRA_GROUP_ID = "groupId";
    private static final int CHARACTER_LIMIT = 500;
    private final String CURRENT_USER_ID = CurrentUser.id();

    private FirebaseFirestore db;
    private String groupId;
    private EditText titleEditText;
    private EditText bookEditText;
    private EditText nameEditText;
    private EditText contentEditText;
    private Switch repliesSwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_post);
        findViewById(R.id.backButton).setOnClickListener(v -> finish());
        db = FirebaseFirestore.getInstance();
        groupId = getIntent().getStringExtra(EXTRA_GROUP_ID);

        titleEditText = findViewById(R.id.titleEditText);
        bookEditText = findViewById(R.id.bookEditText);
        nameEditText = findViewById(R.id.nameEditText);
        nameEditText.setVisibility(View.GONE);
        contentEditText = findViewById(R.id.contentEditText);
        repliesSwitch = findViewById(R.id.repliesSwitch);
        contentEditText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(CHARACTER_LIMIT)});

        Button postButton = findViewById(R.id.postButton);
        postButton.setOnClickListener(v -> createPost());
    }

    private void createPost() {
        String title = titleEditText.getText().toString().trim();
        String book = bookEditText.getText().toString().trim();
        String content = contentEditText.getText().toString().trim();

        if (title.isEmpty() || book.isEmpty() || content.isEmpty()) {
            Toast.makeText(this, "Please fill in every field", Toast.LENGTH_SHORT).show();
            return;
        }

        db.collection("users").document(CURRENT_USER_ID).get()
            .addOnSuccessListener(userDoc -> {
                String name = userDoc.getString("username");

                Map<String, Object> postData = new HashMap<>();
                postData.put("groupId", groupId);
                postData.put("authorId", CURRENT_USER_ID);
                postData.put("authorName", name != null ? name : "Unknown");
                postData.put("title", title);
                postData.put("book", book);
                postData.put("content", content);
                postData.put("repliesEnabled", repliesSwitch.isChecked());
                postData.put("createdAt", System.currentTimeMillis());

                db.collection("posts")
                    .add(postData)
                    .addOnSuccessListener(ref -> {
                        Toast.makeText(this, "Post created", Toast.LENGTH_SHORT).show();
                        finish();
                    })
                    .addOnFailureListener(e ->
                        Toast.makeText(this, "Failed to create post", Toast.LENGTH_SHORT).show());
            })
            .addOnFailureListener(e ->
                Toast.makeText(this, "Failed to create post", Toast.LENGTH_SHORT).show());
    }
}
