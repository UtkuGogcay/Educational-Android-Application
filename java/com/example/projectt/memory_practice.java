package com.example.projectt;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.Random;

public class memory_practice extends AppCompatActivity {
    String Choice="",userEmail;
    final String PrefKey="memory",TAG=PrefKey;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    int highScore,score=0,index=0,answer;


    Button buttonStart;
    EditText editTextAnswer;
    TextView textViewScore,textViewHighScore,textViewQuestion,textViewExplanation;
    Boolean highScoreChanced;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memory_practice);
        userEmail = getIntent().getStringExtra("userEmail");

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getString(R.string.practice));

        editTextAnswer=findViewById(R.id.editTextNumber_memory_prac_answer);
        textViewScore=findViewById(R.id.textView_memory_p_score);
        textViewHighScore=findViewById(R.id.textView_memory_p_high_score);
        textViewExplanation=findViewById(R.id.textView_memory_prac_explanation);
        textViewQuestion = findViewById(R.id.textView_mem_prac_question);
        buttonStart=findViewById(R.id.button_mem_prac_start);
        Choice=  getIntent().getStringExtra("choice");
        highScore= 0;
        textViewHighScore.setText(String.valueOf(highScore));
        buttonStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editTextAnswer.setVisibility(View.INVISIBLE);
                buttonStart.setVisibility(View.INVISIBLE);
                textViewExplanation.setText("");

                if(0==index++){

                    buttonStart.setText(getString(R.string.submit));
                    setQuestion();

                }
                else{
                    int userAnswer= Integer.parseInt( editTextAnswer.getText().toString());
                    if(answer==userAnswer){
                        rightAnswer();
                    }
                    else{
                        wrongAnswer();
                    }
                    editTextAnswer.setText("");
                    setQuestion();
                }
            }

        });
        getHighScore();
    }
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
    void rightAnswer(){
        score++;
        textViewScore.setText(String.valueOf(score));
        if(highScore<score){
            highScore=score;
            highScoreChanced=true;
            textViewHighScore.setText(String.valueOf(highScore));
            setHighScore();
        }
        Toast.makeText(this, R.string.right_answer, Toast.LENGTH_SHORT).show();
    }
    void wrongAnswer(){
        index=0;
        editTextAnswer.setVisibility(View.INVISIBLE);
        textViewQuestion.setText("");
        buttonStart.setText(R.string.start);
        textViewScore.setText(String.valueOf(score));
        setHighScore();
        new AlertDialog.Builder(this).setTitle(R.string.wrong_answer).setMessage(R.string.score2+score).show();
        score=0;

    }

    void setQuestion()  {
        final Handler handler = new Handler();
        final int delay = 1000;

        Random random = new Random();
        int[] array = new int[5];
        textViewQuestion.setVisibility(View.VISIBLE);
        for(int i =0;i<5;i++){
            int q = random.nextInt(8)+1;
            if(i!=0)
                while(array[i-1]==q){
                    q= random.nextInt(8)+1;
                }
            array[i]=q;
        }
        if(Choice.equals("forward")){

            Runnable runnable = new Runnable() {
                int i = 0;

                @Override
                public void run() {
                    if (i < 5) {
                        textViewQuestion.setText(String.valueOf(array[i]));
                        answer+=(int)array[i]*Math.pow(10.0,(double)4-i);
                        i++;
                        handler.postDelayed(this, delay);
                    }
                }
            };

            handler.postDelayed(runnable, delay);
            final int delay2=5500;
            Runnable runnable2 = new Runnable() {
                @Override
                public void run() {
                    textViewQuestion.setText("");
                    textViewExplanation.setText(getString(R.string.memory_prac_fwd));
                    editTextAnswer.setVisibility(View.VISIBLE);
                    buttonStart.setVisibility(View.VISIBLE);
                    handler.postDelayed(this, delay2);
                }
            };
            handler.postDelayed(runnable2,delay2);
        }
        else if(Choice.equals("reverse")){
            Runnable runnable = new Runnable() {
                int i = 0;

                @Override
                public void run() {
                    if (i < 5) {
                        textViewQuestion.setText(String.valueOf(array[4-i]));
                        answer+=(int)array[i]*Math.pow(10.0,(double)4-i);
                        i++;
                        handler.postDelayed(this, delay);
                    }
                }
            };

            handler.postDelayed(runnable, delay);
            final int delay2=5500;
            Runnable runnable2 = new Runnable() {
                @Override
                public void run() {
                    textViewQuestion.setText("");
                    textViewExplanation.setText(getString(R.string.memory_prac_rvs));
                    editTextAnswer.setVisibility(View.VISIBLE);
                    buttonStart.setVisibility(View.VISIBLE);
                    handler.postDelayed(this, delay2);
                }
            };
            handler.postDelayed(runnable2,delay2);
        }

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
