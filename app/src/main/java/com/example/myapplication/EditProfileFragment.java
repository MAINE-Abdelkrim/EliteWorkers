package com.example.myapplication;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class EditProfileFragment extends Fragment {

    private EditText editName, editEmail, editPhone, editAddress;
    private Button btnSave;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @SuppressLint("MissingInflatedId")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_profile, container, false);

        editName = view.findViewById(R.id.editTextName);
        editEmail = view.findViewById(R.id.editTextEmail);
        editPhone = view.findViewById(R.id.editTextPhone);
        editAddress = view.findViewById(R.id.editTextAddress);
        btnSave = view.findViewById(R.id.btnSaveChanges);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        loadUserData();

        btnSave.setOnClickListener(v -> saveUserData());

        return view;
    }

    private void loadUserData() {
        String uid = mAuth.getCurrentUser().getUid();
        db.collection("users").document(uid).get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        if (documentSnapshot.contains("name"))
                            editName.setText(documentSnapshot.getString("name"));
                        if (documentSnapshot.contains("email"))
                            editEmail.setText(documentSnapshot.getString("email"));
                        if (documentSnapshot.contains("phone"))
                            editPhone.setText(documentSnapshot.getString("phone"));
                        if (documentSnapshot.contains("address"))
                            editAddress.setText(documentSnapshot.getString("address"));
                    }
                });
    }

    private void saveUserData() {
        String uid = mAuth.getCurrentUser().getUid();

        Map<String, Object> updates = new HashMap<>();
        updates.put("name", editName.getText().toString().trim());
        updates.put("email", editEmail.getText().toString().trim());
        updates.put("phone", editPhone.getText().toString().trim());
        updates.put("address", editAddress.getText().toString().trim());

        db.collection("users").document(uid).update(updates)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(getContext(), "Profile updated", Toast.LENGTH_SHORT).show();
                    getActivity().onBackPressed(); // Return to previous screen
                });
    }
}
