package com.scaledscholars.readersrally;

import android.util.Log;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

    public class TestGroups
    {

        public static List<Group> getTestGroups()
        {

            // name, genre, bookLength, communicationMethod, groupSize
            List<Group> testGroups = new ArrayList<>();

            testGroups.add(new Group("test01", "Fantasy Group", "Fantasy", "Demo Book", 450, "Online", 15));

            testGroups.add(new Group("test02", "Mystery Group", "Mystery", "Demo Book", 320, "In Person", 8));

            testGroups.add(new Group("test03", "Fantasy Group", "Fantasy", "Demo Book", 700, "In Person", 25));

            testGroups.add(new Group("test04", "Small Fantasy Group", "Fantasy", "Demo Book", 280, "Online", 6));

            testGroups.add(new Group("test05", "Sci-Fi Group", "Sci-Fi", "Demo Book", 500, "Online", 12));

            testGroups.add(new Group("test06", "Romance Group", "Romance", "Demo Book", 350, "In Person", 10));

            testGroups.add(new Group("test07", "History Group", "History", "Demo Book", 600, "Online", 30));

            testGroups.add(new Group("test08", "Short Story Group", "Fiction", "Demo Book", 150, "In Person", 5));

            testGroups.add(new Group("test09", "Horror Group", "Horror", "Demo Book", 400, "Online", 20));

            testGroups.add(new Group("test10", "Young Adult Group", "Young Adult", "Demo Book", 300, "Online", 18));

            testGroups.add(new Group("test11", "Religious Studies Group", "Religion", "Demo Book", 550, "In Person", 14));

            testGroups.add(new Group("test12", "Small Mystery Group", "Mystery", "Demo Book", 250, "Online", 4));

            testGroups.add(new Group("test13", "Small Mystery Group", "Mystery", "Demo Book", 500, "In Person", 20));

            return testGroups;
        }
    }


