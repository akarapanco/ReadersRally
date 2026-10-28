package com.scaledscholars.readersrally;

import java.util.ArrayList;
import java.util.List;

public class Main 
{

    public static void main(String[] args) 
    {
        //How the book clubs will display as search results:
        //GroupID
        //Name
        //Genre
        //Book Length
        //Communication Method
        //Group Size

        //Preliminary Book club list test groups:
        List<Group> clubs = new ArrayList<>();

        //Fantasy Group - online cadence, large book, medium group
        clubs.add(new Group(
                "001",
                "Fantasy Group",
                "Fantasy",
                "Fantasy Book",
                450,
                "Online",
                15
                            ));

        //Mystery Group - in-person cadence, medium book, medium group
        clubs.add(new Group(
                "002",
                "Mystery Group",
                "Mystery",
                "Mystery Book",
                320,
                "In Person",
                8
                            ));

        //Large Fantasy Group - in-person cadence, large book, large group
        clubs.add(new Group(
                "003",
                "Large Fantasy Group",
                "Fantasy",
                "Large Fantasy Book",
                700,
                "In Person",
                25
                            ));

        //Thriller Group - online cadence, small book, small group
        clubs.add(new Group(
                "004",
                "Thriller Group",
                "Thriller",
                "Thriller Book",
                280,
                "Online",
                6
                            ));

        //Sci-Fi Group - online cadence, medium book, medium group
        clubs.add(new Group(
                "005",
                "Sci-Fi Group",
                "Science Fiction",
                "Sci-Fi Book",
                500,
                "Online",
                12
                            ));

        System.out.println("TESTING FILTER OPTION:");
        System.out.println("Creating a test filter configuration...");
        
        //----------------------------
        //Filter config entered by a user - test
        GroupFilter filter = new GroupFilter( "Fantasy", 200,500, "Online", 5, 20);
        System.out.println("Fetching search results...");
        
        //----------------------------
        //Accumulate the list of results
        List<Group> results = Filter.filterGroups(clubs, filter);
  
        //Populate the Results
        System.out.println("Search Results:");

        for (Group club : results) 
        {
         System.out.println(club.getName());
         System.out.println(club.getGenre()); 
         System.out.println(club.getBookLength()); 
         System.out.println(club.getCommunicationMethod()); 
         System.out.println(club.getGroupSize()); 
        }

        //----------------------------
        //Search Test
        //----------------------------

        List<Group> searchResults = Search.searchName(clubs, "Fantasy");

        List<Group> finalResults = Filter.filterGroups(searchResults, filter);

        for (Group club : finalResults)      { System.out.println(club.getName()); }

    }
}
