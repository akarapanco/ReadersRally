package com.scaledscholars.readersrally;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import com.google.firebase.firestore.FirebaseFirestore;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder> {
    private final String CURRENT_USER_ID = CurrentUser.id();
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
            holder.editButton.setVisibility(View.VISIBLE);
            holder.editButton.setOnClickListener(v -> showEditDialog(v.getContext(), post));
            holder.deleteButton.setOnClickListener(v -> listener.onDelete(post));
        } else {
            holder.deleteButton.setVisibility(View.GONE);
            holder.editButton.setVisibility(View.GONE);
        }
    }

    private void showEditDialog(android.content.Context ctx, Post post) {
        LinearLayout box = new LinearLayout(ctx);
        box.setOrientation(LinearLayout.VERTICAL);
        int pad = (int) (20 * ctx.getResources().getDisplayMetrics().density);
        box.setPadding(pad, pad / 2, pad, 0);

        EditText titleInput = new EditText(ctx);
        titleInput.setHint("Title");
        titleInput.setText(post.title);
        EditText contentInput = new EditText(ctx);
        contentInput.setHint("Post");
        contentInput.setText(post.content);
        EditText bookInput = new EditText(ctx);
        bookInput.setHint("Book");
        bookInput.setText(post.book);
        Switch repliesSwitch = new Switch(ctx);
        repliesSwitch.setText("Allow replies");
        repliesSwitch.setChecked(post.repliesEnabled);
        box.addView(titleInput);
        box.addView(bookInput);
        box.addView(contentInput);
        box.addView(repliesSwitch);

        new AlertDialog.Builder(ctx)
            .setTitle("Edit post")
            .setView(box)
            .setPositiveButton("Save", (d, w) -> {
                String newTitle = titleInput.getText().toString().trim();
                String newContent = contentInput.getText().toString().trim();
                String newBook = bookInput.getText().toString().trim();
                boolean newReplies = repliesSwitch.isChecked();
                if (newContent.isEmpty()) {
                    Toast.makeText(ctx, "Post can't be empty", Toast.LENGTH_SHORT).show();
                    return;
                }
                FirebaseFirestore.getInstance().collection("posts").document(post.id)
                    .update("title", newTitle, "book", newBook, "content", newContent, "repliesEnabled", newReplies)
                    .addOnSuccessListener(unused -> {
                        post.title = newTitle;
                        post.content = newContent;
                        post.book = newBook;
                        post.repliesEnabled = newReplies;
                        notifyDataSetChanged();
                        Toast.makeText(ctx, "Post updated", Toast.LENGTH_SHORT).show();
                    })
                    .addOnFailureListener(e ->
                        Toast.makeText(ctx, "Failed to update post", Toast.LENGTH_SHORT).show());
            })
            .setNegativeButton("Cancel", null)
            .show();
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    static class PostViewHolder extends RecyclerView.ViewHolder {
        TextView titleText, authorText;
        Button deleteButton, openButton, editButton;

        PostViewHolder(View itemView) {
            super(itemView);
            titleText = itemView.findViewById(R.id.titleText);
            authorText = itemView.findViewById(R.id.authorText);
            deleteButton = itemView.findViewById(R.id.deleteButton);
            openButton = itemView.findViewById(R.id.openButton);
            editButton = itemView.findViewById(R.id.editButton);
        }
    }
}
