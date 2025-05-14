package com.example.myapplication;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Ouvrier;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    public HomeFragment() {}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.recyclerViewOuvriers);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        List<Ouvrier> ouvrierList = new ArrayList<>();
        ouvrierList.add(new Ouvrier("Ahmed", "Plombier", "Casablanca"));
        ouvrierList.add(new Ouvrier("Youssef", "Électricien", "Rabat"));
        ouvrierList.add(new Ouvrier("Khadija", "Peintre", "Fès"));
        ouvrierList.add(new Ouvrier("Hassan", "Maçon", "Tanger"));

        OuvrierAdapter adapter = new OuvrierAdapter(ouvrierList);
        recyclerView.setAdapter(adapter);


        return view;
    }
}
