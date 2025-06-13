package com.example.multiappsdemo.model;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.multiappsdemo.R;

public class MaintenanceActivity extends AppCompatActivity {

    private TextView tvAppDescription;

    // Descriptions pour chaque application
    private String[] descriptions = {
            "Ajouter une tâche, marquer achevée, supprimer une tâche.",
            "Addition, soustraction, multiplication, division.",
            "Ajouter/voir/supprimer un contact.",
            "Prendre et sauvegarder des notes.",
            "Chronomètre et minuteur réglable.",
            "Convertir devises ou unités.",
            "Quiz à choix multiples avec score.",
            "Lire des fichiers MP3 locaux.",
            "Météo de la ville choisie.",
            "Ajouter, totaliser, catégoriser les dépenses."
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maintenance);

        tvAppDescription = findViewById(R.id.tvAppDescription);

        int position = getIntent().getIntExtra("APP_POSITION", 0);
        String userName = getIntent().getStringExtra("USER_NAME");

        String desc = "Bonjour " + userName + ",\n\nFonctionnalités prévues :\n" +
                descriptions[position] + "\n\n" +
                "L’application est en cours de développement.\nMerci de votre patience !";

        tvAppDescription.setText(desc);
    }
}

