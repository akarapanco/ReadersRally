package com.scaledscholars.readersrally;

//Optional prerequisite form addition task for the end user in order to join a club
public class JoinPrerequisite 
{
    public String id;
    public String groupId;
    public String reqName;
    public String description;
    public String formUrl;
    public boolean required;

    public JoinPrerequisite() {}

    public JoinPrerequisite(String id, String groupId, String title, String description, String formUrl, boolean required) 
    {
        //Include PrerequisiteID, groupID, name/subject for requisite, an end-user description, and URL
        this.id = id;
        this.groupId = groupId;
        this.reqName = title;
        this.description = description;
        this.formUrl = formUrl;
        this.required = required;
    }
}
