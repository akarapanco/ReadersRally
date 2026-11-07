package com.scaledscholars.readersrally;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Search
{

    //Search for groups by name
    public static List<Group> searchName(List<Group> groups, String searchTerm)
    {

        List<Group> results = new ArrayList<>();

        //Base case - If no groups exist and no search term exists
        if (groups == null
         || searchTerm == null) { return results; }

        searchTerm = searchTerm.toLowerCase(Locale.ROOT).trim();

        if (searchTerm.isEmpty())
        {
            results.addAll(groups);
            return results;
        }

        //Check each group if the search term is present
        for (Group group : groups)
        {
            if (group == null
             || group.getName() == null)    { continue; }

            String groupName = group.getName().toLowerCase(Locale.ROOT);

            if (groupName.contains(searchTerm))
            {
                results.add(group);
                continue;
            }

            //Check individual words for spelling mistakes
            String[] words = groupName.split("\\s+");

            for (String word : words)
            {
                //Use helper function to account for character differences due to typos
                int distance = incSpellingHelper(word, searchTerm);

                //Allow 3 character differences maximum for individual words in the group name
                if (distance <= 3)
                {
                    results.add(group);
                    break;
                }
            }
        }

        return results;
    }

    //Search for groups by ID
    public static List<Group> searchId(List<Group> groups, String searchTerm)
    {
        List<Group> results = new ArrayList<>();

        if (groups == null
         || searchTerm == null)     { return results; }

        searchTerm = searchTerm.trim();

        if (searchTerm.isEmpty())
        {
            results.addAll(groups);
            return results;
        }

        for (Group group : groups) 
        {
            if (group == null
             || group.getId() == null)  { continue; }

            String groupId = group.getId();

            //Exact or partial ID match
            if (groupId.contains(searchTerm))
            {
                results.add(group);
                continue;
            }

            //Only use character distance matching for IDs if the group ID is longer than 5 digits
            if (groupId.length() >= 5
             && searchTerm.length() >= 5)
            {
                int distance = incSpellingHelper(groupId, searchTerm);

                //Allow a 1-char difference
                if (distance <= 1)  { results.add(group); }
            }
        }

        return results;
    }

    //Helper function to calculate character distance (number of characters different from the correct string)
    private static int incSpellingHelper(String word1, String word2)
    {

        int[][] charDistances = new int[word1.length() + 1][word2.length() + 1];

        for (int i = 0; i <= word1.length(); i++) { charDistances[i][0] = i; }

        for (int j = 0; j <= word2.length(); j++) { charDistances[0][j] = j; }

        for (int i = 1; i <= word1.length(); i++) 
        {

            for (int j = 1; j <= word2.length(); j++)
            {
                int charsToReplace = 0;

                if (word1.charAt(i - 1) != word2.charAt(j - 1))    { charsToReplace = 1; }

                int delete = charDistances[i - 1][j] + 1;
                int insert = charDistances[i][j - 1] + 1;
                int substitute = charDistances[i - 1][j - 1] + charsToReplace;

                charDistances[i][j] = Math.min(delete, Math.min(insert, substitute));
            }
        }

        return charDistances[word1.length()][word2.length()];
    }
}