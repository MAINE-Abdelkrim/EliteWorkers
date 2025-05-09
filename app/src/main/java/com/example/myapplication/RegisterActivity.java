package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.AuthResult;
import androidx.annotation.NonNull;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;


public class RegisterActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;


    private RadioGroup rgUserType;
    private RadioButton rbJobSeeker, rbEmployer, rbCraftsman;
    private TextInputEditText etName, etPhone, etEmail, etCity, etRegion, etPassword, etConfirmPassword;
    private Button btnRegister;
    private TextView tvLoginPrompt;
    private LinearLayout llUserTypeFields;
    private LayoutInflater inflater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Initialize views
        initViews();
        inflater = LayoutInflater.from(this);

        //firebase
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();


        // Set up user type selection
        rgUserType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                updateUserTypeSpecificFields(checkedId);
            }
        });

        // Initial setup of user type fields
        updateUserTypeSpecificFields(rgUserType.getCheckedRadioButtonId());

        // Register button click listener
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateInputs()) {
                    registerUser();
                }
            }
        });

        // Login prompt click listener
        tvLoginPrompt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // Go back to login activity
            }
        });
    }

    private void initViews() {
        rgUserType = findViewById(R.id.rgUserType);
        rbJobSeeker = findViewById(R.id.rbJobSeeker);
        rbEmployer = findViewById(R.id.rbEmployer);
        rbCraftsman = findViewById(R.id.rbCraftsman);
        etName = findViewById(R.id.etName);
        etPhone = findViewById(R.id.etPhone);
        etEmail = findViewById(R.id.etEmail);
        etCity = findViewById(R.id.etCity);
        etRegion = findViewById(R.id.etRegion);
        etPassword = findViewById(R.id.etPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);
        btnRegister = findViewById(R.id.btnRegister);
        tvLoginPrompt = findViewById(R.id.tvLoginPrompt);
        llUserTypeFields = findViewById(R.id.llUserTypeFields);
    }

    private void updateUserTypeSpecificFields(int checkedId) {
        // Clear previous fields
        llUserTypeFields.removeAllViews();

        if (checkedId == R.id.rbJobSeeker) {
            // Add JobSeeker specific fields
            addJobSeekerFields();
        } else if (checkedId == R.id.rbEmployer) {
            // Add Employer specific fields
            addEmployerFields();
        } else if (checkedId == R.id.rbCraftsman) {
            // Add Craftsman specific fields
            addCraftsmanFields();
        }
    }

    private void addJobSeekerFields() {
        View jobSeekerView = inflater.inflate(R.layout.layout_job_seeker_fields, llUserTypeFields, false);
        llUserTypeFields.addView(jobSeekerView);
    }

    private void addEmployerFields() {
        View employerView = inflater.inflate(R.layout.layout_employer_fields, llUserTypeFields, false);
        llUserTypeFields.addView(employerView);
    }

    private void addCraftsmanFields() {
        View craftsmanView = inflater.inflate(R.layout.layout_craftsman_fields, llUserTypeFields, false);
        llUserTypeFields.addView(craftsmanView);
    }

    private boolean validateInputs() {
        boolean isValid = true;

        String name = etName.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String city = etCity.getText().toString().trim();
        String region = etRegion.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String confirmPassword = etConfirmPassword.getText().toString().trim();

        // Validate name
        if (TextUtils.isEmpty(name)) {
            etName.setError("Name is required");
            isValid = false;
        }

        // Validate phone
        if (TextUtils.isEmpty(phone)) {
            etPhone.setError("Phone is required");
            isValid = false;
        }

        // Validate email
        if (TextUtils.isEmpty(email)) {
            etEmail.setError("Email is required");
            isValid = false;
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail.setError("Enter a valid email address");
            isValid = false;
        }

        // Validate city
        if (TextUtils.isEmpty(city)) {
            etCity.setError("City is required");
            isValid = false;
        }

        // Validate region
        if (TextUtils.isEmpty(region)) {
            etRegion.setError("Region is required");
            isValid = false;
        }

        // Validate password
        if (TextUtils.isEmpty(password)) {
            etPassword.setError("Password is required");
            isValid = false;
        } else if (password.length() < 6) {
            etPassword.setError("Password must be at least 6 characters");
            isValid = false;
        }

        // Validate confirm password
        if (TextUtils.isEmpty(confirmPassword)) {
            etConfirmPassword.setError("Confirm password is required");
            isValid = false;
        } else if (!password.equals(confirmPassword)) {
            etConfirmPassword.setError("Passwords do not match");
            isValid = false;
        }

        // Validate user type specific fields
        if (rgUserType.getCheckedRadioButtonId() == R.id.rbJobSeeker) {
            // Validate JobSeeker fields
            isValid = validateJobSeekerFields() && isValid;
        } else if (rgUserType.getCheckedRadioButtonId() == R.id.rbEmployer) {
            // Validate Employer fields
            isValid = validateEmployerFields() && isValid;
        } else if (rgUserType.getCheckedRadioButtonId() == R.id.rbCraftsman) {
            // Validate Craftsman fields
            isValid = validateCraftsmanFields() && isValid;
        }

        return isValid;
    }

    private boolean validateJobSeekerFields() {
        // Get job seeker specific fields and validate them
        // For this template, we'll return true. In a real app, add validation logic.
        return true;
    }

    private boolean validateEmployerFields() {
        // Get employer specific fields and validate them
        // For this template, we'll return true. In a real app, add validation logic.
        return true;
    }

    private boolean validateCraftsmanFields() {
        // Get craftsman specific fields and validate them
        // For this template, we'll return true. In a real app, add validation logic.
        return true;
    }

    private void registerUser() {
        // Get user type
        String userType = "";
        if (rgUserType.getCheckedRadioButtonId() == R.id.rbJobSeeker) {
            userType = "JobSeeker";
        } else if (rgUserType.getCheckedRadioButtonId() == R.id.rbEmployer) {
            userType = "Employer";
        } else if (rgUserType.getCheckedRadioButtonId() == R.id.rbCraftsman) {
            userType = "Craftsman";
        }

        // Get common user data
        String name = etName.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String city = etCity.getText().toString().trim();
        String region = etRegion.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        String finalUserType = userType;
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            String uid = mAuth.getCurrentUser().getUid();

                            User user = new User(uid,name, phone, email, city, region, finalUserType);

                            db.collection("users").document(uid)
                                    .set(user)
                                    .addOnSuccessListener(aVoid -> {
                                        Toast.makeText(RegisterActivity.this, "Registration successful", Toast.LENGTH_SHORT).show();
                                        finish();
                                    })
                                    .addOnFailureListener(e -> {
                                        Toast.makeText(RegisterActivity.this, "Failed to save user data: " + e.getMessage(), Toast.LENGTH_LONG).show();
                                    });

                        } else {
                            Toast.makeText(RegisterActivity.this, "Registration failed: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });


    }
}