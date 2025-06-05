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
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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

public class spelling_practice extends AppCompatActivity {
    String userEmail ="";
    final String PrefKey="spelling",TAG=PrefKey;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    TextView textViewScore, textViewHighScore;
    ImageView imageViewQuestion;
    Button buttonSubmit;
    EditText editTextAnswer;
    int[]images;

    String[]animals;
    int questionIndex,score=0,highScore=0;
    boolean highScoreChanged=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spelling_practice);
        userEmail = getIntent().getStringExtra("userEmail");
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getString(R.string.practice));

        textViewScore=findViewById(R.id.textView_spelling_practice_score);
        textViewHighScore=findViewById(R.id.textView_spelling_prac_high_score);
        imageViewQuestion=findViewById(R.id.imageView_spelling_prac_question);
        buttonSubmit=findViewById(R.id.button_spelling_prac_play);
        editTextAnswer=findViewById(R.id.editText_spelling_prac_answer);

        images=new int[]{R.drawable.bird,R.drawable.bull,R.drawable.cat,R.drawable.cow,R.drawable.crab,R.drawable.dog,R.drawable.dragon,R.drawable.duck,R.drawable.elephant,R.drawable.fox,R.drawable.frog,R.drawable.horse,R.drawable.monkey,R.drawable.panda,R.drawable.penguin};
        animals = new String[]{getString(R.string.bird).toString(),getString(R.string.bull).toString(),getString(R.string.cat).toString(),getString(R.string.cow),getString(R.string.crab),getString(R.string.dog),getString(R.string.dragon),getString(R.string.duck),getString(R.string.elephant),getString(R.string.fox),getString(R.string.frog),getString(R.string.horse),getString(R.string.monkey),getString(R.string.panda),getString(R.string.penguin)};

        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer();

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
    void setQuestion(){
        Random random = new Random();
        questionIndex=random.nextInt(15);
        imageViewQuestion.setImageResource(images[questionIndex]);
        editTextAnswer.setText("");
    }
    void checkAnswer(){
        String userAnswer=editTextAnswer.getText().toString();
        if(userAnswer.equalsIgnoreCase(animals[questionIndex])){
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
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}