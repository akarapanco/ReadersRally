package com.scaledscholars.readersrally;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class ReplyAdapter extends RecyclerView.Adapter<ReplyAdapter.ReplyViewHolder> {
    private static final String CURRENT_USER_ID = "O4ONRhlDs0BqdozYLG1t";
    private List<Reply> replies;
    private OnDeleteListener listener;

    public interface OnDeleteListener {
        void onDelete(Reply reply);
    }

    public ReplyAdapter(List<Reply> replies, OnDeleteListener listener) {
        this.replies = replies;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ReplyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_reply, parent, false);
        return new ReplyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReplyViewHolder holder, int position) {
        Reply reply = replies.get(position);
        holder.replyContentText.setText(reply.content);

        if (CURRENT_USER_ID.equals(reply.authorId)) {
            holder.deleteReplyButton.setVisibility(View.VISIBLE);
            holder.deleteReplyButton.setOnClickListener(v -> listener.onDelete(reply));
        } else {
            holder.deleteReplyButton.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return replies.size();
    }

    static class ReplyViewHolder extends RecyclerView.ViewHolder {
        TextView replyContentText;
        Button deleteReplyButton;

        ReplyViewHolder(View itemView) {
            super(itemView);
            replyContentText = itemView.findViewById(R.id.replyContentText);
            deleteReplyButton = itemView.findViewById(R.id.deleteReplyButton);
        }
    }
}
