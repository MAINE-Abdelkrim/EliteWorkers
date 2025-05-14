package com.example.myapplication;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private RecyclerView recyclerView;
    private OuvrierAdapter adapter;
    private List<Ouvrier> ouvrierList = new ArrayList<>();
    private FirebaseFirestore db;

    public HomeFragment() {}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        recyclerView = view.findViewById(R.id.recyclerViewOuvriers);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new OuvrierAdapter(ouvrierList);
        recyclerView.setAdapter(adapter);

        db = FirebaseFirestore.getInstance();
        loadOuvriersFromFirestore();

        return view;
    }

    private void loadOuvriersFromFirestore() {
        db.collection("users")
                .whereEqualTo("userType", "Ouvrier")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    ouvrierList.clear();
                    for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                        String name = doc.getString("name");
                        String city = doc.getString("city");
                        String region = doc.getString("region");

                        // Assuming Ouvrier constructor: (String name, String job, String city)
                        // Replace "job" with actual Firestore field if available
                        ouvrierList.add(new Ouvrier(name, region, city));
                    }
                    adapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(getContext(), "Erreur de chargement: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    Log.e("HomeFragment", "Firestore error", e);
                });
    }
}
