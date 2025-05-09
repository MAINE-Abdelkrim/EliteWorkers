package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class ProfileFragment extends Fragment {

    private Button logoutButton,  btnModifyProfile;;
    private TextView tvName, tvPhone, tvEmail, tvCity, tvRegion, tvUserType;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        // Initialize Firebase Auth and Firestore
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // Initialize TextViews
        tvName = view.findViewById(R.id.textViewName);
        tvPhone = view.findViewById(R.id.textViewPhone);
        tvEmail = view.findViewById(R.id.textViewEmail);
        tvCity = view.findViewById(R.id.textViewCity);
        tvRegion = view.findViewById(R.id.textViewRegion);
        tvUserType = view.findViewById(R.id.textViewUserType);

        // Fetch user data
        fetchUserData();

        logoutButton = view.findViewById(R.id.logoutButton);
         btnModifyProfile =view.findViewById(R.id.modifyButton);
        btnModifyProfile.setOnClickListener(v -> {
            FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, new EditProfileFragment());
            transaction.addToBackStack(null);
            transaction.commit();
        });

        logoutButton.setOnClickListener(v -> logoutUser());

        return view;
    }

    private void fetchUserData() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            String uid = currentUser.getUid();
            db.collection("users").document(uid)
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document != null && document.exists()) {
                                User user = document.toObject(User.class);
                                if (user != null) {
                                    tvName.setText("Name: " + user.getName());
                                    tvPhone.setText("Phone: " + user.getPhone());
                                    tvEmail.setText("Email: " + user.getEmail());
                                    tvCity.setText("City: " + user.getCity());
                                    tvRegion.setText("Region: " + user.getRegion());
                                    tvUserType.setText("User Type: " + user.getUserType());
                                } else {
                                    Log.d("ProfileFragment", "Error converting document to User object");
                                    // Optionally display an error message to the user
                                }
                            } else {
                                Log.d("ProfileFragment", "No such document");
                                // Optionally display a "no data found" message to the user
                            }
                        } else {
                            Log.d("ProfileFragment", "Fetch failed with: ", task.getException());
                            // Optionally display an error message to the user
                        }
                    });
        }
    }

    private void logoutUser() {
        FirebaseAuth.getInstance().signOut();
        clearUserSession();
        Intent intent = new Intent(getActivity(), LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        if (getActivity() != null) {
            getActivity().finish();
        }
    }

    private void clearUserSession() {
        // Implement your logic to clear SharedPreferences or other session data if needed
        // Example using SharedPreferences:
        /*
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("user_session", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
        */
    }
}