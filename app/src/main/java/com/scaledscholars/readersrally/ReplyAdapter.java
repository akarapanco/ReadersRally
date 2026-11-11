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
    private final String CURRENT_USER_ID = CurrentUser.id();
    private List<Reply> replies;
    private OnDeleteListener listener;
    private OnEditListener editListener;

    public interface OnDeleteListener {
        void onDelete(Reply reply);
    }

    public interface OnEditListener {
        void onEdit(Reply reply);
    }

    public ReplyAdapter(List<Reply> replies, OnDeleteListener listener, OnEditListener editListener) {
        this.replies = replies;
        this.listener = listener;
        this.editListener = editListener;
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
        String who = reply.authorName != null ? reply.authorName : "Someone";
        holder.replyContentText.setText(who + ": " + reply.content);

        if (CURRENT_USER_ID.equals(reply.authorId)) {
            holder.deleteReplyButton.setVisibility(View.VISIBLE);
            holder.editReplyButton.setVisibility(View.VISIBLE);
            holder.editReplyButton.setOnClickListener(v -> editListener.onEdit(reply));
            holder.deleteReplyButton.setOnClickListener(v -> listener.onDelete(reply));
        } else {
            holder.deleteReplyButton.setVisibility(View.GONE);
            holder.editReplyButton.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return replies.size();
    }

    static class ReplyViewHolder extends RecyclerView.ViewHolder {
        TextView replyContentText;
        Button deleteReplyButton, editReplyButton;

        ReplyViewHolder(View itemView) {
            super(itemView);
            replyContentText = itemView.findViewById(R.id.replyContentText);
            deleteReplyButton = itemView.findViewById(R.id.deleteReplyButton);
            editReplyButton = itemView.findViewById(R.id.editReplyButton);
        }
    }
}
