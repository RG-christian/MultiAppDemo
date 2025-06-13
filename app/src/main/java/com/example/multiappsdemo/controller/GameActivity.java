package com.example.multiappsdemo.controller;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AlertDialog;

import com.example.multiappsdemo.R;
import com.example.multiappsdemo.model.Question;
import com.example.multiappsdemo.model.QuestionBank;

import java.util.Arrays;

public class GameActivity extends AppCompatActivity implements View.OnClickListener {

    // Déclaration des widgets de l’interface
    private TextView mNameTextView;
    private TextView mQuestionTextView;
    private Button mAnswer1Button;
    private Button mAnswer2Button;
    private Button mAnswer3Button;
    private Button mAnswer4Button;

    // Le modèle (banque de questions)
    private final QuestionBank mQuestionBank = generateQuestions();
    public static final String BUNDLE_EXTRA_SCORE = "BUNDLE_EXTRA_SCORE";

    // Variables de logique de jeu
    private int mScore;                  // Score du joueur
    private int mRemainingQuestionCount; // Nombre de questions restantes
    private Question mCurrentQuestion;   // Question courante

    private boolean mEnableTouchEvents = true;
    public static final String BUNDLE_STATE_SCORE = "BUNDLE_STATE_SCORE";
    public static final String BUNDLE_STATE_QUESTION = "BUNDLE_STATE_QUESTION";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        // Initialisation  de EnableTouchEvents
        mEnableTouchEvents = true;

        // Branchement des widgets avec leur ID respectif
        mNameTextView = findViewById(R.id.game_activity_textview_name);
        mQuestionTextView = findViewById(R.id.game_activity_textview_question);
        mAnswer1Button = findViewById(R.id.game_activity_button_1);
        mAnswer2Button = findViewById(R.id.game_activity_button_2);
        mAnswer3Button = findViewById(R.id.game_activity_button_3);
        mAnswer4Button = findViewById(R.id.game_activity_button_4);

        // Récupération et affichage du prénom transmis depuis MainActivity
        String firstName = getIntent().getStringExtra("USERNAME");
        mNameTextView.setText("Bonne chance, " + firstName + " !");

        // Déclaration des listeners sur les 4 boutons de réponse
        mAnswer1Button.setOnClickListener(this);
        mAnswer2Button.setOnClickListener(this);
        mAnswer3Button.setOnClickListener(this);
        mAnswer4Button.setOnClickListener(this);

        if (savedInstanceState != null) {
            // Restaure le score et la progression
            mScore = savedInstanceState.getInt(BUNDLE_STATE_SCORE);
            mRemainingQuestionCount = savedInstanceState.getInt(BUNDLE_STATE_QUESTION);
        } else {
            // Initialisation du score et du nombre de questions de la partie
            mScore = 0;
            mRemainingQuestionCount = 20; // Mets ici le nombre de questions à jouer (doit être ≤ au nombre de questions dans la banque)
        }


        mCurrentQuestion = mQuestionBank.getCurrentQuestion();

