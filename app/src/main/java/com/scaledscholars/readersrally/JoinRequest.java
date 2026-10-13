package com.scaledscholars.readersrally;

public class JoinRequest {
    public String id;
    public String groupId;
    public String userId;
    public String message;
    public String status;
    public String groupName;
    public String userEmail;

    public JoinRequest() {}

    public JoinRequest(String id, String groupId, String userId, String message, String status) {
        this.id = id;
        this.groupId = groupId;
        this.userId = userId;
        this.message = message;
        this.status = status;
    }
}
