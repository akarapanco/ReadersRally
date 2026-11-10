package com.scaledscholars.readersrally;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.slider.RangeSlider;
import com.google.android.material.slider.Slider;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.firestore.WriteBatch;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class MyGroupsActivity extends AppCompatActivity {
    private static final String [] COMMUNICATION_METHODS = {"Online", "Hybrid", "In Person"};
    private static final String [] GENRE_SUGGESTIONS = {"Mystery", "Fantasy", "Science Fiction",
        "Romance", "Thriller", "Historical Fiction", "Non-Fiction", "Biography", "Horror",
        "Young Adult", "Classics"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_groups);
        Button enrolledButton = findViewById(R.id.enrolledGroupsButton);
        Button bookmarkedButton = findViewById(R.id.bookmarkedGroupsButton);
        Button joinRequestsButton = findViewById(R.id.joinRequestsButton);
        Button createGroupButton = findViewById(R.id.createGroupButton);
        enrolledButton.setOnClickListener(v -> {
            startActivity(new Intent(this, EnrolledGroupsActivity.class));
        });
        bookmarkedButton.setOnClickListener(v -> {
            startActivity(new Intent(this, BookmarkedGroupsActivity.class));
        });
        joinRequestsButton.setOnClickListener(v -> {
            startActivity(new Intent(this, JoinRequestsActivity.class));
        });
        createGroupButton.setOnClickListener(v ->
            showCreateGroupDialog());
    }

    private void showCreateGroupDialog() {
        View content = getLayoutInflater().inflate(R.layout.dialog_create_group, null);
        AlertDialog dialog = new MaterialAlertDialogBuilder(this).setView(content).create();

        // Required fields
        TextInputLayout nameLayout = content.findViewById(R.id.groupNameLayout);
        TextInputEditText nameInput = content.findViewById(R.id.groupNameInput);
        TextView maxSizeLabel = content.findViewById(R.id.maxSizeLabel);
        Slider maxSizeSlider = content.findViewById(R.id.maxSizeSlider);
        TextInputLayout communicationLayout = content.findViewById(R.id.communicationLayout);
        MaterialAutoCompleteTextView communicationInput = content.findViewById(R.id.communicationInput);

        // Optional fields
        TextInputEditText bookTitleInput = content.findViewById(R.id.bookTitleInput);
        MaterialAutoCompleteTextView genreInput = content.findViewById(R.id.genreInput);
        CheckBox lengthCheckBox = content.findViewById(R.id.lengthCheckBox);
        TextView lengthLabel = content.findViewById(R.id.lengthLabel);
        RangeSlider lengthSlider = content.findViewById(R.id.lengthSlider);
        TextInputEditText descriptionInput = content.findViewById(R.id.descriptionInput);

        Button cancelButton = content.findViewById(R.id.cancelButton);
        Button confirmButton = content.findViewById(R.id.confirmButton);

        // Dropdown adapters
        communicationInput.setAdapter(new ArrayAdapter<>(
                this, android.R.layout.simple_dropdown_item_1line, COMMUNICATION_METHODS));
        genreInput.setAdapter(new ArrayAdapter<>(
                this, android.R.layout.simple_dropdown_item_1line, GENRE_SUGGESTIONS));

        // Max size slider label
        maxSizeSlider.addOnChangeListener((slider, value, fromUser) ->
                maxSizeLabel.setText("Maximum Size *: " + (int) value + " members"));

        // Book length range slider (only active when the checkbox is ticked)
        lengthSlider.setValues(150f, 500f);
        lengthSlider.addOnChangeListener((slider, value, fromUser) -> {
            List<Float> vals = slider.getValues();
            lengthLabel.setText(vals.get(0).intValue() + " - " + vals.get(1).intValue() + " pages");
        });
        lengthCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            lengthSlider.setEnabled(isChecked);
            if (isChecked) {
                List<Float> vals = lengthSlider.getValues();
                lengthLabel.setText(vals.get(0).intValue() + " - " + vals.get(1).intValue() + " pages");
            } else {
                lengthLabel.setText("Any length");
            }
        });

        // Clear errors as soon as the user fixes them
        nameInput.setOnFocusChangeListener((v, hasFocus) -> nameLayout.setError(null));
        communicationInput.setOnItemClickListener((parent, view, position, id) ->
                communicationLayout.setError(null));

        cancelButton.setOnClickListener(b -> dialog.dismiss());

        confirmButton.setOnClickListener(b -> {
            String name = textOf(nameInput);
            String communication = textOf(communicationInput);

            // Validate required fields
            boolean valid = true;
            if (name.isEmpty()) {
                nameLayout.setError("Group name is required");
                valid = false;
            } else {
                nameLayout.setError(null);
            }
            if (communication.isEmpty()) {
                communicationLayout.setError("Please choose a communication method");
                valid = false;
            } else {
                communicationLayout.setError(null);
            }
            if (!valid) return;

            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            if (user == null) {
                Toast.makeText(this, "You must be logged in to create a group.", Toast.LENGTH_SHORT).show();
                return;
            }

            // Build the Firestore document
            Map<String, Object> group = new HashMap<>();
            group.put("name", name);
            group.put("groupSize", (int) maxSizeSlider.getValue());
            group.put("communicationMethod", communication.toLowerCase(Locale.US));
            group.put("currentMembers", 1);
            //USER - might replace with username if that makes more sense for checks
            group.put("createdBy", user.getUid());

            // Optional fields: always present, null when left blank
            String bookTitle = textOf(bookTitleInput);
            group.put("bookTitle", bookTitle.isEmpty() ? null : bookTitle);

            String genre = textOf(genreInput);
            group.put("genre", genre.isEmpty() ? null : genre);

            String description = textOf(descriptionInput);
            group.put("description", description.isEmpty() ? null : description);

            if (lengthCheckBox.isChecked()) {
                List<Float> range = lengthSlider.getValues();
                group.put("minBookLength", range.get(0).intValue());
                group.put("maxBookLength", range.get(1).intValue());
            } else {
                group.put("minBookLength", null);
                group.put("maxBookLength", null);
            }
            confirmButton.setEnabled(false); // prevent double-submits
            saveGroup(group, user.getUid(), dialog, confirmButton);
        });

        dialog.show();
    }

    private void saveGroup(Map<String, Object> group, String uid, AlertDialog dialog, Button confirmButton) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        DocumentReference groupRef = db.collection("groups").document(); // generates the ID up front
        DocumentReference userRef = db.collection("users").document(uid);

        Map<String, Object> userUpdate = new HashMap<>();
        userUpdate.put("enrolledGroupIds", FieldValue.arrayUnion(groupRef.getId()));

        WriteBatch batch = db.batch();
        batch.set(groupRef, group);
        batch.set(userRef, userUpdate, SetOptions.merge()); // merge: keeps the rest of the user doc, creates the list if missing

        batch.commit()
                .addOnSuccessListener(unused -> {
                    Toast.makeText(this, "Group created!", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                })
                .addOnFailureListener(e -> {
                    confirmButton.setEnabled(true);
                    Toast.makeText(this, "Could not create group: " + e.getMessage(),
                            Toast.LENGTH_LONG).show();
                });
    }

    private String textOf(TextView view) {
        return view.getText() == null ? "" : view.getText().toString().trim();
    }
}
