package com.scaledscholars.readersrally;

public class Post {
    public String id;
    public String groupId;
    public String authorId;
    public String authorName;
    public String title;
    public String book;
    public String content;
    public boolean repliesEnabled;
    public long createdAt;

    public Post() {}

    public Post(String id, String groupId, String authorId, String authorName, String title,
                String book, String content, boolean repliesEnabled, long createdAt) {
        this.id = id;
        this.groupId = groupId;
        this.authorId = authorId;
        this.authorName = authorName;
        this.title = title;
        this.book = book;
        this.content = content;
        this.repliesEnabled = repliesEnabled;
        this.createdAt = createdAt;
    }
}
