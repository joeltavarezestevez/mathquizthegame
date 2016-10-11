package com.desprosolutions.mathquiz;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;
import com.jraska.falcon.Falcon;

import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    public static GoogleAnalytics analytics;
    public static Tracker tracker;
    private InterstitialAd interstitial;
    private InterstitialAd interstitialVideos;

    //Level of the game
    int Level = 1;
    int Time = -1;
    int Score = 0;
    int highScore = 0;
    int LevelCounter = 0;
    SharedPreferences HighScore;
    int TIME = 151;
    boolean stopped = true;

    // Create a Random object to generate random numbers.
    Random randomizer = new Random();
    String[] signs = {"+", "-", "x", "/"};

    private Handler customHandler = new Handler();

    // This SoundPlayer plays a sound whenever the player hits a wall.
    //System.Media.SoundPlayer startSoundPlayer = new System.Media.SoundPlayer(@"C:\Windows\Media\chimes.wav");
    //System.Media.SoundPlayer finishSoundPlayer = new System.Media.SoundPlayer(@"C:\Windows\Media\tada.wav");

    // These ints will store the numbers for the addition problem.
    int addend1;
    int addend2;
    // These ints will store the numbers for the subtraction problem.
    int minuend;
    int subtrahend;
    // These ints will store the numbers for the multiplication problem.
    int multiplicand;
    int multiplier;
    // These ints will store the numbers for the division problem.
    int dividend;
    int divisor;
    //Limits
    int AdditionMaxLimit;
    int SubstractionMaxLimit;
    int MultDivMaxLimit;
    int AdditionMinLimit;
    int SubstractionMinLimit;
    int MultDivMinLimit;
    //Configuración
    TextView textViewNivel;
    TextView textViewTiempo;
    TextView textViewScore;
    TextView textViewTiempoRestante;
    //Suma
    TextView textViewSuma;
    TextView textViewSuma1;
    TextView textViewSuma2;
    TextView textViewSumaIgual;
    EditText editTextSuma;
    //Resta
    TextView textViewResta;
    TextView textViewResta1;
    TextView textViewResta2;
    TextView textViewRestaIgual;
    EditText editTextResta;
    //Multiplicacion
    TextView textViewMultiplicacion;
    TextView textViewMultiplicacion1;
    TextView textViewMultiplicacion2;
    TextView textViewMultiplicacionIgual;
    EditText editTextMultiplicacion;
    //Division
    TextView textViewDivision;
    TextView textViewDivision1;
    TextView textViewDivision2;
    TextView textViewDivisionIgual;
    EditText editTextDivision;
    //Buttion Iniciar
    Button buttonIniciar;
    //Resultado de cada Operacion
    String suma;
    String resta;
    String multiplicacion;
    String division;
    int extraChances;
    Boolean mIsAdVew;
    /// <summary>
    /// Start the quiz by filling in all of the problems
    /// and starting the timer.
    /// </summary>
    public void StartTheQuiz()
    {

        Collections.shuffle(Arrays.asList(signs));
        extraChances = 0;
        textViewSuma.setText(signs[0]);
        textViewResta.setText(signs[1]);
        textViewMultiplicacion.setText(signs[2]);
        textViewDivision.setText(signs[3]);
        textViewTiempo.setTextColor(Color.parseColor("#000000"));
        editTextSuma.setTextColor(Color.parseColor("#FFFFFF"));
        editTextResta.setTextColor(Color.parseColor("#FFFFFF"));
        editTextMultiplicacion.setTextColor(Color.parseColor("#FFFFFF"));
        editTextDivision.setTextColor(Color.parseColor("#FFFFFF"));

        if (buttonIniciar.getText().toString() == getString(R.string.buttonIniciar))
        {
            Score = 0;
            textViewScore.setText(getString(R.string.textViewScore) + " 0");
        }


        if (Time == -1) {

            textViewNivel.setText(getString(R.string.textViewNivel) + " " + String.valueOf(Level));

            //Fill in the Max Random Limits For the Problems
            AdditionMaxLimit += 5;
            SubstractionMaxLimit += 5;
            MultDivMaxLimit += 2;
            //Fill in the Min Random Limits For the Problems
            AdditionMinLimit += 2;
            SubstractionMinLimit += 2;
            MultDivMinLimit += 1;
            // Fill in the addition problem.
            addend1 = randomizer.nextInt(AdditionMaxLimit - AdditionMinLimit + 1) + AdditionMinLimit;
            addend2 = randomizer.nextInt(AdditionMaxLimit - AdditionMinLimit + 1) + AdditionMinLimit;
            // Fill in the subtraction problem.
            minuend = randomizer.nextInt(SubstractionMaxLimit - SubstractionMinLimit + 1) + SubstractionMinLimit;
            subtrahend = randomizer.nextInt(minuend - SubstractionMinLimit + 1) + SubstractionMinLimit;
            // Fill in the multiplication problem.
            multiplicand = randomizer.nextInt(MultDivMaxLimit - MultDivMinLimit + 1) + MultDivMinLimit;
            multiplier = randomizer.nextInt(MultDivMaxLimit - MultDivMinLimit + 1) + MultDivMinLimit;
            // Fill in the division problem.
            divisor = randomizer.nextInt(MultDivMaxLimit - MultDivMinLimit + 1) + MultDivMinLimit;
            int temporaryQuotient = randomizer.nextInt(MultDivMaxLimit - MultDivMinLimit + 1) + MultDivMinLimit;
            dividend = divisor * temporaryQuotient;

            if(signs[0] == "+"){
                setAdditionLimits(textViewSuma1, textViewSuma2);
            }
            else if (signs[0] == "-"){
                setSubstractionLimits(textViewSuma1, textViewSuma2);
            }
            else if (signs[0] == "x"){
                setMultiplicationLimits(textViewSuma1, textViewSuma2);
            }
            else if (signs[0] == "/"){
                setDivisionLimits(textViewSuma1, textViewSuma2);
            }

            if(signs[1] == "+"){
                setAdditionLimits(textViewResta1, textViewResta2);
            }
            else if (signs[1] == "-"){
                setSubstractionLimits(textViewResta1, textViewResta2);
            }
            else if (signs[1] == "x"){
                setMultiplicationLimits(textViewResta1, textViewResta2);
            }
            else if (signs[1] == "/"){
                setDivisionLimits(textViewResta1, textViewResta2);
            }

            if(signs[2] == "+"){
                setAdditionLimits(textViewMultiplicacion1, textViewMultiplicacion2);
            }
            else if (signs[2] == "-"){
                setSubstractionLimits(textViewMultiplicacion1, textViewMultiplicacion2);
            }
            else if (signs[2] == "x"){
                setMultiplicationLimits(textViewMultiplicacion1, textViewMultiplicacion2);
            }
            else if (signs[2] == "/"){
                setDivisionLimits(textViewMultiplicacion1, textViewMultiplicacion2);
            }

            if(signs[3] == "+"){
                setAdditionLimits(textViewDivision1, textViewDivision2);
            }
            else if (signs[3] == "-"){
                setSubstractionLimits(textViewDivision1, textViewDivision2);
            }
            else if (signs[3] == "x"){
                setMultiplicationLimits(textViewDivision1, textViewDivision2);
            }
            else if (signs[3] == "/"){
                setDivisionLimits(textViewDivision1, textViewDivision2);
            }

            if (LevelCounter == 5) {
                TIME = TIME + 50;
                LevelCounter = 0;
            }

            //Assign the time
            Time = TIME;

            //Clear all the editText
            editTextSuma.setText("");
            editTextResta.setText("");
            editTextMultiplicacion.setText("");
            editTextDivision.setText("");
            requestFocusAndShowKeyboard(editTextSuma);
            // Start the timer.
            customHandler.removeCallbacks(updateTimerThread);
            customHandler.postDelayed(updateTimerThread, 0);

            //Disable the button
            buttonIniciar.setEnabled(false);

            //Enable all the editText
            editTextSuma.setEnabled(true);
            editTextResta.setEnabled(true);
            editTextMultiplicacion.setEnabled(true);
            editTextDivision.setEnabled(true);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Force the screen to Portrait if the device is a Smartphone or a chinese tablet.
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        setContentView(R.layout.activity_main);

        // Prepare the Interstitial Ad
        interstitial = new InterstitialAd(this);
        // Insert the Ad Unit ID
        interstitial.setAdUnitId("ca-app-pub-2915919069891811/9356553190");

        interstitial.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                requestNewInterstitial();
            }
        });

        requestNewInterstitial();

        // Prepare the Interstitial Video Ad
        interstitialVideos = new InterstitialAd(this);
        // Insert the Ad Unit ID
        interstitialVideos.setAdUnitId("ca-app-pub-2915919069891811/3033831194");

        interstitialVideos.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {

                try {

                    requestNewInterstitialVideo();

                    Time += 50;

                    //Disable the button
                    buttonIniciar.setEnabled(false);
                    editTextSuma.setEnabled(true);
                    editTextResta.setEnabled(true);
                    editTextMultiplicacion.setEnabled(true);
                    editTextDivision.setEnabled(true);

                    //Enable and clear the editText without correct answer
                    if (editTextSuma.getText().toString() == "0") {
                        editTextSuma.setText("");
                        requestFocusAndShowKeyboard(editTextSuma);
                    }
                    else if (editTextResta.getText().toString() == "0") {
                        editTextResta.setText("");
                        requestFocusAndShowKeyboard(editTextResta);
                    }
                    else if (editTextMultiplicacion.getText().toString() == "0") {
                        editTextMultiplicacion.setText("");
                        requestFocusAndShowKeyboard(editTextMultiplicacion);
                    }
                    else if (editTextDivision.getText().toString() == "0") {
                        editTextDivision.setText("");
                        requestFocusAndShowKeyboard(editTextDivision);
                    }

                    // Start the timer.
                    customHandler.removeCallbacks(updateTimerThread);
                    customHandler.postDelayed(updateTimerThread, 0);
                }
                catch (Exception e){
                    Log.d("RompeVideo", "onAdClosed: " + e.getMessage());
                }
            }
        });

        requestNewInterstitialVideo();

        //Locate the Banner Ad in activity_main.xml
        AdView mAdView = (AdView) this.findViewById(R.id.adView);
        mIsAdVew = mAdView != null && mAdView.getVisibility() == View.VISIBLE;

        // Request for Ads
        AdRequest adRequest = new AdRequest.Builder().build();

        if (mIsAdVew) {
        /* display adView on Bottom */
            mAdView.loadAd(adRequest);
            mAdView.bringToFront();
        }

        analytics = GoogleAnalytics.getInstance(this);
        analytics.setLocalDispatchPeriod(1800);

        tracker = analytics.newTracker("UA-11614389-10");
        tracker.enableExceptionReporting(true);
        tracker.enableAdvertisingIdCollection(true);
        tracker.enableAutoActivityTracking(true);

        textViewNivel = (TextView) findViewById(R.id.textViewNivel);
        textViewTiempoRestante = (TextView) findViewById(R.id.textViewTiempoRestante);
        textViewTiempo = (TextView) findViewById(R.id.textViewTiempo);
        textViewScore = (TextView) findViewById(R.id.textViewScore);
        textViewSuma = (TextView) findViewById(R.id.textViewSuma);
        textViewSuma1 = (TextView) findViewById(R.id.textViewSuma1);
        textViewSuma2 = (TextView) findViewById(R.id.textViewSuma2);
        textViewSumaIgual = (TextView) findViewById(R.id.textViewSumaIgual);
        textViewResta = (TextView) findViewById(R.id.textViewResta);
        textViewResta1 = (TextView) findViewById(R.id.textViewResta1);
        textViewResta2 = (TextView) findViewById(R.id.textViewResta2);
        textViewRestaIgual = (TextView) findViewById(R.id.textViewRestaIgual);
        textViewMultiplicacion = (TextView) findViewById(R.id.textViewMultiplicacion);
        textViewMultiplicacion1 = (TextView) findViewById(R.id.textViewMultiplicacion1);
        textViewMultiplicacion2 = (TextView) findViewById(R.id.textViewMultiplicacion2);
        textViewMultiplicacionIgual = (TextView) findViewById(R.id.textViewMultiplicacionIgual);
        textViewDivision = (TextView) findViewById(R.id.textViewDivision);
        textViewDivision1 = (TextView) findViewById(R.id.textViewDivision1);
        textViewDivision2 = (TextView) findViewById(R.id.textViewDivision2);
        textViewDivisionIgual = (TextView) findViewById(R.id.textViewDivisionIgual);
        editTextSuma = (EditText) findViewById(R.id.editTextSuma);
        editTextResta = (EditText) findViewById(R.id.editTextResta);
        editTextMultiplicacion = (EditText) findViewById(R.id.editTextMultiplicacion);
        editTextDivision = (EditText) findViewById(R.id.editTextDivision);
        buttonIniciar = (Button) findViewById(R.id.buttonIniciar);


        Typeface tf = Typeface.createFromAsset(getAssets(), "fonts/rudiment.ttf");

        textViewNivel.setTypeface(tf);
        textViewTiempoRestante.setTypeface(tf);
        //textViewTiempo.setTypeface(tf);
        textViewScore.setTypeface(tf);
        textViewSuma.setTypeface(tf);
        textViewSuma1.setTypeface(tf);
        textViewSuma2.setTypeface(tf);
        textViewSumaIgual.setTypeface(tf);
        editTextSuma.setTypeface(tf);
        textViewResta.setTypeface(tf);
        textViewResta1.setTypeface(tf);
        textViewResta2.setTypeface(tf);
        textViewRestaIgual.setTypeface(tf);
        editTextResta.setTypeface(tf);
        textViewMultiplicacion.setTypeface(tf);
        textViewMultiplicacion1.setTypeface(tf);
        textViewMultiplicacion2.setTypeface(tf);
        textViewMultiplicacionIgual.setTypeface(tf);
        editTextMultiplicacion.setTypeface(tf);
        textViewDivision.setTypeface(tf);
        textViewDivision1.setTypeface(tf);
        textViewDivision2.setTypeface(tf);
        textViewDivisionIgual.setTypeface(tf);
        editTextDivision.setTypeface(tf);
        buttonIniciar.setTypeface(tf);

        editTextSuma.setEnabled(false);
        editTextResta.setEnabled(false);
        editTextMultiplicacion.setEnabled(false);
        editTextDivision.setEnabled(false);

        Collections.shuffle(Arrays.asList(signs));

        textViewSuma.setText(signs[0]);

        textViewResta.setText(signs[1]);

        textViewMultiplicacion.setText(signs[2]);

        textViewDivision.setText(signs[3]);

        buttonIniciar.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                StartTheQuiz();
            }
        });

        editTextSuma.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void afterTextChanged(Editable editable) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                try {
                    if (editTextSuma.length() > 0 && editTextSuma.getText().toString() != "") {
                        if (signs[0] == "+") {
                            //Check the Addition Problem
                            suma = editTextSuma.getText().toString();
                            if (addend1 + addend2 == Integer.parseInt(suma)) {
                                editTextSuma.setTextColor(Color.parseColor("#00FF00"));
                                editTextSuma.setEnabled(false);
                                requestFocusAndShowKeyboard(editTextResta);
                            } else {
                                editTextSuma.setTextColor(Color.parseColor("#FF0000"));
                            }
                        } else if (signs[0] == "-") {
                            //Check the Substraction Problem
                            resta = editTextSuma.getText().toString();
                            if (minuend - subtrahend == Integer.parseInt(resta)) {
                                editTextSuma.setTextColor(Color.parseColor("#00FF00"));
                                editTextSuma.setEnabled(false);
                                requestFocusAndShowKeyboard(editTextResta);
                            } else {
                                editTextSuma.setTextColor(Color.parseColor("#FF0000"));
                            }
                        } else if (signs[0] == "x") {
                            //Check the Multiplication Problem
                            multiplicacion = editTextSuma.getText().toString();
                            if (multiplicand * multiplier == Integer.parseInt(multiplicacion)) {
                                editTextSuma.setTextColor(Color.parseColor("#00FF00"));
                                editTextSuma.setEnabled(false);
                                requestFocusAndShowKeyboard(editTextResta);
                            } else {
                                editTextSuma.setTextColor(Color.parseColor("#FF0000"));
                            }
                        } else if (signs[0] == "/") {
                            //Check the Division Problem
                            division = editTextSuma.getText().toString();
                            if (dividend / divisor == Integer.parseInt(division)) {
                                editTextSuma.setTextColor(Color.parseColor("#00FF00"));
                                editTextSuma.setEnabled(false);
                                requestFocusAndShowKeyboard(editTextResta);
                            } else {
                                editTextSuma.setTextColor(Color.parseColor("#FF0000"));
                            }
                        }
                    }
                }
                catch (Exception e)
                {
                    Log.d("LaRealEmplota", "onTextChanged Error " + e.getMessage());
                }
            }
        });

        editTextResta.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void afterTextChanged(Editable editable) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                try {
                    if (editTextResta.length() > 0 && editTextResta.getText().toString() != "") {
                        if (signs[1] == "+") {
                            //Check the Addition Problem
                            suma = editTextResta.getText().toString();
                            if (addend1 + addend2 == Integer.parseInt(suma)) {
                                editTextResta.setTextColor(Color.parseColor("#00FF00"));
                                editTextResta.setEnabled(false);
                                requestFocusAndShowKeyboard(editTextMultiplicacion);
                            } else {
                                editTextResta.setTextColor(Color.parseColor("#FF0000"));
                            }
                        } else if (signs[1] == "-") {
                            //Check the Substraction Problem
                            resta = editTextResta.getText().toString();
                            if (minuend - subtrahend == Integer.parseInt(resta)) {
                                editTextResta.setTextColor(Color.parseColor("#00FF00"));
                                editTextResta.setEnabled(false);
                                requestFocusAndShowKeyboard(editTextMultiplicacion);
                            } else {
                                editTextResta.setTextColor(Color.parseColor("#FF0000"));
                            }
                        } else if (signs[1] == "x") {
                            //Check the Multiplication Problem
                            multiplicacion = editTextResta.getText().toString();
                            if (multiplicand * multiplier == Integer.parseInt(multiplicacion)) {
                                editTextResta.setTextColor(Color.parseColor("#00FF00"));
                                editTextResta.setEnabled(false);
                                requestFocusAndShowKeyboard(editTextMultiplicacion);
                            } else {
                                editTextResta.setTextColor(Color.parseColor("#FF0000"));
                            }
                        } else if (signs[1] == "/") {
                            //Check the Division Problem
                            division = editTextResta.getText().toString();
                            if (dividend / divisor == Integer.parseInt(division)) {
                                editTextResta.setTextColor(Color.parseColor("#00FF00"));
                                editTextResta.setEnabled(false);
                                requestFocusAndShowKeyboard(editTextMultiplicacion);
                            } else {
                                editTextResta.setTextColor(Color.parseColor("#FF0000"));
                            }
                        }
                    }
                }
                catch (Exception e)
                {
                    Log.d("LaRealEmplota", "onTextChanged Error " + e.getMessage());
                }
            }
        });

        editTextMultiplicacion.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void afterTextChanged(Editable editable) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                try {
                    if (editTextMultiplicacion.length() > 0 && editTextMultiplicacion.getText().toString() != "") {
                        if (signs[2] == "+") {
                            //Check the Addition Problem
                            suma = editTextMultiplicacion.getText().toString();
                            if (addend1 + addend2 == Integer.parseInt(suma)) {
                                editTextMultiplicacion.setTextColor(Color.parseColor("#00FF00"));
                                editTextMultiplicacion.setEnabled(false);
                                requestFocusAndShowKeyboard(editTextDivision);
                            } else {
                                editTextMultiplicacion.setTextColor(Color.parseColor("#FF0000"));
                            }
                        } else if (signs[2] == "-") {
                            //Check the Substraction Problem
                            resta = editTextMultiplicacion.getText().toString();
                            if (minuend - subtrahend == Integer.parseInt(resta)) {
                                editTextMultiplicacion.setTextColor(Color.parseColor("#00FF00"));
                                editTextMultiplicacion.setEnabled(false);
                                requestFocusAndShowKeyboard(editTextDivision);
                            } else {
                                editTextMultiplicacion.setTextColor(Color.parseColor("#FF0000"));
                            }
                        } else if (signs[2] == "x") {
                            //Check the Multiplication Problem
                            multiplicacion = editTextMultiplicacion.getText().toString();
                            if (multiplicand * multiplier == Integer.parseInt(multiplicacion)) {
                                editTextMultiplicacion.setTextColor(Color.parseColor("#00FF00"));
                                editTextMultiplicacion.setEnabled(false);
                                requestFocusAndShowKeyboard(editTextDivision);
                            } else {
                                editTextMultiplicacion.setTextColor(Color.parseColor("#FF0000"));
                            }
                        } else if (signs[2] == "/") {
                            //Check the Division Problem
                            division = editTextMultiplicacion.getText().toString();
                            if (dividend / divisor == Integer.parseInt(division)) {
                                editTextMultiplicacion.setTextColor(Color.parseColor("#00FF00"));
                                editTextMultiplicacion.setEnabled(false);
                                requestFocusAndShowKeyboard(editTextDivision);
                            } else {
                                editTextMultiplicacion.setTextColor(Color.parseColor("#FF0000"));
                            }
                        }
                    }
                }
                catch (Exception e)
                {
                    Log.d("LaRealEmplota", "onTextChanged Error " + e.getMessage());
                }
            }
        });

        editTextDivision.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void afterTextChanged(Editable editable) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                try {
                    if (editTextDivision.length() > 0 && editTextDivision.getText().toString() != "") {
                        if (signs[3] == "+") {
                            //Check the Addition Problem
                            suma = editTextDivision.getText().toString();
                            if (addend1 + addend2 == Integer.parseInt(suma)) {
                                editTextDivision.setTextColor(Color.parseColor("#00FF00"));
                                editTextDivision.setEnabled(false);
                                if (editTextSuma.length() <= 0 || editTextSuma.getTextColors().toString() == String.valueOf(Color.parseColor("#FF0000"))) {
                                    requestFocusAndShowKeyboard(editTextSuma);
                                } else if (editTextResta.length() <= 0 || editTextResta.getTextColors().toString() == String.valueOf(Color.parseColor("#FF0000"))) {
                                    requestFocusAndShowKeyboard(editTextResta);
                                } else if (editTextMultiplicacion.length() <= 0 || editTextMultiplicacion.getTextColors().toString() == String.valueOf(Color.parseColor("#FF0000"))) {
                                    requestFocusAndShowKeyboard(editTextMultiplicacion);
                                }
                            } else {
                                editTextDivision.setTextColor(Color.parseColor("#FF0000"));
                            }
                        } else if (signs[3] == "-") {
                            //Check the Substraction Problem
                            resta = editTextDivision.getText().toString();
                            if (minuend - subtrahend == Integer.parseInt(resta)) {
                                editTextDivision.setTextColor(Color.parseColor("#00FF00"));
                                editTextDivision.setEnabled(false);
                                if (editTextSuma.length() <= 0 || editTextSuma.getTextColors().toString() == String.valueOf(Color.parseColor("#FF0000"))) {
                                    requestFocusAndShowKeyboard(editTextSuma);
                                } else if (editTextResta.length() <= 0 || editTextResta.getTextColors().toString() == String.valueOf(Color.parseColor("#FF0000"))) {
                                    requestFocusAndShowKeyboard(editTextResta);
                                } else if (editTextMultiplicacion.length() <= 0 || editTextMultiplicacion.getTextColors().toString() == String.valueOf(Color.parseColor("#FF0000"))) {
                                    requestFocusAndShowKeyboard(editTextMultiplicacion);
                                }
                            } else {
                                editTextDivision.setTextColor(Color.parseColor("#FF0000"));
                            }
                        } else if (signs[3] == "x") {
                            //Check the Multiplication Problem
                            multiplicacion = editTextDivision.getText().toString();
                            if (multiplicand * multiplier == Integer.parseInt(multiplicacion)) {
                                editTextDivision.setTextColor(Color.parseColor("#00FF00"));
                                editTextDivision.setEnabled(false);
                                if (editTextSuma.length() <= 0 || editTextSuma.getTextColors().toString() == String.valueOf(Color.parseColor("#FF0000"))) {
                                    requestFocusAndShowKeyboard(editTextSuma);
                                } else if (editTextResta.length() <= 0 || editTextResta.getTextColors().toString() == String.valueOf(Color.parseColor("#FF0000"))) {
                                    requestFocusAndShowKeyboard(editTextResta);
                                } else if (editTextMultiplicacion.length() <= 0 || editTextMultiplicacion.getTextColors().toString() == String.valueOf(Color.parseColor("#FF0000"))) {
                                    requestFocusAndShowKeyboard(editTextMultiplicacion);
                                }
                            } else {
                                editTextDivision.setTextColor(Color.parseColor("#FF0000"));
                            }
                        } else if (signs[3] == "/") {
                            //Check the Division Problem
                            division = editTextDivision.getText().toString();
                            if (dividend / divisor == Integer.parseInt(division)) {
                                editTextDivision.setTextColor(Color.parseColor("#00FF00"));
                                editTextDivision.setEnabled(false);
                                if (editTextSuma.length() <= 0 || editTextSuma.getTextColors().toString() == String.valueOf(Color.parseColor("#FF0000"))) {
                                    requestFocusAndShowKeyboard(editTextSuma);
                                } else if (editTextResta.length() <= 0 || editTextResta.getTextColors().toString() == String.valueOf(Color.parseColor("#FF0000"))) {
                                    requestFocusAndShowKeyboard(editTextResta);
                                } else if (editTextMultiplicacion.length() <= 0 || editTextMultiplicacion.getTextColors().toString() == String.valueOf(Color.parseColor("#FF0000"))) {
                                    requestFocusAndShowKeyboard(editTextMultiplicacion);
                                }
                            } else {
                                editTextDivision.setTextColor(Color.parseColor("#FF0000"));
                            }
                        }
                    }
                }
                catch (Exception e)
                {
                    Log.d("LaRealEmplota", "onTextChanged Error " + e.getMessage());
                }
            }
        });
    }


    private void setAdditionLimits(TextView textview1, TextView textview2) {
        textview1.setText(String.valueOf(addend1));
        textview2.setText(String.valueOf(addend2));
    }

    private void setSubstractionLimits(TextView textview1, TextView textview2) {
        textview1.setText(String.valueOf(minuend));
        textview2.setText(String.valueOf(subtrahend));
    }

    private void setMultiplicationLimits(TextView textview1, TextView textview2) {
        textview1.setText(String.valueOf(multiplicand));
        textview2.setText(String.valueOf(multiplier));
    }

    private void setDivisionLimits(TextView textview1, TextView textview2) {
        textview1.setText(String.valueOf(dividend));
        textview2.setText(String.valueOf(divisor));
    }

    private void requestFocusAndShowKeyboard(EditText editText){

        editText.requestFocus();
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
    }

    private void requestNewInterstitial() {

        // Request for Ads
        AdRequest adRequest = new AdRequest.Builder().build();
        // Load ads into Interstitial Ads
        interstitial.loadAd(adRequest);
    }

    private void requestNewInterstitialVideo() {
        // Request for Ads
        AdRequest adRequest = new AdRequest.Builder().build();
        // Load ads into Interstitial Ads
        interstitialVideos.loadAd(adRequest);
    }

    private void displayInterstitial() {
        // If Ads are loaded, show Interstitial else show nothing.
        if (interstitial.isLoaded()) {
            interstitial.show();
        }
    }

    private void displayInterstitialVideo() {

        // If Ads are loaded, show Interstitial else show nothing.
        if (interstitialVideos.isLoaded()) {
            interstitialVideos.show();
        }
    }

    public String formatTime(int time){

        int secs = (time / 10  % 60);
        String timer = " " + String.format("%02d", secs);
        return timer;
    }

    /// <summary>
    /// Check the answer to see if the user got everything right.
    /// </summary>
    /// <returns>True if the answer's correct, false otherwise.</returns>
    private boolean CheckTheAnswer()
    {
        if(signs[0] == "+"){
            suma = editTextSuma.getText().toString();
        }
        else if (signs[0] == "-"){
            resta = editTextSuma.getText().toString();
        }
        else if (signs[0] == "x"){
            multiplicacion = editTextSuma.getText().toString();
        }
        else if (signs[0] == "/"){
            division = editTextSuma.getText().toString();
        }

        if(signs[1] == "+"){
            suma = editTextResta.getText().toString();
        }
        else if (signs[1] == "-"){
            resta = editTextResta.getText().toString();
        }
        else if (signs[1] == "x"){
            multiplicacion = editTextResta.getText().toString();
        }
        else if (signs[1] == "/"){
            division = editTextResta.getText().toString();
        }

        if(signs[2] == "+"){
            suma = editTextMultiplicacion.getText().toString();
        }
        else if (signs[2] == "-"){
            resta = editTextMultiplicacion.getText().toString();
        }
        else if (signs[2] == "x"){
            multiplicacion = editTextMultiplicacion.getText().toString();
        }
        else if (signs[2] == "/"){
            division = editTextMultiplicacion.getText().toString();
        }

        if(signs[3] == "+"){
            suma = editTextDivision.getText().toString();
        }
        else if (signs[3] == "-"){
            resta = editTextDivision.getText().toString();
        }
        else if (signs[3] == "x"){
            multiplicacion = editTextDivision.getText().toString();
        }
        else if (signs[3] == "/"){
            division = editTextDivision.getText().toString();
        }

        if (suma.length() > 0 && resta.length() > 0 && multiplicacion.length() > 0 && division.length()> 0) {
            if ((addend1 + addend2 == Integer.parseInt(suma))
                    && (minuend - subtrahend == Integer.parseInt(resta))
                    && (multiplicand * multiplier == Integer.parseInt(multiplicacion))
                    && (dividend / divisor == Integer.parseInt(division)) && Time > -1)
                return true;
            else
                return false;
        }

        return false;
    }

    private Runnable updateTimerThread = new Runnable() {

        public void run() {

            Time -= 1;

            if (CheckTheAnswer())
            {
                try {
                    // If the user got the answer right, stop the timer
                    customHandler.removeCallbacks(updateTimerThread);
                    buttonIniciar.setEnabled(true);
                    editTextSuma.setEnabled(false);
                    editTextResta.setEnabled(false);
                    editTextMultiplicacion.setEnabled(false);
                    editTextDivision.setEnabled(false);
                    buttonIniciar.setText(getString(R.string.mNextlevel));
                    Score = Score + 400 + 100 * Integer.parseInt(String.valueOf(textViewTiempo.getText().toString().trim()));
                    textViewScore.setText(getString(R.string.textViewScore) + " " + String.valueOf(Score));
                    Time = -1;
                    Level++;
                    LevelCounter++;
                }
                catch (Exception e) {

                    Log.d("LaRompePunto", "Errol: " + e.getMessage());

                }
            }
            else if (Time > -1)
            {
                // Display the new time left
                // by updating the Time Left label.
                String timer = formatTime(Time);
                textViewTiempo.setText(timer);
                customHandler.postDelayed(this, 100);

                if (Time < 51) {
                    textViewTiempo.setTextColor(Color.parseColor("#FF0000"));;
                }
            }
            else
            {
                // If the user ran out of time, stop the timer, show
                // a MessageBox, and fill in the answers.
                customHandler.removeCallbacks(updateTimerThread);
                textViewTiempo.setText(getString(R.string.mTimeup));
                buttonIniciar.setText(getString(R.string.buttonIniciar));
                buttonIniciar.setEnabled(true);
                editTextSuma.setEnabled(false);
                editTextResta.setEnabled(false);
                editTextMultiplicacion.setEnabled(false);
                editTextDivision.setEnabled(false);

                //Fill in the Max Random Limits For the Problems
                AdditionMaxLimit = 0;
                SubstractionMaxLimit = 0;
                MultDivMaxLimit = 0;

                //Fill in the Min Random Limits For the Problems
                AdditionMinLimit = 0;
                SubstractionMinLimit = 0;
                MultDivMinLimit = 0;

                Level = 1;
                LevelCounter = 0;
                TIME = 151;

                if (editTextSuma.length() <= 0) {
                    editTextSuma.setText("0");
                    editTextSuma.setTextColor(Color.parseColor("#FF0000"));
                }

                if (editTextResta.length() <= 0) {
                    editTextResta.setText("0");
                    editTextResta.setTextColor(Color.parseColor("#FF0000"));
                }

                if (editTextMultiplicacion.length() <= 0) {
                    editTextMultiplicacion.setText("0");
                    editTextMultiplicacion.setTextColor(Color.parseColor("#FF0000"));
                }

                if (editTextDivision.length() <= 0) {
                    editTextDivision.setText("0");
                    editTextDivision.setTextColor(Color.parseColor("#FF0000"));
                }

                initiatePopupWindow();

                //Click on Stop
                customHandler.removeCallbacks(updateTimerThread);
            }
        }
    };

    @Override
    public void onBackPressed() {
        // your code.
        finish();
    }

    PopupWindow pwindo;

    private void initiatePopupWindow() {
        try {

            // We need to get the instance of the LayoutInflater
            LayoutInflater inflater = (LayoutInflater) MainActivity.this
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View layout = inflater.inflate(R.layout.popup,
                    (ViewGroup) findViewById(R.id.popup_element));
            Display display = getWindowManager().getDefaultDisplay();
            int width = display.getWidth();
            int height = display .getHeight();

            //pwindo = new PopupWindow(layout, 500, 680, true);
            pwindo = new PopupWindow(layout, (int) (width/1.5), height/2, true);
            pwindo.showAtLocation(layout, Gravity.CENTER, 0, 0);

            Button buttonTryAgain = (Button) layout.findViewById(R.id.buttonTryAgain);
            Button buttonShareScore = (Button) layout.findViewById(R.id.buttonShareScore);
            buttonTryAgain.setOnClickListener(try_again_button_click_listener);
            buttonShareScore.setOnClickListener(share_score_button_click_listener);

            Typeface tf = Typeface.createFromAsset(getAssets(), "fonts/rudiment.ttf");

            TextView textViewGameOver = (TextView) layout.findViewById(R.id.textViewGameOver);
            TextView textViewScore = (TextView) layout.findViewById(R.id.textViewScore);
            TextView textViewHighScore = (TextView) layout.findViewById(R.id.textViewHighScore);
            TextView textViewExtraTime = (TextView) layout.findViewById(R.id.textViewExtraTime);

            textViewGameOver.setTypeface(tf);
            textViewScore.setTypeface(tf);
            textViewHighScore.setTypeface(tf);
            textViewExtraTime.setTypeface(tf);

            // If Ads are loaded, show Interstitial else show nothing.
            if (!interstitialVideos.isLoaded() || extraChances == 1) {
                textViewExtraTime.setVisibility(View.GONE);
            }

            textViewExtraTime.setOnClickListener(textViewExtraTime_click_listener);

            textViewScore.setText(String.valueOf(Score));

            SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
            String HScore = settings.getString("HighScore", "");

            if (HScore.length()>0) {
                highScore = Integer.parseInt(HScore);
            }

            if (Score > highScore) {

                SharedPreferences.Editor editor = settings.edit();
                editor.putString("HighScore", String.valueOf(Score));
                editor.commit();
                highScore = Score;
            }

            textViewHighScore.setText(String.valueOf(highScore));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private View.OnClickListener try_again_button_click_listener = new View.OnClickListener() {
        public void onClick(View v) {
            pwindo.dismiss();
            displayInterstitial();
            requestNewInterstitial();
            requestNewInterstitialVideo();
        }
    };

    private View.OnClickListener textViewExtraTime_click_listener = new View.OnClickListener() {
        public void onClick(View v) {
            if (v.getId() == R.id.textViewExtraTime) {
                pwindo.dismiss();
                displayInterstitialVideo();
                requestNewInterstitial();
                requestNewInterstitialVideo();
                extraChances++;
            }
        }
    };

    private View.OnClickListener share_score_button_click_listener = new View.OnClickListener() {
        public void onClick(View v) {
            if (v.getId() == R.id.buttonShareScore) {
                takeScreenshotandShare();
            }
            else
            {
                pwindo.dismiss();
            }
        }
    };

    public void takeScreenshotandShare()
    {
        File imagePath = createFile();
        Falcon.takeScreenshot(MainActivity.this, imagePath);
        Toast.makeText(getApplicationContext(), getString(R.string.mScreenshotsaved), Toast.LENGTH_SHORT).show();
        shareImage(imagePath);
    }

    private File createFile()
    {
        File directory = new File(Environment.getExternalStorageDirectory() + "/MathQuiz");
        if (!directory.exists()) {

            try {
                directory.mkdirs();
            } catch (Exception e) {
                Log.d("App", getString(R.string.mFailedCreateDirectory) + " " + e.getMessage());
            }
        }

        Date date = new Date();
        String filename = String.valueOf(date.getDate()) + String.valueOf(date.getTime());
        File file = new File(Environment.getExternalStorageDirectory() + "/MathQuiz/" + "Screenshot_" + filename + ".png");

        return file;
    }

    private void shareImage(File file){

        String text_message = getString(R.string.mTextmessage);
        Uri uri = Uri.fromFile(file);
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_SUBJECT, text_message);
        intent.putExtra(Intent.EXTRA_TEXT, text_message);
        intent.putExtra(Intent.EXTRA_TITLE, text_message);
        intent.putExtra(Intent.EXTRA_STREAM, uri);

        startActivity(Intent.createChooser(intent, getString(R.string.mSharescore)));
    }
/*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }*/
}
