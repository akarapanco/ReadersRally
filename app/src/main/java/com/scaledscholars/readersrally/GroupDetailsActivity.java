package com.scaledscholars.readersrally;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import android.content.Intent;
import java.util.List;

import android.view.View;
import android.widget.ImageButton;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FieldValue;
import java.util.HashMap;
import java.util.Map;

public class GroupDetailsActivity extends AppCompatActivity
{
    private FirebaseFirestore db;
    private String groupId;
    private TextView groupName;
    private TextView groupBio;
    private TextView groupGenre;
    private TextView groupBook;
    private TextView groupBookLength;
    private TextView groupCommunication;
    private TextView groupSize;

    private Button requestJoinButton;



    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_group_details);

        findViewById(R.id.backButton).setOnClickListener(v -> finish());

        db = FirebaseFirestore.getInstance();

        //Get selected group ID
        groupId = getIntent().getStringExtra("groupId");

        groupName = findViewById(R.id.groupDetailsName);
        groupBio = findViewById(R.id.groupDetailsBio);
        groupGenre = findViewById(R.id.groupDetailsGenre);
        groupBook = findViewById(R.id.groupDetailsBook);
        groupBookLength = findViewById(R.id.groupDetailsBookLength);
        groupCommunication = findViewById(R.id.groupDetailsCommunication);
        groupSize = findViewById(R.id.groupDetailsSize);

        requestJoinButton = findViewById(R.id.requestJoinButton);
        ImageButton settingsButton = findViewById(R.id.settingsButton);
        settingsButton.setVisibility(Button.GONE);

        if (groupId == null
         || groupId.isEmpty())
        {
            Toast.makeText(this, "Group not found.", Toast.LENGTH_SHORT).show();
            finish();

            return;
        }

        loadGroupDetails();

        requestJoinButton.setOnClickListener(v -> sendJoinRequest());


        checkGroupMembership(settingsButton);

        settingsButton.setOnClickListener(v ->
        {
            Intent intent = new Intent(this, GroupSettingsActivity.class);
            intent.putExtra("groupId", groupId);
            startActivity(intent);
        });
    }

    private void loadGroupDetails()
    {
        //Use with firestore
        db.collection("groups").document(groupId).get().addOnSuccessListener(documentSnapshot -> {displayGroupDetails(documentSnapshot);
                })
                .addOnFailureListener(e ->
                {
                    Toast.makeText(this, "Unable to load group details", Toast.LENGTH_SHORT
                    ).show();
                });
    }

    private void displayGroupDetails(DocumentSnapshot doc)
    {

        if (!doc.exists())
        {
            Toast.makeText(this, "Group does not exist.", Toast.LENGTH_SHORT).show();

            finish();
            return;
        }

        String name = doc.getString("name");
        String bio = doc.getString("description");
        String genre = doc.getString("genre");
        String book = doc.getString("bookTitle");
        String communication = doc.getString("communicationMethod");
        String creator = doc.getString("createdBy");
        if (creator != null && creator.equals(CurrentUser.id())) { requestJoinButton.setVisibility(View.GONE); }

        Long bookLength = doc.getLong("bookLength");
        Long members = doc.getLong("currentMembers");
        Long maxMembers = doc.getLong("groupSize");
        groupName.setText(valueOrDefault(name));
        groupBio.setText(valueOrDefault(bio));
        groupGenre.setText("Genre: " + valueOrDefault(genre));
        groupBook.setText("Currently Reading: " + valueOrDefault(book));

        if (bookLength != null)     { groupBookLength.setText("Book Length: " + bookLength + " pages"); }
        else    { groupBookLength.setText("Book Length: Not specified"); }

        groupCommunication.setText("Communication Method: " + valueOrDefault(communication));

        if (members != null && maxMembers != null)  { groupSize.setText("Members: " + members + "/" + maxMembers); }
        else    { groupSize.setText("Members: Not specified"); }

        com.google.firebase.firestore.FirebaseFirestore.getInstance().collection("users")
                .whereArrayContains("enrolledGroupIds", doc.getId()).get()
                .addOnSuccessListener(memberSnapshot ->
                { if (maxMembers != null) { groupSize.setText("Members: " + memberSnapshot.size() + "/" + maxMembers); } });
    }

    private void checkGroupMembership(ImageButton settingsButton)
    {
        if (FirebaseAuth.getInstance().getCurrentUser() == null)    { return; }
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        db.collection("users").document(userId).get().addOnSuccessListener(doc ->
                {
                    List<String> enrolledGroupIds = (List<String>) doc.get("enrolledGroupIds");

                    if (enrolledGroupIds != null
                     && enrolledGroupIds.contains(groupId))     { settingsButton.setVisibility(Button.VISIBLE); requestJoinButton.setVisibility(View.GONE); }
                });
    }

    private String valueOrDefault(String value)
    {
        if (value == null
         || value.trim().isEmpty())     { return "Not specified"; }

        return value;
    }


    private void sendJoinRequest()
    {
        if (FirebaseAuth.getInstance().getCurrentUser() == null)
        {
            Toast.makeText(this, "You are not signed in. Please sign in to place join requests.", Toast.LENGTH_SHORT).show();
            return;
        }

        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();


        if (groupId == null
         || groupId.isEmpty())
        {
            Toast.makeText(this, "Unknown or invalid group selected.", Toast.LENGTH_SHORT).show();
            return;
        }

        String requestId = groupId + "_" + userId;

        db.collection("joinRequests").document(requestId).get().addOnSuccessListener(document ->
                {
                    if (document.exists())
                    {
                        Toast.makeText(this, "Join request already sent.", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    Map<String, Object> request = new HashMap<>();
                    request.put("groupId", groupId);
                    request.put("userId", userId);
                    request.put("status", "pending");
                    request.put("message", "I'd like to join this group!");
                    request.put("createdAt", FieldValue.serverTimestamp());

                    db.collection("joinRequests").document(requestId).set(request).addOnSuccessListener(unused ->
                            {
                                Toast.makeText(this, "Join request sent!", Toast.LENGTH_SHORT).show();

                                requestJoinButton.setEnabled(false);
                                requestJoinButton.setText("Request Pending");
                            })
                            .addOnFailureListener(e ->
                            {
                                Toast.makeText(this, "Failed to send request: " + e.getMessage(), Toast.LENGTH_LONG).show();
                            });
                })
                .addOnFailureListener(e ->
                { Toast.makeText(this, "Failed to check existing requests: " + e.getMessage(), Toast.LENGTH_LONG).show(); });
    }


}