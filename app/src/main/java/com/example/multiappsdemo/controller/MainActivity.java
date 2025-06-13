package com.example.multiappsdemo.controller;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.multiappsdemo.R;
import com.example.multiappsdemo.model.AppInfo;
import com.example.multiappsdemo.model.MaintenanceActivity;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private EditText etUserName;
    private RecyclerView rvApps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etUserName = findViewById(R.id.etUserName);
        rvApps = findViewById(R.id.rvApps);

        // Liste d’applications à afficher (titre + description)
        List<AppInfo> apps = Arrays.asList(
                new AppInfo("To-Do List", "Ajouter, marquer et supprimer des tâches"),
                new AppInfo("Calculatrice simple", "Addition, soustraction, multiplication, division"),
                new AppInfo("Carnet de Contacts Simplifié", "Ajouter, voir, supprimer des contacts"),
                new AppInfo("Bloc-Notes Personnel", "Prise de notes avec titre et contenu"),
                new AppInfo("Chronomètre / Minuteur", "Chronomètre, minuteur réglable"),
                new AppInfo("Convertisseur", "Conversion unités ou devises"),
                new AppInfo("TopQuiz", "Quiz à choix multiples, scores, rappels"),
                new AppInfo("Lecteur de musique", "Lire fichiers MP3 locaux"),
                new AppInfo("Application météo", "Ville, météo et température"),
                new AppInfo("Suivi des dépenses", "Ajouter et voir ses dépenses")
        );

        rvApps.setLayoutManager(new LinearLayoutManager(this));
        AppsAdapter adapter = new AppsAdapter(apps, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = (int) view.getTag();
                String userName = etUserName.getText().toString().trim();
                if (userName.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Veuillez entrer votre nom", Toast.LENGTH_SHORT).show();
                    return;
                }
                switch (position) {
                    case 1:
                        startActivity(new Intent(MainActivity.this, CalculatriceActivity.class)
                                .putExtra("USER_NAME", userName));
                        break;
                    case 6:
                        startActivity(new Intent(MainActivity.this, TopQuizActivity.class)
                                .putExtra("USER_NAME", userName));
                        break;
                    default:
                        startActivity(new Intent(MainActivity.this, MaintenanceActivity.class)
                                .putExtra("APP_POSITION", position)
                                .putExtra("USER_NAME", userName));
                }
            }
        });
        rvApps.setAdapter(adapter);

    }
}
