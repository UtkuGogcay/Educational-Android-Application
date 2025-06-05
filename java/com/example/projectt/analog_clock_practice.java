package com.example.projectt;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.TimePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class analog_clock_practice extends AppCompatActivity {
    String userEmail = "";
    TextView textViewScore, textViewHighScore;
    ImageView imageViewQuestion;
    Button buttonSubmit;
    private NumberPicker hourPicker;
    private NumberPicker minutePicker;
    int[]images;

    String[]answers;
    int questionIndex,score=0,highScore=0;
    boolean highScoreChanged=false;

    String PrefKey,TAG;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analog_clock_practice);
        userEmail = getIntent().getStringExtra("userEmail");
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getString(R.string.practice));


        PrefKey="analog";
        TAG=PrefKey;

        textViewScore=findViewById(R.id.textView_analog_clock_practice_score);
        textViewHighScore=findViewById(R.id.textView_analog_clock_prac_high_score);
        imageViewQuestion=findViewById(R.id.imageView_analog_clock_prac_question);
        buttonSubmit=findViewById(R.id.button_analog_clock_prac_play);

        hourPicker = findViewById(R.id.hourPicker);
        hourPicker.setMinValue(1);
        hourPicker.setMaxValue(12);
        hourPicker.setWrapSelectorWheel(true);

        minutePicker = findViewById(R.id.minutePicker);
        minutePicker.setMinValue(0);
        minutePicker.setMaxValue(59);
        minutePicker.setWrapSelectorWheel(true);
        images=new int[]{R.drawable.s_eleven_past_thirty,R.drawable.s_fivefivefive,R.drawable.s_four_past_quarter,R.drawable.s_four_twenty,R.drawable.s_nine,R.drawable.s_nine_past_thirty,R.drawable.s_one_past_forty_five,R.drawable.s_seven_past_quarter,R.drawable.s_six,R.drawable.s_ten_past_quarter,R.drawable.s_three_forty,R.drawable.s_two};
        answers=new String[]{"11:30","05:55","04:15","04:20","09:00","09:30","01:45","07:15","06:00","10:15","03:40","02:00"};
        getHighScore();

        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer();

            }

        });
        setQuestion();
    }
    void setQuestion(){
        Random random = new Random();
        questionIndex=random.nextInt(12);
        imageViewQuestion.setImageResource(images[questionIndex]);
    }
    void checkAnswer(){
        String userAnswer=String.format("%02d:%02d", hourPicker.getValue(), minutePicker.getValue());
        if(userAnswer.equals(answers[questionIndex])){
            score++;
            textViewScore.setText(String.valueOf(score));
            if(score>highScore){
                highScore=score;
                textViewHighScore.setText(String.valueOf(highScore));
                highScoreChanged=true;
                setHighScore();
            }
            Toast.makeText(this, R.string.right_answer, Toast.LENGTH_SHORT).show();
        }
        else{
            new AlertDialog.Builder(this).setTitle(R.string.gameover).setMessage(getString(R.string.score2).toString()+score).show();
            score=0;
            textViewScore.setText(String.valueOf(score));
            setHighScore();
        }
        setQuestion();
    }
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    void setHighScore(){
        SharedPreferences user_scores = getSharedPreferences(userEmail, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = user_scores.edit();

        editor.putString(PrefKey,String.valueOf(highScore));
        editor.apply();
        db.collection("users").document(userEmail).update(PrefKey,highScore);

    }
    void getHighScore(){
        SharedPreferences user_scores = getSharedPreferences(userEmail, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = user_scores.edit();

        highScore= Integer.parseInt(user_scores.getString(PrefKey, String.valueOf(highScore)));
        DocumentReference docRef = db.collection("users").document(userEmail);
        DocumentSnapshot document;
        textViewHighScore.setText(String.valueOf(highScore));

        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());

                        if(document.getLong(PrefKey)!=null){
                            int tempHighScore= document.getLong(PrefKey).intValue();
                            if(tempHighScore>highScore) {
                                highScore=tempHighScore;
                                editor.putString(PrefKey,String.valueOf(highScore));
                                editor.apply();
                            }
                        }




                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}