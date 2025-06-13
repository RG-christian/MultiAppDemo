package com.example.multiappsdemo.controller;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import java.text.DecimalFormat;
import android.graphics.Color;

import android.os.Vibrator;
import android.content.Context;

import com.example.multiappsdemo.R;

public class CalculatriceActivity extends AppCompatActivity {

    // Affichage
    private TextView tvResult;

    // Variables
    private double val1 = Double.NaN;
    private double val2;
    private String operator = "";
    private boolean isOperatorClicked = false;
    private boolean isOn = true;

    private boolean hasError = false;
    private TextView tvHistory;

    // Boutons
    private Button btnOn;
    private Button btnOff;
    private Button btnAC;
    private Button btn7;
    private Button btn8;
    private Button btn9;
    private Button btnMul;
    private Button btn4, btn5, btn6, btnMinus, btn1, btn2, btn3, btnPlus;
    private Button btnDel, btn0, btnDot, btnEq;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculatrice);

        // Affiche la flèche de retour dans la barre du haut
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }


        // Liaison XML <-> Java
        tvResult = findViewById(R.id.tvResult);

        btnOn    = findViewById(R.id.btnOn);
        btnOff   = findViewById(R.id.btnOff);
        btnAC    = findViewById(R.id.btnAC);
        Button btnDiv = findViewById(R.id.btnDiv);
        btn7     = findViewById(R.id.btn7);
        btn8     = findViewById(R.id.btn8);
        btn9     = findViewById(R.id.btn9);
        btnMul   = findViewById(R.id.btnMul);
        btn4     = findViewById(R.id.btn4);
        btn5     = findViewById(R.id.btn5);
        btn6     = findViewById(R.id.btn6);
        btnMinus = findViewById(R.id.btnMinus);
        btn1     = findViewById(R.id.btn1);
        btn2     = findViewById(R.id.btn2);
        btn3     = findViewById(R.id.btn3);
        btnPlus  = findViewById(R.id.btnPlus);
        btnDel   = findViewById(R.id.btnDel);
        btn0     = findViewById(R.id.btn0);
        btnDot   = findViewById(R.id.btnDot);
        btnEq    = findViewById(R.id.btnEq);
        tvHistory = findViewById(R.id.tvHistory);


        // Listener chiffres et virgule
        View.OnClickListener numberClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isOn) return;
                Button b = (Button) v;
                String value = b.getText().toString();
                String current = tvResult.getText().toString();
                if (isOperatorClicked || current.equals("0") || current.equals("Erreur")) {
                    current = "";
                    isOperatorClicked = false;
                }
                // Empêcher deux virgules dans le même nombre
                if (value.equals(".") && current.contains(".")) return;
                tvResult.setText(current + value);
            }
        };

        btn0.setOnClickListener(numberClickListener);
        btn1.setOnClickListener(numberClickListener);
        btn2.setOnClickListener(numberClickListener);
        btn3.setOnClickListener(numberClickListener);
        btn4.setOnClickListener(numberClickListener);
        btn5.setOnClickListener(numberClickListener);
        btn6.setOnClickListener(numberClickListener);
        btn7.setOnClickListener(numberClickListener);
        btn8.setOnClickListener(numberClickListener);
        btn9.setOnClickListener(numberClickListener);
        btnDot.setOnClickListener(numberClickListener);

        // Listener opérateurs
        View.OnClickListener operatorClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isOn) return;
                String current = tvResult.getText().toString();
                try {
                    val1 = Double.parseDouble(current);
                } catch (Exception e) {
                    val1 = 0;
                }
                Button b = (Button) v;
                operator = b.getText().toString();
                isOperatorClicked = true;
            }
        };

        btnPlus.setOnClickListener(operatorClickListener);
        btnMinus.setOnClickListener(operatorClickListener);
        btnMul.setOnClickListener(operatorClickListener);
        btnDiv.setOnClickListener(operatorClickListener);

        // Egal
        btnEq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Vibrator V = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                if (V != null) V.vibrate(25);

                if (!isOn || !btnEq.isEnabled()) return;
                animateButton(btnEq);
                String current = tvResult.getText().toString();
                try {
                    val2 = Double.parseDouble(current);
                    double res = 0;
                    boolean error = false;
                    String operation = ""; // <-- On construit après

                    switch (operator) {
                        case "+":
                            res = val1 + val2;
                            break;
                        case "-":
                            res = val1 - val2;
                            break;
                        case "X":
                        case "x":
                        case "*":
                            res = val1 * val2;
                            break;
                        case "/":
                            if (val2 == 0) error = true;
                            else res = val1 / val2;
                            break;
                        default:
                            res = val2;
                    }

                    if (error) {
                        tvResult.setText("Erreur");
                        tvResult.setTextColor(Color.RED);
                        btnEq.setEnabled(false);
                        btnEq.setAlpha(0.5f);
                        hasError = true;
                        operation = val1 + " " + operator + " " + val2 + " = Erreur";
                    } else {
                        DecimalFormat format = new DecimalFormat("0.##");
                        tvResult.setText(format.format(res));
                        tvResult.setTextColor(Color.WHITE);
                        btnEq.setEnabled(true);
                        btnEq.setAlpha(1f);
                        hasError = false;
                        operation = val1 + " " + operator + " " + val2 + " = " + format.format(res);
                    }
                    tvHistory.setText(operation);

                    val1 = res;
                    isOperatorClicked = true;
                } catch (Exception e) {
                    tvResult.setText("Erreur");
                }
            }
        });


        // AC (clear)
        btnAC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isOn) return;
                animateButton(btnAC);
                tvResult.setText("0");
                tvResult.setTextColor(Color.WHITE);
                val1 = Double.NaN;
                val2 = 0;
                operator = "";
                btnEq.setEnabled(true);
                btnEq.setAlpha(1f);
                hasError = false;
                isOperatorClicked = false;
            }
        });



        // DEL (supprimer dernier caractère)
        btnDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isOn) return;
                animateButton(btnDel);
                String current = tvResult.getText().toString();
                if (current.length() > 1 && !current.equals("Erreur")) {
                    tvResult.setText(current.substring(0, current.length() - 1));
                } else {
                    tvResult.setText("0");
                }
            }
        });


        // ON (active la calculatrice)
        btnOn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animateButton(btnOn);
                isOn = true;
                tvResult.setText("0");
                tvResult.setTextColor(Color.WHITE);
                btnEq.setEnabled(true);
                btnEq.setAlpha(1f);
                hasError = false;
            }
        });


        // OFF (désactive l'écran)
        btnOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animateButton(btnOff);
                isOn = false;
                tvResult.setText("");
            }
        });

    }
    private void animateButton(View button) {
        button.setAlpha(0.6f); // Rends le bouton plus pâle
        button.postDelayed(() -> button.setAlpha(1f), 100); // Restaure l'opacité après 100ms
    }
    @Override
    public void onBackPressed() {
        // Affiche un message de confirmation, ou ferme l’activité
        super.onBackPressed(); // (Pour quitter normalement)
        // Ou affiche un Toast :
        // Toast.makeText(this, "Retour au menu", Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }



}