package com.scaledscholars.readersrally;

public class GroupFilter 
{
    private String genre;
    private Integer minBookLength;
    private Integer maxBookLength;
    private String communicationMethod;
    private Integer minGroupSize;
    private Integer maxGroupSize;

    public GroupFilter(String genre, Integer minBookLength, Integer maxBookLength, String communicationMethod, Integer minGroupSize, Integer maxGroupSize) 
    {
        this.genre = genre;
        this.minBookLength = minBookLength;
        this.maxBookLength = maxBookLength;
        this.communicationMethod = communicationMethod;
        this.minGroupSize = minGroupSize;
        this.maxGroupSize = maxGroupSize;
    }

    //Genre of book club (Fantasy, Mystery, etc) *potentially look at picking from an array of genre strings in final version*
    public String getGenre()    { return genre; }

    //Minimum page book length on user-selected slider
    public Integer getMinBookLength()   { return minBookLength; }

    //Maximum page book length on user-selected slider
    public Integer getMaxBookLength()   { return maxBookLength; }

    //Online/In-person (potentially add a time zone filter in the future)
    public String getCommunicationMethod()      { return communicationMethod; }

    //Minimum group size on user-selected slider
    public Integer getMinGroupSize()    { return minGroupSize; }

    //Maximum group size on user-selected slider
    public Integer getMaxGroupSize()    { return maxGroupSize; }


}