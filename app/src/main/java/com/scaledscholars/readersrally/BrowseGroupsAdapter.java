
package com.scaledscholars.readersrally;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class BrowseGroupsAdapter
        extends RecyclerView.Adapter<BrowseGroupsAdapter.GroupViewHolder>
{

    private final List<Group> groups;

    public BrowseGroupsAdapter(List<Group> groups) {
        this.groups = groups;
    }

    @NonNull
    @Override
    public GroupViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_browse_group_card, parent, false);

        return new GroupViewHolder(view);
    }

    @Override
    public void onBindViewHolder(
            @NonNull GroupViewHolder holder, int position)
    {

        Group group = groups.get(position);
        Context context = holder.itemView.getContext();

        String name = group.getName();
        String book = group.getBookTitle();
        String genre = group.getGenre();
        Integer size = group.getGroupSize();

        if (name == null)
        { name = context.getString(R.string.group_unnamed); }

        if (book == null)
        { book = context.getString(R.string.group_not_specified); }

        if (genre == null)
        { genre = context.getString(R.string.group_not_specified); }

        holder.groupNameText.setText(name);

        holder.groupBookText.setText(
                context.getString(R.string.group_book, book)
        );

        holder.groupGenreText.setText(
                context.getString(R.string.group_genre, genre)
        );

        if (size != null)
        {
            holder.groupSizeText.setText(
                    context.getString(R.string.group_members, size)
            );
        }
        else
        {
            holder.groupSizeText.setText(
                    R.string.group_members_unknown
            );
        }

        holder.viewGroupButton.setOnClickListener(v -> {

            Intent intent = new Intent(
                    context,
                    GroupDetailsActivity.class
            );

            // Pass the selected group's Firestore document ID
            intent.putExtra("groupId", group.getId());

            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount()  { return groups.size(); }

    public static class GroupViewHolder
            extends RecyclerView.ViewHolder
    {

        TextView groupNameText;
        TextView groupBookText;
        TextView groupGenreText;
        TextView groupSizeText;
        Button viewGroupButton;

        public GroupViewHolder(@NonNull View itemView)
        {
            super(itemView);

            groupNameText = itemView.findViewById(R.id.groupNameText);
            groupBookText = itemView.findViewById(R.id.groupBookText);
            groupGenreText = itemView.findViewById(R.id.groupGenreText);
            groupSizeText = itemView.findViewById(R.id.groupSizeText);
            viewGroupButton = itemView.findViewById(R.id.viewGroupButton);
        }
    }
}
