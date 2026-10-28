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

        if (groups == null
         || searchTerm == null) { return results; }

        searchTerm = searchTerm.toLowerCase(Locale.ROOT).trim();

        if (searchTerm.isEmpty())
        {
            results.addAll(groups);
            return results;
        }

        for (Group group : groups)
        {

            if (group == null
             || group.getName() == null)    { continue; }

            String groupName = group.getName().toLowerCase(Locale.ROOT);

            //Exact match or partially complete
            if (groupName.contains(searchTerm))
            {
                results.add(group);
                continue;
            }

            //Check individual words for spelling mistakes
            String[] words = groupName.split("\\s+");

            for (String word : words)
            {
                int distance = levenshteinDistance(word, searchTerm);

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

            //Only allow fuzzy matching for longer IDs
            if (groupId.length() >= 6
             && searchTerm.length() >= 6)
            {

                int distance = levenshteinDistance(groupId, searchTerm);

                //Allow one character difference
                if (distance <= 1)  { results.add(group); }
            }
        }

        return results;
    }

    //Helper function used by both search methods
    private static int levenshteinDistance(String word1, String word2)
    {

        int[][] dp = new int[word1.length() + 1][word2.length() + 1];

        for (int i = 0; i <= word1.length(); i++) { dp[i][0] = i; }

        for (int j = 0; j <= word2.length(); j++) { dp[0][j] = j; }

        for (int i = 1; i <= word1.length(); i++) 
        {

            for (int j = 1; j <= word2.length(); j++)
            {

                int cost = 0;

                if (word1.charAt(i - 1) != word2.charAt(j - 1))    { cost = 1; }

                int deletion = dp[i - 1][j] + 1;
                int insertion = dp[i][j - 1] + 1;
                int substitution = dp[i - 1][j - 1] + cost;

                dp[i][j] = Math.min(deletion, Math.min(insertion, substitution));
            }
        }

        return dp[word1.length()][word2.length()];
    }
}