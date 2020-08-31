package com.basbaer.runcleconnect;


import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class LogInActivity extends AppCompatActivity implements View.OnKeyListener, View.OnClickListener {

    //Feld in dem der User seinen Username eingibt
    EditText usernameEditText;

    //Feld in dem der User sein Passwort eingibt
    EditText passwordEditText;

    //Log-In bzw. Sign-Up Button (je nach Modus)
    Button logInButton;

    //aktueller User
    ParseUser currentUser;

    //Feld in der der User sein Passwort wiederholen muss, falls er upsignen möchte
    EditText repeatPassword;

    //speichert, welcher Modus aktiv ist (Login-Modus oder SignUp-Modus) und welcher user eingeloggt ist
    public static SharedPreferences sharedPreferences;
    //TextView, die mit einem Click darauf ändert, in welchem Modus man sich befindet
    TextView changeModeTextView;

    public static void updateSharedPreferences(boolean loggedIn, String username, String password) {
        sharedPreferences.edit().putBoolean("userLoggedIn", loggedIn).apply();
        sharedPreferences.edit().putString("username", username).apply();
        sharedPreferences.edit().putString("userPassword", password).apply();

    }


    //Methode, die vom Button aufgerufen wird (entspricht je nach Modus dem Sign-Up-Button oder dem
    //Log-In Button
    public void logIn(View view) {
        //schaut, ob er der user sich einloggen oder upsignen möchte
        if (!sharedPreferences.getBoolean("LogInModeActive", false)) {

            //checkt, ob die beiden Passwörter übereinstimmen
            if (repeatPassword.getText().toString().equals(passwordEditText.getText().toString())) {

                //erstellt auf ParseServer einen neuen User
                currentUser = new ParseUser();

                //setzt für diesen User Username und Passwort
                currentUser.setUsername(String.valueOf(usernameEditText.getText()));

                currentUser.setPassword(String.valueOf(passwordEditText.getText()));

                //Sign ihn im Hintergrund ab
                currentUser.signUpInBackground(new SignUpCallback() {
                    @Override
                    public void done(ParseException e) {

                        //checkt, ob alles geklappt hat
                        if (e == null) {

                            Log.i("SignUp", "Successful");

                            //user stays logged in
                            updateSharedPreferences(true, usernameEditText.getText().toString(), passwordEditText.getText().toString());

                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);

                            startActivity(intent);

                        } else {
                            //zeigt dem User an, was falsch gelaufen ist

                            if (e.getCode() == -1) {
                                Toast.makeText(getApplicationContext(), "Username and Password can't be empty", Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                            }

                        }

                    }
                });

            } else {

                Toast.makeText(getApplicationContext(), "Passwords do not match", Toast.LENGTH_LONG).show();

            }

        } else {

            //Wenn er sich einloggen möchte
            ParseUser.logInInBackground(usernameEditText.getText().toString(), passwordEditText.getText().toString(), new LogInCallback() {
                @Override
                public void done(ParseUser user, ParseException e) {

                    if (user != null) {
                        Log.i("LogIn", "Successful");

                        //user stays logged in
                        updateSharedPreferences(true, usernameEditText.getText().toString(), passwordEditText.getText().toString());

                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);

                        startActivity(intent);

                    } else {

                        Log.i("LogIn", "Failed: " + e.toString());

                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG);
                    }

                }
            });
        }

    }

    //wird ausgeführt, wenn auf die TextView geklickt wird, die den Modus ändert
    public void changeLogInMode(View view) {

        //
        if (!sharedPreferences.getBoolean("LogInModeActive", false)) {

            repeatPassword.setVisibility(View.INVISIBLE);

            logInButton.setText("Log In");

            sharedPreferences.edit().putBoolean("LogInModeActive", true).apply();

            changeModeTextView.setText("Sign up?");


        } else {
            repeatPassword.setVisibility(View.VISIBLE);

            logInButton.setText("Create an account");

            sharedPreferences.edit().putBoolean("LogInModeActive", false).apply();

            changeModeTextView.setText("log in?");
        }

    }

    //sorgt dafür, dass der User automatisch angemeldet wird, wenn er Enter drückt
    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {

            logIn(v);

        }

        return false;
    }

    //sorgt dafür, dass das Keyboard verschwindet, wenn der User irgendwo auf den Bildschirm drückt
    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.relative_layout_picture || v.getId() == R.id.activity_log_in) {

            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);

            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);

        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        //just for better workflow
        ParseUser currentUser = ParseUser.getCurrentUser();
        if (currentUser != null) {


            ParseUser.logInInBackground("discobreak21", "discobreak21", new LogInCallback() {
                @Override
                public void done(ParseUser user, ParseException e) {

                    if (user != null) {
                        Log.i("LogIn", "Successful");

                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);

                        startActivity(intent);
                    } else {

                        Log.i("LogIn", "Failed: " + e.toString());

                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG);
                    }

                }
            });

        } else {

            Intent intent = new Intent(getApplicationContext(), MainActivity.class);

            startActivity(intent);

        }


        //speichert den Log-In Modus ab
        sharedPreferences = this.getSharedPreferences("com.basbaer.runcleconnect", Context.MODE_PRIVATE);

        //checkt ob ein user eingeloggt ist
        if (sharedPreferences.getBoolean("userLoggedIn", false)) {
            ParseUser.logInInBackground(sharedPreferences.getString("username", null), sharedPreferences.getString("userPassword", null), new LogInCallback() {
                @Override
                public void done(ParseUser user, ParseException e) {

                    if (user != null) {
                        Log.i("LogIn", "Successful");

                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);

                        startActivity(intent);
                    } else {

                        Log.i("LogIn", "Failed: " + e.toString());

                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG);
                    }

                }
            });


        } else {


            usernameEditText = (EditText) findViewById(R.id.usernameEditText);

            passwordEditText = (EditText) findViewById(R.id.passwordEditText);

            logInButton = (Button) findViewById(R.id.logInButton);

            repeatPassword = (EditText) findViewById(R.id.passwordRepetionEditText);

            changeModeTextView = (TextView) findViewById(R.id.changeModeTextView);


            //sorgt dafür, dass der richtige Modus beim Öffnen angezeigt wird
            if (sharedPreferences.getBoolean("LogInModeActive", false)) {
                repeatPassword.setVisibility(View.INVISIBLE);

                logInButton.setText("Log In");

                changeModeTextView.setText("Sign up?");
            }

            //erstellt einen Listener, sodass sich der Benutzer mit drücken der Enter-Taste direkt anmeldet
            passwordEditText.setOnKeyListener(this);


            //Layout in dem sich alles befindet
            ConstraintLayout constraintLayout = (ConstraintLayout) findViewById(R.id.activity_log_in);

            //RelativeLayout in dem das Hintergrundbild gespeichert ist (ist ein separates Layout, da
            //das Hintergrundbild einen alpha-Wert <1 hat, aber die anderen Elemente 1 besitzen
            ConstraintLayout constraintLayoutBackground = (ConstraintLayout) findViewById(R.id.relative_layout_picture);

            constraintLayout.setOnClickListener(this);
            constraintLayoutBackground.setOnClickListener(this);


            ParseAnalytics.trackAppOpenedInBackground(getIntent());
        }
    }


}
