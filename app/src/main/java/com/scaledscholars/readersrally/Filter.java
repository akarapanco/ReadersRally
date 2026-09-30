package com.scaledscholars.readersrally;

import java.util.ArrayList;
import java.util.List;

public class Filter 
{

    public static List<Group> filterGroups(List<Group> clubs, GroupFilter filter) 
    {

        List<Group> results = new ArrayList<>();

        //Early return case if no clubs found
        if (clubs == null)  { return results; }

        //Early return case if no filters are selected
        // if (filter == null) 
        // {
        //     results.addAll(clubs);
        //     return results;
        // }

        for (Group club : clubs) 
        {
            //For Genre, read the filter value if it's not null
            if (filter.getGenre() != null) 
            {
                if (club.getGenre().equalsIgnoreCase(filter.getGenre()) == false)  { continue; }
            }

            //Min Book Length range (should never be empty unless filter not selected)
            if (filter.getMinBookLength() != null) 
            {
                //Populate results if book length is greater than or equal to the minimum
                if (club.getBookLength() < filter.getMinBookLength())   { continue; }
            }

            //Maximum Book Length range (should never be empty unless filter not selected)
            if (filter.getMaxBookLength() != null) 
            {
                //Populate results if book length is less than or equal to the maximum
                if (club.getBookLength() > filter.getMaxBookLength())   { continue; }
            }

            //Communication Method, read the filter value if it's not null
            if (filter.getCommunicationMethod() != null) 
            {
                if (club.getCommunicationMethod().equalsIgnoreCase(filter.getCommunicationMethod()) == false)   { continue; }
            }

            // Min Group Size, read the filter value if it's not null
            if (filter.getMinGroupSize() != null) 
            {
                //Populate results if group size is greater than or equal to the minimum
                if (club.getGroupSize() < filter.getMinGroupSize())     { continue; }
            }

            //Maximum Group Size, read the filter value if it's not null
            if (filter.getMaxGroupSize() != null) 
            {
                //Populate results if group size is less than or equal to the maximum
                if (club.getGroupSize() > filter.getMaxGroupSize())     { continue; }
            }

            //Add the club to results list if it fits the condition of all filters
            results.add(club);
        }

        return results;
    }
}