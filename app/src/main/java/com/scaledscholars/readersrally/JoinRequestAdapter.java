package com.scaledscholars.readersrally;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class JoinRequestAdapter extends RecyclerView.Adapter<JoinRequestAdapter.RequestViewHolder> {
    public interface OnActionListener {
        void onApprove(JoinRequest request);
        void onDeny(JoinRequest request);
    }

    private List<JoinRequest> requestList;
    private OnActionListener listener;

    public JoinRequestAdapter(List<JoinRequest> requestList, OnActionListener listener) {
        this.requestList = requestList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public RequestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_join_request_card, parent, false);
        return new RequestViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RequestViewHolder holder, int position) {
        JoinRequest request = requestList.get(position);
        holder.groupNameText.setText(request.groupName);
        holder.userText.setText(request.userEmail);
        holder.messageText.setText(request.message);
        holder.approveButton.setOnClickListener(v -> listener.onApprove(request));
        holder.denyButton.setOnClickListener(v -> listener.onDeny(request));
    }

    @Override
    public int getItemCount() {
        return requestList.size();
    }

    static class RequestViewHolder extends RecyclerView.ViewHolder {
        TextView groupNameText, userText, messageText;
        Button approveButton, denyButton;

        RequestViewHolder(View itemView) {
            super(itemView);
            groupNameText = itemView.findViewById(R.id.requestGroupNameText);
            userText = itemView.findViewById(R.id.requestUserText);
            messageText = itemView.findViewById(R.id.requestMessageText);
            approveButton = itemView.findViewById(R.id.approveButton);
            denyButton = itemView.findViewById(R.id.denyButton);
        }
    }
}
