package com.scaledscholars.readersrally;

//Optional prerequisite task for the end user in order to join a club
public class JoinPrerequisite 
{
    public String id;
    public String groupId;
    public String title;
    public String description;
    public String formUrl;
    public boolean required;

    public JoinPrerequisite() {}

    public JoinPrerequisite(String id, String groupId, String title, String description, String formUrl, boolean required) 
    {
        this.id = id;
        this.groupId = groupId;
        this.title = title;
        this.description = description;
        this.formUrl = formUrl;
        this.required = required;
    }
}
