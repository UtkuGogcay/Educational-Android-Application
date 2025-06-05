package com.example.projectt;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Arrays;
import java.util.Random;

public class directions_practice extends AppCompatActivity {

    final String PrefKey="directions",TAG=PrefKey;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    String userEmail ="";
    Button button_ansA,button_ansB,button_ansC,button_ansD;
    TextView textViewScore,textViewHighScore;
    ImageView imageViewQuestion;
    String[] Propositions;
    int[] Drawblabes;
    String[]Answers;
    int question_index;
    int score=0,highScore=0;
    boolean highScoreChanged=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_directions_practice);
        Propositions = new String[]{getString(R.string.in), getString(R.string.on), getString(R.string.under), getString(R.string.behind), getString(R.string.between), getString(R.string.infrontof), getString(R.string.nextto), getString(R.string.opposite)};
        Drawblabes = new int[]{R.drawable.q_in, R.drawable.q_on, R.drawable.q_under, R.drawable.q_behind, R.drawable.q_between, R.drawable.q_infrontof, R.drawable.q_nextto, R.drawable.q_opposite};
        Answers = new String[]{getString(R.string.in), getString(R.string.on), getString(R.string.under), getString(R.string.behind), getString(R.string.between), getString(R.string.infrontof), getString(R.string.nextto), getString(R.string.opposite)};
        userEmail = getIntent().getStringExtra("userEmail");
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getString(R.string.practice));

        button_ansA=findViewById(R.id.button_direc_prac_ansA);
        button_ansB=findViewById(R.id.button_direc_prac_ansB);
        button_ansC=findViewById(R.id.button_direc_prac_ansC);
        button_ansD=findViewById(R.id.button_direc_prac_ansD);
        textViewScore=findViewById(R.id.textView_directions_practice_score);
        textViewHighScore=findViewById(R.id.textView_directions_prac_high_score);
        imageViewQuestion=findViewById(R.id.imageView_direction_prac_question);

        button_ansA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer(button_ansA.getText().toString());
            }

        });
        button_ansB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer(button_ansB.getText().toString());
            }

        });
        button_ansC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer(button_ansC.getText().toString());
            }

        });

        button_ansD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer(button_ansD.getText().toString());
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
    void checkAnswer(String userAnswer){
        if(userAnswer.equals(Answers[question_index])){
            score++;
            textViewScore.setText(String.valueOf(score));
            if(score>highScore){
                highScore=score;
                highScoreChanged=true;
                textViewHighScore.setText(String.valueOf(highScore));
                setHighScore();
            }
            Toast.makeText(this, R.string.right_answer, Toast.LENGTH_SHORT).show();
        }
        else {
            new AlertDialog.Builder(this).setTitle(R.string.gameover).setMessage(R.string.score2+score).show();
            score=0;
            textViewScore.setText(String.valueOf(score));
            setHighScore();

        }
    setQuestion();
    }
    void setQuestion(){
        Random random = new Random();
        question_index=random.nextInt(Drawblabes.length-1);
        int[] answers=new int[4];
        for(int i=0;i<3;i++) {
            int answer_index = random.nextInt(Drawblabes.length - 1);
            while (question_index == answer_index)
                answer_index = random.nextInt(Drawblabes.length - 1);
            answers[i]=answer_index;
        }
        answers[3]=question_index;
        shuffleArray(answers);
        imageViewQuestion.setImageResource(Drawblabes[question_index]);
        button_ansA.setText(Propositions[answers[0]]);
        button_ansB.setText(Propositions[answers[1]]);
        button_ansC.setText(Propositions[answers[2]]);
        button_ansD.setText(Propositions[answers[3]]);


    }
    private static void shuffleArray(int[] array) {
        Random random = new Random();
        for (int i = array.length - 1; i > 0; i--) {
            int index = random.nextInt(i + 1);
            // Swap array elements
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