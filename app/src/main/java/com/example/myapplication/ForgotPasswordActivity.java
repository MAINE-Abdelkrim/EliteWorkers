package com.example.myapplication; // Replace with your actual package name

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;


import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;

public class ForgotPasswordActivity extends AppCompatActivity {

    private EditText etEmail;
    private Button btnResetPassword;
    private ProgressBar progressBar;
    private TextInputLayout tilEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        tilEmail = findViewById(R.id.tilEmail);
        etEmail = findViewById(R.id.etEmail);
        btnResetPassword = findViewById(R.id.btnResetPassword);
        progressBar = findViewById(R.id.progressBar);

        btnResetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = etEmail.getText().toString().trim();

                if (TextUtils.isEmpty(email)) {
                    tilEmail.setError("Please enter your email");
                    return;
                } else {
                    tilEmail.setError(null); // Clear any previous error
                }

                progressBar.setVisibility(View.VISIBLE);

                // Simulate a password reset process (replace with your actual logic)
                // In a real-world scenario, you would send a request to your server
                // to handle the password reset.  This is just a placeholder.
                // Simulate a password reset process
                FirebaseAuth.getInstance().sendPasswordResetEmail(email)
                        .addOnCompleteListener(task -> {
                            progressBar.setVisibility(View.GONE);
                            if (task.isSuccessful()) {
                                Toast.makeText(ForgotPasswordActivity.this, "Password reset email sent to " + email, Toast.LENGTH_LONG).show();
                                finish(); // Go back to the login screen
                            } else {
                                Toast.makeText(ForgotPasswordActivity.this, "Failed to send reset email.", Toast.LENGTH_LONG).show();
                            }
                        });


            }
        });
    }

    //Basic Email Validation
    private boolean isValidEmail(String email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
}
