package com.scaledscholars.readersrally;

public class Group 
{
    private String id;
    private String name;
    private String genre;
    private String bookTitle;
    private String communicationMethod;
    private Integer bookLength;
    private Integer groupSize;

    public Group() {}

    public Group(String id, String name, String bookTitle)
    {
        this.id = id;
        this.name = name;
        this.bookTitle = bookTitle;
    }

    //Constructor for filter and search operations
    public Group(String id, String name, String genre, String bookTitle, Integer bookLength, String communicationMethod, Integer groupSize) 
    {
        this.id = id;
        this.name = name;
        this.genre = genre;
        this.bookTitle = bookTitle;
        this.bookLength = bookLength;
        this.communicationMethod = communicationMethod;
        this.groupSize = groupSize;
    }

    //Getters
    //Display id (search)
    public String getId()       { return id; }

    //Display name (search)
    public String getName()     { return name; }

    //Display genre (filter)
    public String getGenre()    { return genre; }

    //Display book title (search?)
    public String getBookTitle() { return bookTitle; }

    //Display book length (filter)
    public Integer getBookLength()  { return bookLength; }

    //Display communication method (filter)
    public String getCommunicationMethod()      { return communicationMethod; }

    //Display group size (filter)
    public Integer getGroupSize()   { return groupSize; }
}