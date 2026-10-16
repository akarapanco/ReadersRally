package com.scaledscholars.readersrally;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Button;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder> {
    private static final String CURRENT_USER_ID = "O4ONRhlDs0BqdozYLG1t";
    private List<Post> posts;
    private OnDeleteListener listener;

    public interface OnDeleteListener {
        void onDelete(Post post);
    }

    public PostAdapter(List<Post> posts, OnDeleteListener listener) {
        this.posts = posts;
        this.listener = listener;
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
        holder.genreBookText.setText(post.book);
        holder.contentText.setText(post.content);
        holder.repliesText.setText(post.repliesEnabled ? "Replies on" : "Replies off");

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
        TextView genreBookText, contentText, repliesText;
        Button deleteButton;

        PostViewHolder(View itemView) {
            super(itemView);
            genreBookText = itemView.findViewById(R.id.genreBookText);
            contentText = itemView.findViewById(R.id.contentText);
            repliesText = itemView.findViewById(R.id.repliesText);
            deleteButton = itemView.findViewById(R.id.deleteButton);
        }
    }
}
