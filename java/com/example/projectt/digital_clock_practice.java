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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Random;


public class digital_clock_practice extends AppCompatActivity {
    String userEmail ="";
    final String PrefKey="digital",TAG=PrefKey;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    int highScore,score=0,questionIndex;
    Button buttonAnsA,buttonAnsB,buttonAnsC,buttonAnsD;
    TextView textViewScore, textViewHighScore;
    ImageView imageViewQuestion;
    boolean highScoreChanged=false;
    int[] images;
    int[] answers;
    String[] possibleHour;
    String[] possibleMinute;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_digital_clock_practice);
        userEmail = getIntent().getStringExtra("userEmail");
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getString(R.string.digital_clock));

        images=new int[]{R.drawable.d_eight_ten,R.drawable.d_four_thirty,R.drawable.d_six_twenty,R.drawable.d_ten_fifty,R.drawable.d_ten_thirty,R.drawable.d_three,R.drawable.d_two_forty};
        answers=new int[]{71,33,52,95,93,20,14};
        possibleHour = new String[]{
                getString(R.string.one),
                getString(R.string.two),
                getString(R.string.three),
                getString(R.string.four),
                getString(R.string.five),
                getString(R.string.six),
                getString(R.string.seven),
                getString(R.string.eight),
                getString(R.string.nine),
                getString(R.string.ten),
                getString(R.string.eleven),
                getString(R.string.twelve)
        };
        possibleMinute = new String[]{
                getString(R.string.oclock),
                getString(R.string.ten),
                getString(R.string.twenty),
                getString(R.string.thirty),
                getString(R.string.forty),
                getString(R.string.fifty),
        };
        buttonAnsA=findViewById(R.id.button_digital_clock_prac_ansA);
        buttonAnsB=findViewById(R.id.button_digital_clock_prac_ansB);
        buttonAnsC=findViewById(R.id.button_digital_clock_prac_ansC);
        buttonAnsD=findViewById(R.id.button_digital_clock_prac_ansD);
        textViewScore=findViewById(R.id.textView_digital_clock_practice_score);
        textViewHighScore=findViewById(R.id.textView_digital_clock_prac_high_score);
        imageViewQuestion=findViewById(R.id.imageView_digital_clock_prac_question);
        getHighScore();
        buttonAnsA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer(String.valueOf( buttonAnsA.getTag()));
            }

        });
        buttonAnsB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)  {
                checkAnswer(String.valueOf( buttonAnsB.getTag()));
            }
        });
        buttonAnsC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer(String.valueOf( buttonAnsC.getTag()));
            }

        });

        buttonAnsD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer(String.valueOf( buttonAnsD.getTag()));
            }

        });
        setQuestion();
    }

    private void checkAnswer(String tag) {
        if(answers[questionIndex]== Integer.parseInt(tag)){
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
        Random random =new Random(System.currentTimeMillis());
        int[] q=new int[4];
        questionIndex=random.nextInt(7);
        q[0]= 10*(random.nextInt(9)+1)+random.nextInt(6);
        q[1]= 10*(random.nextInt(9)+1)+random.nextInt(6);
        q[2]= 10*(random.nextInt(9)+1)+random.nextInt(6);
        q[3]=answers[questionIndex];
        shuffleArray(q);
        buttonAnsA.setText(parseText(q[0]));
        buttonAnsA.setTag(q[0]);
        buttonAnsB.setText(parseText(q[1]));
        buttonAnsB.setTag(q[1]);
        buttonAnsC.setText(parseText(q[2]));
        buttonAnsC.setTag(q[2]);
        buttonAnsD.setText(parseText(q[3]));
        buttonAnsD.setTag(q[3]);
        imageViewQuestion.setImageResource(images[questionIndex]);




    }
    String parseText(int i){
        int firstIndex=i%10;
        i/=10;
        int secondIndex=i%10;
        String a=possibleHour[secondIndex];
        a+=" ";
        if(firstIndex!=0)a+=getString(R.string.past)+" ";
        a+=possibleMinute[firstIndex];
        return a;
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