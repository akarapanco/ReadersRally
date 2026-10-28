package com.scaledscholars.readersrally;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.slider.RangeSlider;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class BrowseGroupsActivity extends AppCompatActivity
{

    private FirebaseFirestore db;

    private List<Group> groups;
    private List<Group> displayedGroups;

    private BrowseGroupsAdapter adapter;
    private SearchView searchView;

    // Current search
    private String currentSearch = "";

    // Current filters
    private String selectedGenre = null;
    private String selectedCommunication = null;

    private Integer minBookFilter = null;
    private Integer maxBookFilter = null;

    private Integer minSizeFilter = null;
    private Integer maxSizeFilter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_browse_groups);

        db = FirebaseFirestore.getInstance();

        groups = new ArrayList<>();
        displayedGroups = new ArrayList<>();

        // RecyclerView
        RecyclerView recyclerView =
                findViewById(R.id.browseGroupsRecyclerView);

        recyclerView.setLayoutManager(
                new LinearLayoutManager(this)
        );

        adapter = new BrowseGroupsAdapter(displayedGroups);
        recyclerView.setAdapter(adapter);

        // SearchView
        searchView = findViewById(R.id.groupSearchBar);

        searchView.setOnQueryTextListener(
                new SearchView.OnQueryTextListener() {

                    @Override
                    public boolean onQueryTextSubmit(String query) {
                        currentSearch = query;
                        updateDisplayedGroups();
                        return true;
                    }

                    @Override
                    public boolean onQueryTextChange(String newText) {
                        currentSearch = newText;
                        updateDisplayedGroups();
                        return true;
                    }
                }
        );

        //Filter button
        Button filterButton = findViewById(R.id.filterButton);

        filterButton.setOnClickListener(
                v -> showFilterDialog()
        );

        //Temporary test data upload
        TestGroups.uploadTestGroups();

        //Load groups
        loadGroups();
    }

    // Load groups from Firestore
    private void loadGroups()
    {

        db.collection("groups")
                .get()
                .addOnSuccessListener(querySnapshot -> {

                    groups.clear();

                    for (DocumentSnapshot doc : querySnapshot)
                    {

                        Group group = new Group(
                                doc.getId(),
                                doc.getString("name"),
                                doc.getString("genre"),
                                doc.getString("bookTitle"),
                                getInteger(doc, "bookLength"),
                                doc.getString("communicationMethod"),
                                getInteger(doc, "groupSize")
                        );

                        groups.add(group);
                    }

                    updateDisplayedGroups();
                })
                .addOnFailureListener(e -> {

                    Toast.makeText(
                            this,
                            "Failed to load groups: " + e.getMessage(),
                            Toast.LENGTH_LONG
                    ).show();
                });
    }

    // Apply BOTH search and filters
    private void updateDisplayedGroups()
    {

        GroupFilter filter = new GroupFilter(
                selectedGenre,
                minBookFilter,
                maxBookFilter,
                selectedCommunication,
                minSizeFilter,
                maxSizeFilter
        );

        List<Group> filteredGroups =
                Filter.filterGroups(groups, filter);

        displayedGroups.clear();

        String searchText = currentSearch.trim()
                .toLowerCase(Locale.ROOT);

        for (Group group : filteredGroups)
        {

            String name = group.getName();
            String id = group.getId();

            if (searchText.isEmpty())
            {
                displayedGroups.add(group);
            }
            else
            {

                boolean matchesName =
                        name != null
                                && name.toLowerCase(Locale.ROOT)
                                        .contains(searchText);

                boolean matchesId =
                        id != null
                                && id.toLowerCase(Locale.ROOT)
                                        .contains(searchText);

                if (matchesName || matchesId) { displayedGroups.add(group); }
            }
        }

        adapter.notifyDataSetChanged();
    }

    //Show filter dialog
    private void showFilterDialog()
    {

        View dialogView = getLayoutInflater()
                .inflate(R.layout.browse_filter_config, null);

        Spinner genreSpinner =
                dialogView.findViewById(R.id.genreSpinner);

        Spinner communicationSpinner =
                dialogView.findViewById(R.id.communicationSpinner);

        RangeSlider bookSlider =
                dialogView.findViewById(R.id.bookLengthSlider);

        RangeSlider sizeSlider =
                dialogView.findViewById(R.id.groupSizeSlider);

        TextView bookValue =
                dialogView.findViewById(R.id.bookLengthValue);

        TextView sizeValue =
                dialogView.findViewById(R.id.groupSizeValue);

        // Restore genre
        if (selectedGenre != null)
        {

            for (int i = 0; i < genreSpinner.getCount(); i++)
            {

                String option =
                        genreSpinner.getItemAtPosition(i).toString();

                if (option.equalsIgnoreCase(selectedGenre))
                {

                    genreSpinner.setSelection(i);
                    break;
                }
            }
        }

        // Restore communication
        if (selectedCommunication != null) {

            for (int i = 0; i < communicationSpinner.getCount(); i++)
            {

                String option =
                        communicationSpinner.getItemAtPosition(i).toString();

                if (option.equalsIgnoreCase(selectedCommunication))
                {
                    communicationSpinner.setSelection(i);
                    break;
                }
            }
        }

        // Restore book range
        float minBook = 0;
        float maxBook = 1000;

        if (minBookFilter != null)
        { minBook = minBookFilter; }

        if (maxBookFilter != null)
        { maxBook = maxBookFilter; }

        bookSlider.setValues(minBook, maxBook);

        // Restore group size range
        float minSize = 1;
        float maxSize = 50;

        if (minSizeFilter != null)
        { minSize = minSizeFilter; }

        if (maxSizeFilter != null)
        { maxSize = maxSizeFilter; }

        sizeSlider.setValues(minSize, maxSize);

        updateRangeLabels(
                bookSlider,
                sizeSlider,
                bookValue,
                sizeValue
        );

        bookSlider.addOnChangeListener(
                (slider, value, fromUser) ->
                        updateRangeLabels(
                                bookSlider,
                                sizeSlider,
                                bookValue,
                                sizeValue
                        )
        );

        sizeSlider.addOnChangeListener(
                (slider, value, fromUser) ->
                        updateRangeLabels(
                                bookSlider,
                                sizeSlider,
                                bookValue,
                                sizeValue
                        )
        );

        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("Filter Groups")
                .setView(dialogView)

                .setPositiveButton("Apply Filters", (d, which) -> {

                    selectedGenre =
                            genreSpinner.getSelectedItem().toString();

                    selectedCommunication =
                            communicationSpinner.getSelectedItem().toString();

                    if (selectedGenre.equals("Any"))
                    { selectedGenre = null; }

                    if (selectedCommunication.equals("Any"))
                    { selectedCommunication = null; }

                    int minBookSelected =
                            bookSlider.getValues().get(0).intValue();

                    int maxBookSelected =
                            bookSlider.getValues().get(1).intValue();

                    int minSizeSelected =
                            sizeSlider.getValues().get(0).intValue();

                    int maxSizeSelected =
                            sizeSlider.getValues().get(1).intValue();

                    // Book length filter
                    if (minBookSelected == 0
                            && maxBookSelected == 1000)
                    {
                        minBookFilter = null;
                        maxBookFilter = null;

                    }
                    else
                    {
                        minBookFilter = minBookSelected;
                        maxBookFilter = maxBookSelected;
                    }

                    //Group size filter
                    if (minSizeSelected == 1
                            && maxSizeSelected == 50)
                    {
                        minSizeFilter = null;
                        maxSizeFilter = null;

                    }
                    else
                    {
                        minSizeFilter = minSizeSelected;
                        maxSizeFilter = maxSizeSelected;
                    }

                    updateDisplayedGroups();

                    Toast.makeText(
                            this,
                            displayedGroups.size() + " groups found",
                            Toast.LENGTH_SHORT
                    ).show();
                })

                .setNeutralButton("Clear", (d, which) -> {

                    selectedGenre = null;
                    selectedCommunication = null;

                    minBookFilter = null;
                    maxBookFilter = null;

                    minSizeFilter = null;
                    maxSizeFilter = null;

                    updateDisplayedGroups();
                })

                .setNegativeButton("Cancel", null)
                .create();

        dialog.show();
    }

    // Update slider labels
    private void updateRangeLabels(RangeSlider bookSlider, RangeSlider sizeSlider, TextView bookValue, TextView sizeValue)
    {

        int minBook = bookSlider.getValues().get(0).intValue();
        int maxBook = bookSlider.getValues().get(1).intValue();

        if (minBook == 0
                && maxBook == 1000)
        {

            bookValue.setText("Any book length");

        }
        else
        {
            bookValue.setText(
                    minBook + " - " + maxBook + " pages"
            );
        }

        int minSize = sizeSlider.getValues().get(0).intValue();
        int maxSize = sizeSlider.getValues().get(1).intValue();

        if (minSize == 1 && maxSize == 50)
        {
            sizeValue.setText("Any group size");
        }
        else
        {
            sizeValue.setText(minSize + " - " + maxSize + " members");
        }
    }

    // Convert Firestore Long to Integer
    private Integer getInteger(DocumentSnapshot doc, String field)
    {
        Long value = doc.getLong(field);

        if (value == null)  { return null; }

        return value.intValue();
    }
}