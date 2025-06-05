package com.example.projectt;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

public class multiplication_practice extends AppCompatActivity {
    String userEmail = "";
    final String PrefKey="mult",TAG=PrefKey;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    Button start;
    TextView textViewScore, TextViewQuestion,textViewHighScore;
    EditText editTextAnswer;
    String question;

    final String dbField="multiplication";
    boolean highScoreChanged=false;

    int score=0,answer,highScore=0,index=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multiplication_practice);
        userEmail = getIntent().getStringExtra("userEmail");
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getString(R.string.multiplication));

        start=findViewById(R.id.button_mult_prac_start);
        textViewScore=findViewById(R.id.textView_multiplication_practice_score);
        TextViewQuestion =findViewById(R.id.textView_mult_prac_question);
        editTextAnswer=findViewById(R.id.editTextNumber_mult_prac_answer);
        textViewHighScore=findViewById(R.id.textView_mult_prac_high_score);


        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(index==0){
                    editTextAnswer.setVisibility(View.VISIBLE);
                    start.setText(R.string.submit);
                    setQuestion();
                    index++;
                }
                else{
                    index++;

                    int userAnswer= Integer.parseInt( editTextAnswer.getText().toString());
                    if(answer==userAnswer){
                        rightAnswer();
                        if(highScore<score){
                            highScoreChanged=true;
                            highScore=score;
                            textViewHighScore.setText(String.valueOf(highScore));
                        };
                    }
                    else{
                        wrongAnswer();
                    }
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
        setQuestion();

        Toast t = Toast.makeText(this,R.string.right_answer,Toast.LENGTH_SHORT);
        t.show();
    }
    void wrongAnswer(){
        if(highScoreChanged)
            setHighScore();
        editTextAnswer.setVisibility(View.INVISIBLE);
        index=0;
        textViewScore.setText("0");
        TextViewQuestion.setText("");
        start.setText(R.string.start);
        new AlertDialog.Builder(this).setTitle(R.string.wrong_answer).setMessage(R.string.score2+score).show();
        score=0;

    }
    void setQuestion(){

            Random random= new Random();
            int multiplicand = random.nextInt(9)+2;
            int multiplier = random.nextInt(9)+2;
            answer=multiplicand*multiplier;
            question= multiplicand +"x"+Integer.toString(multiplier);
            TextViewQuestion.setText(question);
            editTextAnswer.setText("");

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