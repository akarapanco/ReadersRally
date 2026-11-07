package com.scaledscholars.readersrally;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class GroupDetailsActivity extends AppCompatActivity
{
    private FirebaseFirestore db;
    private String groupId;
    private TextView groupName;
    private TextView groupBio;
    private TextView groupGenre;
    private TextView groupBook;
    private TextView groupBookLength;
    private TextView groupCommunication;
    private TextView groupSize;

    private Button requestJoinButton;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_group_details);

        db = FirebaseFirestore.getInstance();

        //Get selected group ID
        groupId = getIntent().getStringExtra("groupId");

        groupName = findViewById(R.id.groupDetailsName);
        groupBio = findViewById(R.id.groupDetailsBio);
        groupGenre = findViewById(R.id.groupDetailsGenre);
        groupBook = findViewById(R.id.groupDetailsBook);
        groupBookLength = findViewById(R.id.groupDetailsBookLength);
        groupCommunication = findViewById(R.id.groupDetailsCommunication);
        groupSize = findViewById(R.id.groupDetailsSize);

        requestJoinButton = findViewById(R.id.requestJoinButton);

        if (groupId == null
         || groupId.isEmpty())
        {
            Toast.makeText(this, "Group not found.", Toast.LENGTH_SHORT).show();
            finish();

            return;
        }

        loadGroupDetails();

        requestJoinButton.setOnClickListener(v ->
        { Toast.makeText(this, "Join request sent (to nobody)", Toast.LENGTH_SHORT).show();});
    }

    private void loadGroupDetails()
    {
        //Use with firestore
    }

    private void displayGroupDetails(DocumentSnapshot doc)
    {

        if (!doc.exists())
        {
            Toast.makeText(this, "Group does not exist.", Toast.LENGTH_SHORT).show();
            finish();

            return;
        }

        String name = doc.getString("name");
        String bio = doc.getString("bio");
        String genre = doc.getString("genre");
        String book = doc.getString("bookTitle");
        String communication = doc.getString("communicationMethod");

        Long bookLength = doc.getLong("bookLength");
        Long members = doc.getLong("groupSize");

        groupName.setText(valueOrDefault(name));
        groupBio.setText(valueOrDefault(bio));
        groupGenre.setText("Genre: " + valueOrDefault(genre));
        groupBook.setText("Currently Reading: " + valueOrDefault(book));

        if (bookLength != null)
        { groupBookLength.setText("Book Length: " + bookLength + " pages"); }
        else
        { groupBookLength.setText("Book Length: Not specified"); }

        groupCommunication.setText(
                "Communication Method: " + valueOrDefault(communication)
        );

        if (members != null) { groupSize.setText("Members: " + members); }
        else
        { groupSize.setText("Members: Not specified"); }
    }

    private String valueOrDefault(String value)
    {
        if (value == null
         || value.trim().isEmpty())
        { return "Not specified"; }

        return value;
    }

}