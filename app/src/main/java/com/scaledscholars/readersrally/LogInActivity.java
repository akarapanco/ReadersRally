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

public class LogInActivity extends AppCompatActivity {

    private TextInputEditText emailInput, passwordInput;
    private Button logInButton;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        auth = FirebaseAuth.getInstance();

        emailInput = findViewById(R.id.emailInput);
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
        String email = text(emailInput);
        String password = text(passwordInput);

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailInput.setError("Enter a valid email");
            return;
        }
        if (password.isEmpty()) {
            passwordInput.setError("Enter your password");
            return;
        }

        logInButton.setEnabled(false);
        auth.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener(result -> goToMain())
                .addOnFailureListener(e -> {
                    logInButton.setEnabled(true);
                    Toast.makeText(this, "Log in failed: check your email and password",
                            Toast.LENGTH_LONG).show();
                });
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
