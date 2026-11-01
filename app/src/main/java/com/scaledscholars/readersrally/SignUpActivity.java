package com.scaledscholars.readersrally;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.WriteBatch;

import java.util.HashMap;
import java.util.Map;

public class SignUpActivity extends AppCompatActivity {

    private TextInputEditText usernameInput, emailInput, passwordInput, confirmPasswordInput;
    private Button signUpBtn;
    private FirebaseAuth auth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        usernameInput = findViewById(R.id.usernameInput);
        emailInput = findViewById(R.id.emailInput);
        passwordInput = findViewById(R.id.passwordInput);
        confirmPasswordInput = findViewById(R.id.confirmPasswordInput);
        signUpBtn = findViewById(R.id.signUpBtn);
        TextView goToLogIn = findViewById(R.id.goToLogIn);

        signUpBtn.setOnClickListener(v -> attemptSignUp());
        goToLogIn.setOnClickListener(v -> {
            startActivity(new Intent(this, LogInActivity.class));
            finish();
        });
    }

    private void attemptSignUp() {
        String username = text(usernameInput);
        String email = text(emailInput);
        String password = text(passwordInput);
        String confirm = text(confirmPasswordInput);

        if (!username.matches("^[A-Za-z0-9_]{3,20}$")) {
            usernameInput.setError("3-20 characters: letters, numbers, underscores");
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailInput.setError("Enter a valid email");
            return;
        }
        if (password.length() < 6) {
            passwordInput.setError("At least 6 characters");
            return;
        }
        if (!password.equals(confirm)) {
            confirmPasswordInput.setError("Passwords don't match");
            return;
        }

        setLoading(true);
        auth.createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener(result -> saveProfile(result.getUser(), username, email))
                .addOnFailureListener(e -> {
                    setLoading(false);
                    Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
                });
    }

    /**
     * Writes users/{uid} and usernames/{lowercaseUsername} in one batch.
     * The security rules only allow *creating* a usernames doc, so if the name is
     * already taken the whole batch fails and we remove the new Auth account.
     */
    private void saveProfile(FirebaseUser user, String username, String email) {
        String uid = user.getUid();

        Map<String, Object> profile = new HashMap<>();
        profile.put("username", username);
        profile.put("email", email);

        Map<String, Object> claim = new HashMap<>();
        claim.put("uid", uid);

        WriteBatch batch = db.batch();
        batch.set(db.collection("users").document(uid), profile);
        batch.set(db.collection("usernames").document(username.toLowerCase()), claim);

        batch.commit()
                .addOnSuccessListener(unused -> {
                    Toast.makeText(this, "Account created!", Toast.LENGTH_SHORT).show();
                    goToMain();
                })
                .addOnFailureListener(e -> {
                    // Roll back so the email isn't left registered without a username
                    user.delete();
                    setLoading(false);
                    usernameInput.setError("That username is taken. Try another.");
                });
    }

    private void goToMain() {
        Intent intent = new Intent(this, HomePage.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    private void setLoading(boolean loading) {
        signUpBtn.setEnabled(!loading);
    }

    private String text(TextInputEditText field) {
        return field.getText() == null ? "" : field.getText().toString().trim();
    }
}