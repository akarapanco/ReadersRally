package com.scaledscholars.readersrally;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder> {
    private static final String CURRENT_USER_ID = "O4ONRhlDs0BqdozYLG1t";
    private List<Post> posts;
    private OnDeleteListener listener;
    private OnOpenListener openListener;

    public interface OnDeleteListener {
        void onDelete(Post post);
    }

    public interface OnOpenListener {
        void onOpen(Post post);
    }

    public PostAdapter(List<Post> posts, OnDeleteListener listener, OnOpenListener openListener) {
        this.posts = posts;
        this.listener = listener;
        this.openListener = openListener;
    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_post, parent, false);
        return new PostViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {
        Post post = posts.get(position);
        holder.titleText.setText(post.title != null && !post.title.isEmpty() ? post.title : post.book);
        holder.authorText.setText(post.authorName != null && !post.authorName.isEmpty() ? "by " + post.authorName : "");

        holder.openButton.setOnClickListener(v -> openListener.onOpen(post));

        if (CURRENT_USER_ID.equals(post.authorId)) {
            holder.deleteButton.setVisibility(View.VISIBLE);
            holder.deleteButton.setOnClickListener(v -> listener.onDelete(post));
        } else {
            holder.deleteButton.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    static class PostViewHolder extends RecyclerView.ViewHolder {
        TextView titleText, authorText;
        Button deleteButton, openButton;

        PostViewHolder(View itemView) {
            super(itemView);
            titleText = itemView.findViewById(R.id.titleText);
            authorText = itemView.findViewById(R.id.authorText);
            deleteButton = itemView.findViewById(R.id.deleteButton);
            openButton = itemView.findViewById(R.id.openButton);
        }
    }
}