        // Affiche la première question au lancement de l’activité
        displayQuestion(mCurrentQuestion);
    }

    /**
     * Affiche la question courante et met à jour le texte des boutons de réponse
     */
    private void displayQuestion(Question question) {
        mQuestionTextView.setText(question.getQuestion());
        mAnswer1Button.setText(question.getChoiceList().get(0));
        mAnswer2Button.setText(question.getChoiceList().get(1));
        mAnswer3Button.setText(question.getChoiceList().get(2));
        mAnswer4Button.setText(question.getChoiceList().get(3));
    }

    /**
     * Gère le clic sur l’un des boutons de réponse
     */
    @Override
    public void onClick(View view) {
        int index;
        if (view == mAnswer1Button) {
            index = 0;
        } else if (view == mAnswer2Button) {
            index = 1;
        } else if (view == mAnswer3Button) {
            index = 2;
        } else if (view == mAnswer4Button) {
            index = 3;
        } else {
            throw new IllegalStateException("Unknown clicked view : " + view);
        }

        // Vérifie la réponse et met à jour le score
        if (index == mCurrentQuestion.getAnswerIndex()) {
            Toast.makeText(this, "Correct!", Toast.LENGTH_SHORT).show();
            mScore++;
        } else {
            Toast.makeText(this, "Incorrect!", Toast.LENGTH_SHORT).show();
        }

        // Désactive temporairement les interactions utilisateur
        mEnableTouchEvents = false;

        // Temporisation de 2 secondes avant d’afficher la question suivante ou le score
        new android.os.Handler(getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                mEnableTouchEvents = true; // Réactive les interactions
                mRemainingQuestionCount--;

                if (mRemainingQuestionCount <= 0) {
                    endGame();
                } else {
                    mCurrentQuestion = mQuestionBank.getNextQuestion();
                    displayQuestion(mCurrentQuestion);
                }
            }
        }, 2_000); // 2_000 ms = 2 secondes
    }


    /**
     * Affiche la boîte de dialogue de fin de partie avec le score
     */
    private void endGame() {
        // Prépare l’intent de résultat
        Intent intent = new Intent();
        intent.putExtra(BUNDLE_EXTRA_SCORE, mScore);
        setResult(RESULT_OK, intent);
        // Affiche la boîte de dialogue et ferme l’activité au clic sur OK
        new androidx.appcompat.app.AlertDialog.Builder(this)
                .setTitle("Well done!")
                .setMessage("Your score is " + mScore)
                .setPositiveButton("OK", (dialog, which) -> finish())
                .setCancelable(false)
                .create()
                .show();
    }


    /**
     * Génère la banque de questions (modèle)
     */
    private QuestionBank generateQuestions() {
        Question q1 = new Question(
                "Quel langage est interprété par le navigateur pour afficher des pages web dynamiques ?",
                Arrays.asList("Java", "C#", "JavaScript", "Python"),
                2
                // JavaScript est le langage de script des navigateurs web pour l’interactivité côté client.
        );
        // Réponse : JavaScript (2)

        Question q2 = new Question(
                "Quel framework est le plus utilisé pour développer des applications web front-end en 2024 ?",
                Arrays.asList("Django", "React", "Spring", "Laravel"),
                1
                // React est le framework front-end JavaScript le plus populaire actuellement.
        );
        // Réponse : React (1)

        Question q3 = new Question(
                "Quel langage est utilisé pour structurer le contenu d'une page web ?",
                Arrays.asList("CSS", "JavaScript", "HTML", "PHP"),
                2
                // HTML structure le contenu, CSS le met en forme.
        );
        // Réponse : HTML (2)

        Question q4 = new Question(
                "Lequel de ces frameworks est principalement backend ?",
                Arrays.asList("Vue.js", "Angular", "Express.js", "Svelte"),
                2
                // Express.js est un framework Node.js côté serveur.
        );
        // Réponse : Express.js (2)

        Question q5 = new Question(
                "Quel langage permet de styliser les éléments HTML ?",
                Arrays.asList("SQL", "CSS", "Python", "Bash"),
                1
                // CSS sert à la mise en forme des pages web.
        );
        // Réponse : CSS (1)

        Question q6 = new Question(
                "Quel est le rôle principal du framework Django ?",
                Arrays.asList("Développement mobile", "Développement web backend", "Design graphique", "Animation 3D"),
                1
                // Django est un framework Python pour le backend web.
        );
        // Réponse : Développement web backend (1)

        Question q7 = new Question(
                "Quel langage de programmation est principalement utilisé avec le framework Laravel ?",
                Arrays.asList("Python", "PHP", "Ruby", "Java"),
                1
                // Laravel est un framework PHP.
        );
        // Réponse : PHP (1)

        Question q8 = new Question(
                "Quel est le langage de base utilisé pour interroger une base de données relationnelle ?",
                Arrays.asList("SQL", "Java", "XML", "TypeScript"),
                0
                // SQL (Structured Query Language) sert à manipuler les bases de données relationnelles.
        );
        // Réponse : SQL (0)

        Question q9 = new Question(
                "Quel framework JavaScript est basé sur le concept de composants réutilisables ?",
                Arrays.asList("React", "jQuery", "Bootstrap", "Flask"),
                0
                // React propose un modèle basé sur les composants.
        );
        // Réponse : React (0)

        Question q10 = new Question(
                "Lequel de ces langages est typé statiquement ?",
                Arrays.asList("JavaScript", "PHP", "TypeScript", "Ruby"),
                2
                // TypeScript ajoute le typage statique à JavaScript.
        );
        // Réponse : TypeScript (2)

        Question q11 = new Question(
                "Quel framework permet de créer des applications web côté client en utilisant TypeScript ?",
                Arrays.asList("Angular", "Symfony", "Express.js", "Laravel"),
                0
                // Angular est conçu pour fonctionner avec TypeScript.
        );
        // Réponse : Angular (0)

        Question q12 = new Question(
                "Quel standard permet d'échanger des données légères entre client et serveur ?",
                Arrays.asList("JSON", "JPEG", "PNG", "MP3"),
                0
                // JSON (JavaScript Object Notation) est le format d’échange de données web.
        );
        // Réponse : JSON (0)

        Question q13 = new Question(
                "Quel protocole est utilisé pour sécuriser les échanges web ?",
                Arrays.asList("HTTP", "HTTPS", "FTP", "SMTP"),
                1
                // HTTPS ajoute la sécurité SSL/TLS à HTTP.
        );
        // Réponse : HTTPS (1)

        Question q14 = new Question(
                "Quel framework est utilisé pour développer des applications web en Python ?",
                Arrays.asList("Flask", "Spring", "Vue.js", "React"),
                0
                // Flask est un micro-framework web Python.
        );
        // Réponse : Flask (0)

        Question q15 = new Question(
                "Quel langage est compilé côté serveur avant d'être envoyé au navigateur ?",
                Arrays.asList("PHP", "HTML", "CSS", "JSON"),
                0
                // Le code PHP est exécuté côté serveur, le résultat est envoyé au navigateur.
        );
        // Réponse : PHP (0)

        Question q16 = new Question(
                "Quel framework front-end utilise JSX pour décrire l’interface utilisateur ?",
                Arrays.asList("Angular", "Vue.js", "React", "Svelte"),
                2
                // React utilise JSX, une syntaxe qui mélange JavaScript et HTML.
        );
        // Réponse : React (2)

        Question q17 = new Question(
                "Quel langage est utilisé pour décrire la structure des données dans une API RESTful ?",
                Arrays.asList("HTML", "JSON", "CSS", "SVG"),
                1
                // JSON est le format standard pour les API RESTful.
        );
        // Réponse : JSON (1)

        // ---- Ajout de 3 questions supplémentaires ----

        Question q18 = new Question(
                "Quel langage permet d’écrire des feuilles de style dynamiques grâce à des variables et des fonctions ?",
                Arrays.asList("CSS", "Sass", "HTML", "SQL"),
                1
                // Sass (ou SCSS) permet d’utiliser des variables, mixins et fonctions dans les feuilles de style.
        );
        // Réponse : Sass (1)

        Question q19 = new Question(
                "Quel framework Node.js est utilisé pour créer des applications web et des APIs REST rapidement ?",
                Arrays.asList("Express.js", "Rails", "Angular", "Django"),
                0
                // Express.js est le framework minimaliste le plus utilisé dans l’écosystème Node.js.
        );
        // Réponse : Express.js (0)

        Question q20 = new Question(
                "Quel outil de gestion de version est indispensable au travail collaboratif en développement web ?",
                Arrays.asList("npm", "Webpack", "Git", "MySQL"),
                2
                // Git permet de gérer l’historique du code source, les branches, et le travail en équipe.
        );
        // Réponse : Git (2)

        return new QuestionBank(Arrays.asList(
                q1, q2, q3, q4, q5, q6, q7, q8, q9, q10,
                q11, q12, q13, q14, q15, q16, q17, q18, q19, q20
        ));
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return mEnableTouchEvents && super.dispatchTouchEvent(ev);
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(BUNDLE_STATE_SCORE, mScore);
        outState.putInt(BUNDLE_STATE_QUESTION, mRemainingQuestionCount);
    }


}
