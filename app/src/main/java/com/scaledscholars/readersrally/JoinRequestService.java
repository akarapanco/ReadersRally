package com.scaledscholars.readersrally;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/** Firestore operations for prerequisites and student request submission. */
public class JoinRequestService {
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();

    public Task<QuerySnapshot> loadPrerequisites(String groupId) {
        return db.collection("groups").document(groupId)
                .collection("joinPrerequisites").get();
    }

    /**
     * Student-facing submission. The caller must first show all prerequisites
     * and open any external forms; confirmation here is self-reported.
     * The application and Firestore rules must authenticate the user.
     */
    public Task<Void> submitRequest(String groupId, String userId, String message,
                                    List<JoinPrerequisite> prerequisites,
                                    List<String> completedPrerequisiteIds) {
        if (groupId == null || groupId.isEmpty() || userId == null || userId.isEmpty()) {
            throw new IllegalArgumentException("Group and user are required");
        }
        Set<String> completed = new HashSet<>();
        if (completedPrerequisiteIds != null) completed.addAll(completedPrerequisiteIds);
        if (prerequisites != null) {
            for (JoinPrerequisite prerequisite : prerequisites) {
                if (prerequisite.required && !completed.contains(prerequisite.id)) {
                    throw new IllegalArgumentException("Required prerequisite incomplete: " + prerequisite.title);
                }
            }
        }
        // Stable document ID prevents two simultaneous pending requests for one user/group.
        String requestId = groupId + "_" + userId;
        DocumentReference ref = db.collection("joinRequests").document(requestId);
        Map<String, Object> data = new HashMap<>();
        data.put("groupId", groupId);
        data.put("userId", userId);
        data.put("message", message == null ? "" : message);
        data.put("status", "pending");
        data.put("completedPrerequisiteIds", new ArrayList<>(completed));
        data.put("createdAt", com.google.firebase.firestore.FieldValue.serverTimestamp());
        return db.runTransaction(transaction -> {
            DocumentSnapshot existing = transaction.get(ref);
            if (existing.exists()) {
                throw new IllegalStateException("A request already exists for this group");
            }
            transaction.set(ref, data);
            return null;
        });
    }
}
