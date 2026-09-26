package com.scaledscholars.readersrally;

public class Group {
    public String id;
    public String name;
    public String genre;
    public String bookTitle;
    public String communicationMethod;

    public Group() {}

    public Group(String id, String name, String bookTitle) {
        this.id = id;
        this.name = name;
        this.bookTitle = bookTitle;
    }
}
