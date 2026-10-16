package com.scaledscholars.readersrally;

import android.os.Bundle;
import android.text.InputFilter;
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
    private static final String CURRENT_USER_ID = "O4ONRhlDs0BqdozYLG1t";

    private FirebaseFirestore db;
    private String groupId;
    private EditText bookEditText;
    private EditText contentEditText;
    private Switch repliesSwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_post);
        db = FirebaseFirestore.getInstance();
        groupId = getIntent().getStringExtra(EXTRA_GROUP_ID);

        bookEditText = findViewById(R.id.bookEditText);
        contentEditText = findViewById(R.id.contentEditText);
        repliesSwitch = findViewById(R.id.repliesSwitch);
        Button postButton = findViewById(R.id.postButton);

        contentEditText.setFilters(new InputFilter[]{ new InputFilter.LengthFilter(CHARACTER_LIMIT) });

        postButton.setOnClickListener(v -> createPost());
    }

    private void createPost() {
        String book = bookEditText.getText().toString().trim();
        String content = contentEditText.getText().toString().trim();

        if (book.isEmpty() || content.isEmpty()) {
            Toast.makeText(this, "Please fill out the book and post content", Toast.LENGTH_SHORT).show();
            return;
        }

        String userId = CURRENT_USER_ID;
        boolean repliesEnabled = repliesSwitch.isChecked();

        Map<String, Object> postData = new HashMap<>();
        postData.put("groupId", groupId);
        postData.put("authorId", userId);
        postData.put("book", book);
        postData.put("content", content);
        postData.put("repliesEnabled", repliesEnabled);
        postData.put("createdAt", System.currentTimeMillis());

        db.collection("posts")
            .add(postData)
            .addOnSuccessListener(docRef -> {
                Toast.makeText(this, "Post created", Toast.LENGTH_SHORT).show();
                finish();
            })
            .addOnFailureListener(e ->
                Toast.makeText(this, "Failed to create post", Toast.LENGTH_SHORT).show());
    }
}
