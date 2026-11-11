package com.scaledscholars.readersrally;

public class Reply {
    public String id;
    public String postId;
    public String authorId;
    public String content;
    public long createdAt;
    public String authorName;

    public Reply() {}

    public Reply(String id, String postId, String authorId, String content, long createdAt) {
        this.id = id;
        this.postId = postId;
        this.authorId = authorId;
        this.content = content;
        this.createdAt = createdAt;
    }
}
