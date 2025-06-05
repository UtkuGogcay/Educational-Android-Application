package com.example.projectt;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class months_practice extends AppCompatActivity {
    String userEmail ="";

    final String PrefKey="months",TAG=PrefKey;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    Button buttonAnsA,buttonAnsB,buttonAnsC,buttonAnsD;
    TextView textViewScore, textViewHighScore,textViewQuestion;
    String[] Months ;
    int questionIndex,score=0,highScore=0;
    boolean highScoreChanged=false;
    boolean b4=true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_months_practice);
        userEmail = getIntent().getStringExtra("userEmail");
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getString(R.string.practice));

        Months=new String[]{getString(R.string.january).toString(),getString(R.string.february).toString(),getString(R.string.march).toString(),getString(R.string.april).toString(),getString(R.string.may).toString(),getString(R.string.june).toString(),getString(R.string.july).toString(),getString(R.string.august).toString(),getString(R.string.september).toString(),getString(R.string.october).toString(),getString(R.string.november).toString(),getString(R.string.december).toString()};

        buttonAnsA=findViewById(R.id.button_months_prac_ansA);
        buttonAnsB=findViewById(R.id.button_months_prac_ansB);
        buttonAnsC=findViewById(R.id.button_months_prac_ansC);
        buttonAnsD=findViewById(R.id.button_months_prac_ansD);

        textViewScore=findViewById(R.id.textView_months_practice_score);
        textViewHighScore=findViewById(R.id.textView_months_prac_high_score);
        textViewQuestion=findViewById(R.id.textView_months_prac_question);
        buttonAnsA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer(buttonAnsA.getText().toString());
            }

        });
        buttonAnsB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer(buttonAnsB.getText().toString());
            }

        });
        buttonAnsC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer(buttonAnsC.getText().toString());
            }

        });

        buttonAnsD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer(buttonAnsD.getText().toString());
            }

        });
        setQuestion();
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
    void setQuestion() {

        Set<Integer> uniqueIntegers = new HashSet<>();
        Random random = new Random();
        if (random.nextInt(2)==1)b4=false;
        else b4=true;

        while (uniqueIntegers.size() < 4) {
            int randomNumber = random.nextInt(12);
            uniqueIntegers.add(randomNumber);
        }
        int index=0;
        int choices[]=new int[4];
        for (int number : uniqueIntegers)choices[index++]=number;
        shuffleArray(choices);
        questionIndex=choices[0];
        if(b4)choices[0]=(choices[0]-1)%12;
        else choices[0]=(choices[0]+1)%12;
        shuffleArray(choices);
        buttonAnsA.setText(Months[choices[0]]);
        buttonAnsB.setText(Months[choices[1]]);
        buttonAnsC.setText(Months[choices[2]]);
        buttonAnsD.setText(Months[choices[3]]);
        String a;
        if(b4)
        {
            a = getString(R.string.days_whatcomesbefore).toString() + " "+Months[questionIndex];
        }
        else{
            a = getString(R.string.days_whatcomesafter).toString() +" "+ Months[questionIndex];

        }
        textViewQuestion.setText(a);
    }
    void checkAnswer(String userAnswer)
    {
        int index;
        if(b4)index=(questionIndex-1)%12;
        else index=(questionIndex+1)%12;
        if(userAnswer.equals(Months[index]))rightAnswer();
        else falseAnswer();
        setQuestion();
    }
    void rightAnswer(){
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
    void falseAnswer(){
        new AlertDialog.Builder(this).setTitle(R.string.gameover).setMessage(getString(R.string.score2).toString()+score).show();
        score=0;
        textViewScore.setText(String.valueOf(score));
        setHighScore();
    }
    private static void shuffleArray(int[] array) {
        Random random = new Random();
        for (int i = array.length - 1; i > 0; i--) {
            int index = random.nextInt(i + 1);
            int temp = array[index];
            array[index] = array[i];
            array[i] = temp;
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