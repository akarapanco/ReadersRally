
package com.scaledscholars.readersrally;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class GroupRosterActivity extends AppCompatActivity
{
    private String groupId;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_group_roster);

        groupId = getIntent().getStringExtra("groupId");

        if (groupId == null
         || groupId.isEmpty())
        {
            Toast.makeText(this, "Group not found", Toast.LENGTH_SHORT).show();
            finish();
        }

        findViewById(R.id.backButton).setOnClickListener(v -> finish());
    }


}
