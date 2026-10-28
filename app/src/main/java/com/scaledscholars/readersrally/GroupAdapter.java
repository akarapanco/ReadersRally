package com.scaledscholars.readersrally;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class GroupAdapter extends RecyclerView.Adapter<GroupAdapter.GroupViewHolder> {

    public interface OnLeaveClickListener {
        void onLeaveClicked(Group group);
    }

    private List<Group> groupList;
    private OnLeaveClickListener listener;
    private String buttonLabel;

    public GroupAdapter(List<Group> groupList, OnLeaveClickListener listener, String buttonLabel) {
        this.groupList = groupList;
        this.listener = listener;
        this.buttonLabel = buttonLabel;
    }

    @NonNull
    @Override
    public GroupViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_group_card, parent, false);
        return new GroupViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GroupViewHolder holder, int position) {
        Group group = groupList.get(position);
        holder.nameText.setText(group.getName());
        holder.bookText.setText(group.getBookTitle());
        holder.leaveButton.setText(buttonLabel);
        holder.leaveButton.setOnClickListener(v -> listener.onLeaveClicked(group));
    }

    @Override
    public int getItemCount() {
        return groupList.size();
    }

    static class GroupViewHolder extends RecyclerView.ViewHolder {
        TextView nameText, bookText;
        Button leaveButton;

        GroupViewHolder(View itemView) {
            super(itemView);
            nameText = itemView.findViewById(R.id.groupNameText);
            bookText = itemView.findViewById(R.id.groupBookText);
            leaveButton = itemView.findViewById(R.id.leaveButton);
        }
    }
}
