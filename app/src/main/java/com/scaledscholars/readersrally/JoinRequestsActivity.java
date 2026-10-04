package com.scaledscholars.readersrally;

import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.ArrayList;
import java.util.List;

public class JoinRequestsActivity extends AppCompatActivity {
    private FirebaseFirestore db;
    private List<JoinRequest> requestList = new ArrayList<>();
    private JoinRequestAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_requests);
        db = FirebaseFirestore.getInstance();
        RecyclerView recyclerView = findViewById(R.id.joinRequestsRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new JoinRequestAdapter(requestList, new JoinRequestAdapter.OnActionListener() {
            @Override
            public void onApprove(JoinRequest request) {
                approveRequest(request);
            }
            @Override
            public void onDeny(JoinRequest request) {
                denyRequest(request);
            }
        });
        recyclerView.setAdapter(adapter);
        loadRequests();
    }

    private void loadRequests() {
        db.collection("joinRequests")
            .whereEqualTo("status", "pending")
            .get()
            .addOnSuccessListener(querySnapshot -> {
                for (var doc : querySnapshot) {
                    JoinRequest request = new JoinRequest(
                        doc.getId(),
                        doc.getString("groupId"),
                        doc.getString("userId"),
                        doc.getString("message"),
                        doc.getString("status")
                    );
                    attachDetailsAndAdd(request);
                }
            });
    }

    private void attachDetailsAndAdd(JoinRequest request) {
        db.collection("groups").document(request.groupId).get()
            .addOnSuccessListener(groupDoc -> {
                request.groupName = groupDoc.getString("name");
                db.collection("users").document(request.userId).get()
                    .addOnSuccessListener(userDoc -> {
                        request.userEmail = userDoc.getString("email");
                        requestList.add(request);
                        adapter.notifyDataSetChanged();
                    });
            });
    }

    private void approveRequest(JoinRequest request) {
        db.collection("joinRequests").document(request.id)
            .update("status", "approved")
            .addOnSuccessListener(unused -> {
                db.collection("users").document(request.userId)
                    .update("enrolledGroupIds", com.google.firebase.firestore.FieldValue.arrayUnion(request.groupId))
                    .addOnSuccessListener(unused2 -> {
                        requestList.remove(request);
                        adapter.notifyDataSetChanged();
                        Toast.makeText(this, "Request approved", Toast.LENGTH_SHORT).show();
                    });
            });
    }

    private void denyRequest(JoinRequest request) {
        db.collection("joinRequests").document(request.id)
            .update("status", "denied")
            .addOnSuccessListener(unused -> {
                requestList.remove(request);
                adapter.notifyDataSetChanged();
                Toast.makeText(this, "Request denied", Toast.LENGTH_SHORT).show();
            });
    }
}
