package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.AuthResult;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;

public class LoginActivity extends AppCompatActivity {

    private TextInputEditText etEmail, etPassword;
    private Button btnLogin, btnRegister;
    private TextView tvForgotPassword ,tvRegisterPrompt ;

    private FirebaseAuth mAuth; // Firebase Auth instance

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        tvRegisterPrompt = findViewById(R.id.tvRegisterPrompt);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        btnRegister = findViewById(R.id.btnRegister);
        tvForgotPassword = findViewById(R.id.tvForgotPassword);

        // Initialize UI components
        final int[] tapCount = {0};
        final long[] lastTapTime = {0};
        tvRegisterPrompt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long currentTime = System.currentTimeMillis();

                // Si le temps entre deux taps est supérieur à 2 secondes, on reset
                if (currentTime - lastTapTime[0] > 2000) {
                    tapCount[0] = 0;
                }

                tapCount[0]++;
                lastTapTime[0] = currentTime;

                if (tapCount[0] == 5) {
                    tapCount[0] = 0;
                    Toast.makeText(LoginActivity.this, "Accès admin détecté", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                    startActivity(intent);
                }
            }
        });


        // Login button click listener
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = etEmail.getText().toString().trim();
                String password = etPassword.getText().toString().trim();

                if (validateInputs(email, password)) {
                    performLogin(email, password);
                }
            }
        });

        // Register button click listener
        btnRegister.setOnClickListener(v -> {
            startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
        });

        // Forgot password click listener
        tvForgotPassword.setOnClickListener(v -> {
            startActivity(new Intent(LoginActivity.this, ForgotPasswordActivity.class));
        });
    }

    private boolean validateInputs(String email, String password) {
        if (TextUtils.isEmpty(email)) {
            etEmail.setError("Email is required");
            etEmail.requestFocus();
            return false;
        }

        if (TextUtils.isEmpty(password)) {
            etPassword.setError("Password is required");
            etPassword.requestFocus();
            return false;
        }

        return true;
    }

    private void performLogin(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            String uid = mAuth.getCurrentUser().getUid();

                            FirebaseFirestore.getInstance()
                                    .collection("users")
                                    .document(uid)
                                    .get()
                                    .addOnSuccessListener(documentSnapshot -> {
                                        if (documentSnapshot.exists()) {
                                            String userType = documentSnapshot.getString("userType");

                                            switch (userType) {
                                                case "Client":
                                                    Toast.makeText(LoginActivity.this, "Bienvenue Client!", Toast.LENGTH_SHORT).show();
                                                    // Optional: redirect to ClientHomeActivity
                                                    break;
                                                case "Ouvrier":
                                                    Toast.makeText(LoginActivity.this, "Bienvenue Ouvrier!", Toast.LENGTH_SHORT).show();
                                                    // Optional: redirect to OuvrierHomeActivity
                                                    break;
                                                case "Admin":
                                                    Toast.makeText(LoginActivity.this, "Bienvenue Admin!", Toast.LENGTH_SHORT).show();
                                                    // Optional: redirect to AdminDashboardActivity
                                                    break;
                                                default:
                                                    Toast.makeText(LoginActivity.this, "Type d'utilisateur inconnu", Toast.LENGTH_SHORT).show();
                                                    return;
                                            }

                                            // Redirect to main app (adjust this if you want user-type-specific screens)
                                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                            startActivity(intent);
                                            finish();
                                        } else {
                                            Toast.makeText(LoginActivity.this, "Aucune donnée utilisateur trouvée.", Toast.LENGTH_LONG).show();
                                        }
                                    })
                                    .addOnFailureListener(e -> {
                                        Toast.makeText(LoginActivity.this, "Erreur Firestore : " + e.getMessage(), Toast.LENGTH_LONG).show();
                                    });

                        } else {
                            Toast.makeText(LoginActivity.this, "Échec de connexion : " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }


}
