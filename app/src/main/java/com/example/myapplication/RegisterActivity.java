package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.*;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import androidx.annotation.NonNull;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.android.material.textfield.TextInputEditText;

public class RegisterActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    private RadioGroup rgUserType;
    private RadioButton rbClient, rbCraftsman;
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

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        rgUserType.setOnCheckedChangeListener((group, checkedId) -> updateUserTypeSpecificFields(checkedId));

        // Initial setup
        updateUserTypeSpecificFields(rgUserType.getCheckedRadioButtonId());

        btnRegister.setOnClickListener(v -> {
            if (validateInputs()) {
                registerUser();
            }
        });

        tvLoginPrompt.setOnClickListener(v -> finish());
    }

    private void initViews() {
        rgUserType = findViewById(R.id.rgUserType);
        rbClient = findViewById(R.id.rbClient);
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
        llUserTypeFields.removeAllViews();
        if (checkedId == R.id.rbClient) {
            addClientFields();
        } else if (checkedId == R.id.rbCraftsman) {
            addCraftsmanFields();
        }
    }

    private void addClientFields() {
        View clientView = inflater.inflate(R.layout.layout_client_fields, llUserTypeFields, false);
        llUserTypeFields.addView(clientView);
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

        if (TextUtils.isEmpty(name)) {
            etName.setError("Name is required");
            isValid = false;
        }

        if (TextUtils.isEmpty(phone)) {
            etPhone.setError("Phone is required");
            isValid = false;
        }

        if (TextUtils.isEmpty(email)) {
            etEmail.setError("Email is required");
            isValid = false;
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail.setError("Enter a valid email address");
            isValid = false;
        }

        if (TextUtils.isEmpty(city)) {
            etCity.setError("City is required");
            isValid = false;
        }

        if (TextUtils.isEmpty(region)) {
            etRegion.setError("Region is required");
            isValid = false;
        }

        if (TextUtils.isEmpty(password)) {
            etPassword.setError("Password is required");
            isValid = false;
        } else if (password.length() < 6) {
            etPassword.setError("Password must be at least 6 characters");
            isValid = false;
        }

        if (TextUtils.isEmpty(confirmPassword)) {
            etConfirmPassword.setError("Confirm password is required");
            isValid = false;
        } else if (!password.equals(confirmPassword)) {
            etConfirmPassword.setError("Passwords do not match");
            isValid = false;
        }

        int checkedId = rgUserType.getCheckedRadioButtonId();
        if (checkedId == R.id.rbClient) {
            isValid = validateClientFields() && isValid;
        } else if (checkedId == R.id.rbCraftsman) {
            isValid = validateCraftsmanFields() && isValid;
        }

        return isValid;
    }

    private boolean validateClientFields() {
        // Add validation for client-specific fields if needed
        return true;
    }

    private boolean validateCraftsmanFields() {
        // Add validation for craftsman-specific fields if needed
        return true;
    }

    private void registerUser() {
        String userType = "";
        int checkedId = rgUserType.getCheckedRadioButtonId();
        if (checkedId == R.id.rbClient) {
            userType = "Client";
        } else if (checkedId == R.id.rbCraftsman) {
            userType = "Ouvrier";
        }

        String name = etName.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String city = etCity.getText().toString().trim();
        String region = etRegion.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        String finalUserType = userType;

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        String uid = mAuth.getCurrentUser().getUid();
                        User user = new User(uid, name, phone, email, city, region, finalUserType);

                        db.collection("users").document(uid)
                                .set(user)
                                .addOnSuccessListener(aVoid ->
                                        Toast.makeText(RegisterActivity.this, "Registration successful", Toast.LENGTH_SHORT).show())
                                .addOnFailureListener(e ->
                                        Toast.makeText(RegisterActivity.this, "Failed to save user data: " + e.getMessage(), Toast.LENGTH_LONG).show());

                        finish();
                    } else {
                        Toast.makeText(RegisterActivity.this, "Registration failed: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }
}
