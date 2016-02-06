package com.zhawtof.crunchtime;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final boolean REP = true;
    private static final boolean MINUTE = false;

    EditText numberOfRepsOrMinutes;
    Spinner exercisePerformed;
    Spinner alternativeExercise;
    int alternativePosition;
    TextView caloriesBurned;
    TextView minOrRep;
    TextView alternativeNumberMinOrRep;
    TextView alternativeMinOrRep;

    int numberCompleted;
    int alternativeOneHundred;
    float forOneHundredCals;
    float product;

    private class Exercise {

        private String exerciseName;
        private boolean repOrMinute;
        private int forHundredCalories;

        public Exercise(String name, boolean rom, int hund) {
            exerciseName = name;
            repOrMinute = rom;
            forHundredCalories = hund;
        }

        public int getForHundredCalories() {
            return forHundredCalories;
        }

        public boolean getRepOrMinute() {
            return repOrMinute;
        }
    }

    HashMap<String, Exercise> allExercises = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        numberOfRepsOrMinutes = (EditText) findViewById(R.id.minutesRepsNumber);
        caloriesBurned = (TextView) findViewById(R.id.caloriesBurnedField);
        exercisePerformed = (Spinner) findViewById(R.id.exerciseSpinner);
        alternativeExercise = (Spinner) findViewById(R.id.alternativeSpinner);
        alternativeMinOrRep = (TextView) findViewById(R.id.alternativerop);
        alternativeNumberMinOrRep = (TextView) findViewById(R.id.alternativenumrop);
        minOrRep = (TextView) findViewById(R.id.minorrep);

        setupSpinners();
        setupCaloriesBurned();

    }

    private void setupCaloriesBurned() {
        numberOfRepsOrMinutes.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String num = s.toString().trim();
                if (num.length() > 0 && num.matches("^[0-9]*$")) {
                    try{
                        numberCompleted = Integer.parseInt(num);
                        product = numberCompleted * 100 / forOneHundredCals;
                        caloriesBurned.setText(Integer.toString(Math.round(product)));

                        //Calculate Alternative
                        alternativeNumberMinOrRep.setText(Integer.toString(Math.round(product * alternativeOneHundred / 100)));
                    } catch (NumberFormatException nfe) {
                        numberOfRepsOrMinutes.setText("");
                    }
                } else {
                    caloriesBurned.setText("0");
                    alternativeNumberMinOrRep.setText("0");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    private void setupSpinners() {

        allExercises.put("Pushups", new Exercise("Pushups", REP, 350));
        allExercises.put("Situps", new Exercise("Situps", REP, 200));
        allExercises.put("Squats", new Exercise("Squats", REP, 225));
        allExercises.put("Leg-Lifts", new Exercise("Leg-Lifts", MINUTE, 25));
        allExercises.put("Plank", new Exercise("Plank", MINUTE, 25));
        allExercises.put("Jumping Jacks", new Exercise("Jumping Jacks", MINUTE, 10));
        allExercises.put("Pullups", new Exercise("Pullups", REP, 100));
        allExercises.put("Cycling", new Exercise("Cycling", MINUTE, 12));
        allExercises.put("Walking", new Exercise("Walking", MINUTE, 20));
        allExercises.put("Jogging", new Exercise("Jogging", MINUTE, 12));
        allExercises.put("Swimming", new Exercise("Swimming", MINUTE, 13));
        allExercises.put("Stair-Climbing", new Exercise("Stair-Climbing", MINUTE, 15));

        final List<String> exerciseList = new ArrayList<>(allExercises.keySet());
        Collections.sort(exerciseList);
        final ArrayAdapter<String> exerciseAdapter = new ArrayAdapter<>(
                this, R.layout.spinner_item, exerciseList);
        exerciseAdapter.setDropDownViewResource(R.layout.spinner_item);

        exercisePerformed.setAdapter(exerciseAdapter);
        alternativeExercise.setAdapter(exerciseAdapter);

        exercisePerformed.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String item = parent.getItemAtPosition(position).toString();

                if (position == alternativePosition) {
                    product = 0;
                    alternativeExercise.setSelection((position + 1) % exerciseList.size());
                }

                if (allExercises.get(item).getRepOrMinute() == REP) {
                    minOrRep.setText("reps");
                    numberOfRepsOrMinutes.setHint("# Reps");
                } else {
                    minOrRep.setText("minutes");
                    numberOfRepsOrMinutes.setHint("# Minutes");
                }
                forOneHundredCals = allExercises.get(item).getForHundredCalories();
                numberOfRepsOrMinutes.setText("");
                caloriesBurned.setText("0");
                alternativeNumberMinOrRep.setText("0");
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        alternativeExercise.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String item = parent.getItemAtPosition(position).toString();
                alternativePosition = position;
                if (allExercises.get(item).getRepOrMinute() == REP) {
                    alternativeMinOrRep.setText("reps");
                } else {
                    alternativeMinOrRep.setText("minutes");
                }
                alternativeOneHundred = allExercises.get(item).getForHundredCalories();
                if (!Float.isInfinite(product)) {
                    alternativeNumberMinOrRep.setText(Integer.toString(Math.round(product * alternativeOneHundred / 100)));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
}
