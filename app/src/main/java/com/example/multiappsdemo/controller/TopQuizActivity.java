package com.example.multiappsdemo.controller;

import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.TextView;
import android.widget.EditText;
import android.widget.Button;
import android.widget.Toast;
import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;

import com.example.multiappsdemo.R;
import com.example.multiappsdemo.model.User;

public class TopQuizActivity extends AppCompatActivity {
    private TextView mGreetingTextView;
    private EditText mNameEditText;
    private Button mPlayButton;
    private User mUser;


    private static final String SHARED_PREF_USER_INFO = "SHARED_PREF_USER_INFO";
    private static final String SHARED_PREF_USER_INFO_NAME = "SHARED_PREF_USER_INFO_NAME";
    private static final String SHARED_PREF_USER_INFO_SCORE = "SHARED_PREF_USER_INFO_SCORE";
    private static final int GAME_ACTIVITY_REQUEST_CODE = 42;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top_quiz);

        // Liaison des widgets à leur ID dans le layout XML
        mGreetingTextView = findViewById(R.id.main_textview_greeting);
        mNameEditText = findViewById(R.id.main_edittext_name);
        mPlayButton = findViewById(R.id.main_button_play);

        // Lecture des SharedPreferences
        SharedPreferences preferences = getSharedPreferences(SHARED_PREF_USER_INFO, MODE_PRIVATE);
        String firstName = preferences.getString(SHARED_PREF_USER_INFO_NAME, null);
        int lastScore = preferences.getInt(SHARED_PREF_USER_INFO_SCORE, -1);

        // Personnalisation du message d'accueil et du champ de saisie
        if (firstName != null && lastScore != -1) {
            // Message personnalisé avec le score
            mGreetingTextView.setText("Welcome back, " + firstName + "!\nYour last score was " + lastScore + ", will you do better this time?");
            // Prénom pré-rempli, curseur à la fin
            mNameEditText.setText(firstName);
            mNameEditText.setSelection(firstName.length());
            mPlayButton.setEnabled(true);
        } else {
            mGreetingTextView.setText("Welcome to TopQuiz! What is your first name?");
            mPlayButton.setEnabled(false);
        }

        // Listener d'activation du bouton
        mNameEditText.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override public void afterTextChanged(Editable s) {
                mPlayButton.setEnabled(!s.toString().isEmpty());
            }
        });

        // Désactivation du bouton tant que le prénom n'est pas saisi
        mPlayButton.setEnabled(false);

        // Surveille la saisie de texte
        mNameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                // Activation du bouton si au moins un caractère
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
                    mPlayButton.setEnabled(!s.toString().isEmpty());
                }
            }
        });

        // Gère le clic sur le bouton
        mPlayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Affiche un message de bienvenue avec le prénom saisi
                Toast.makeText(TopQuizActivity.this,
                        "Bienvenue, " + mNameEditText.getText().toString() + " !",
                        Toast.LENGTH_SHORT).show();

                // Lancer la nouvelle activité GameActivity
                Intent gameActivityIntent = new Intent(TopQuizActivity.this, GameActivity.class);
                gameActivityIntent.putExtra("USERNAME", mNameEditText.getText().toString());
                startActivityForResult(gameActivityIntent, GAME_ACTIVITY_REQUEST_CODE);

            }
        });


        //
        mUser = new User();
        mUser.setFirstName(mNameEditText.getText().toString());


    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GAME_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            // Récupération du score
            int score = data.getIntExtra(GameActivity.BUNDLE_EXTRA_SCORE, 0);
            String firstName = mNameEditText.getText().toString();

            // Enregistrement dans SharedPreferences
            getSharedPreferences(SHARED_PREF_USER_INFO, MODE_PRIVATE)
                    .edit()
                    .putString(SHARED_PREF_USER_INFO_NAME, firstName)
                    .putInt(SHARED_PREF_USER_INFO_SCORE, score)
                    .apply();

            // Mise à jour du message d'accueil (immédiate, sans recharger l'activité)
            mGreetingTextView.setText("Welcome back, " + firstName + "!\nYour last score was " + score + ", will you do better this time?");
        }
    }


}
