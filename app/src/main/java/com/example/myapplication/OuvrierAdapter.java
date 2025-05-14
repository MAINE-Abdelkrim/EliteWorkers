package com.example.myapplication;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Ouvrier;

import java.util.List;

public class OuvrierAdapter extends RecyclerView.Adapter<OuvrierAdapter.OuvrierViewHolder> {

    private List<Ouvrier> ouvrierList;

    public OuvrierAdapter(List<Ouvrier> ouvrierList) {
        this.ouvrierList = ouvrierList;
    }

    @NonNull
    @Override
    public OuvrierViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ouvrier, parent, false);
        return new OuvrierViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OuvrierViewHolder holder, int position) {
        Ouvrier ouvrier = ouvrierList.get(position);
        holder.nom.setText(ouvrier.getNom());
        holder.metier.setText("Métier : " + ouvrier.getMetier());
        holder.ville.setText("Ville : " + ouvrier.getVille());
    }

    @Override
    public int getItemCount() {
        return ouvrierList.size();
    }

    static class OuvrierViewHolder extends RecyclerView.ViewHolder {
        TextView nom, metier, ville;

        public OuvrierViewHolder(@NonNull View itemView) {
            super(itemView);
            nom = itemView.findViewById(R.id.nom_ouvrier);
            metier = itemView.findViewById(R.id.metier_ouvrier);
            ville = itemView.findViewById(R.id.ville_ouvrier);
        }
    }
}
