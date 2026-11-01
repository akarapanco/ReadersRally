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
import com.google.firebase.firestore.FirebaseFirestore;

public class LogInActivity extends AppCompatActivity {

    private TextInputEditText identifierInput, passwordInput;
    private Button logInButton;
    private FirebaseAuth auth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        identifierInput = findViewById(R.id.identifierInput);
        passwordInput = findViewById(R.id.passwordInput);
        logInButton = findViewById(R.id.logInButton);
        TextView goToSignUp = findViewById(R.id.goToSignUp);

        logInButton.setOnClickListener(v -> attemptLogIn());
        goToSignUp.setOnClickListener(v ->
                startActivity(new Intent(this, SignUpActivity.class)));
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Firebase remembers the signed-in user, so skip this screen if already logged in
        if (auth.getCurrentUser() != null) {
            goToMain();
        }
    }

    private void attemptLogIn() {
        String identifier = text(identifierInput);
        String password = text(passwordInput);

        if (identifier.isEmpty()) {
            identifierInput.setError("Enter your email or username");
            return;
        }
        if (password.isEmpty()) {
            passwordInput.setError("Enter your password");
            return;
        }

        logInButton.setEnabled(false);

        if (Patterns.EMAIL_ADDRESS.matcher(identifier).matches()) {
            // It's an email, so sign in directly
            signIn(identifier, password);
        } else {
            // Treat it as a username: look up the email saved at sign up
            db.collection("usernames").document(identifier.toLowerCase()).get()
                    .addOnSuccessListener(doc -> {
                        String email = doc.getString("email");
                        if (email == null) {
                            showFailure();
                        } else {
                            signIn(email, password);
                        }
                    })
                    .addOnFailureListener(e -> showFailure());
        }
    }

    private void signIn(String email, String password) {
        auth.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener(result -> goToMain())
                .addOnFailureListener(e -> showFailure());
    }

    private void showFailure() {
        logInButton.setEnabled(true);
        Toast.makeText(this, "Log in failed: check your details and try again",
                Toast.LENGTH_LONG).show();
    }

    private void goToMain() {
        Intent intent = new Intent(this, HomePage.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    private String text(TextInputEditText field) {
        return field.getText() == null ? "" : field.getText().toString().trim();
    }
}