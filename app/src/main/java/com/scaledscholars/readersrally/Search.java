package com.scaledscholars.readersrally;

import java.util.ArrayList;
import java.util.List;

public class Search 
{
    public static List<Group> searchName(List<Group> groups, String searchTerm)
    {
        List<Group> results = new ArrayList<>();

        for (Group group : groups)
        {
            if (group.getName().contains(searchTerm))   { results.add(group); }
        }

        return results;
    }
}
