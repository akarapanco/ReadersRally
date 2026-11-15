
package com.scaledscholars.readersrally;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class GroupSettingsActivity extends AppCompatActivity
{
    private String groupId;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_settings);

        groupId = getIntent().getStringExtra("groupId");

        if (groupId == null
         || groupId.isEmpty())
        {
            Toast.makeText(this, "Could not find or access this group", Toast.LENGTH_SHORT).show();

            finish();
            return;
        }

        findViewById(R.id.backButton).setOnClickListener(v -> finish());
        Button viewRosterButton = findViewById(R.id.viewRosterButton);
        Button viewJoinRequestsButton = findViewById(R.id.viewJoinRequestsButton);

        viewRosterButton.setOnClickListener(v ->
        {
            Intent intent = new Intent(this, GroupRosterActivity.class);
            intent.putExtra("groupId", groupId);
            startActivity(intent);
        });

        viewJoinRequestsButton.setOnClickListener(v ->
        {
            Intent intent = new Intent(this, JoinRequestsActivity.class);
            intent.putExtra("groupId", groupId);
            startActivity(intent);
        });
    }
}
