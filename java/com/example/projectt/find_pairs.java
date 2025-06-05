package com.example.projectt;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Random;

public class find_pairs extends AppCompatActivity {
    String userEmail ="";

    ImageButton imageViewPairs1,imageViewPairs2,imageViewPairs3,imageViewPairs4,imageViewPairs5,imageViewPairs6,imageViewPairs7,imageViewPairs8,imageViewPairs9,imageViewPairs10,imageViewPairs11,imageViewPairs12;
    int[]Images;

    boolean anyOpenImage=false;
    int score=0;
    int idQuestionMark,openImage,openID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_pairs);
        userEmail = getIntent().getStringExtra("userEmail");
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getString(R.string.findpairs));

        imageViewPairs1 = findViewById(R.id.imageViewPairs1);
        imageViewPairs2 = findViewById(R.id.imageViewPairs2);
        imageViewPairs3 = findViewById(R.id.imageViewPairs3);
        imageViewPairs4 = findViewById(R.id.imageViewPairs4);
        imageViewPairs5 = findViewById(R.id.imageViewPairs5);
        imageViewPairs6 = findViewById(R.id.imageViewPairs6);
        imageViewPairs7 = findViewById(R.id.imageViewPairs7);
        imageViewPairs8 = findViewById(R.id.imageViewPairs8);
        imageViewPairs9 = findViewById(R.id.imageViewPairs9);
        imageViewPairs10 = findViewById(R.id.imageViewPairs10);
        imageViewPairs11 = findViewById(R.id.imageViewPairs11);
        imageViewPairs12 = findViewById(R.id.imageViewPairs12);


        idQuestionMark=R.drawable.icon_question_mark;
        Images=new int[]{R.drawable.icon_cow,R.drawable.icon_lion,R.drawable.icon_pug,R.drawable.icon_redpanda,R.drawable.icon_sloth,R.drawable.icon_wolf};
        imageViewPairs1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonClick((int)imageViewPairs1.getTag(),R.id.imageViewPairs1);
            }
        });
        imageViewPairs2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonClick((int)imageViewPairs2.getTag(),R.id.imageViewPairs2);
            }
        });
        imageViewPairs3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonClick((int)imageViewPairs3.getTag(),R.id.imageViewPairs3);
            }
        });
        imageViewPairs4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonClick((int)imageViewPairs4.getTag(),R.id.imageViewPairs4);
            }
        });
        imageViewPairs5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonClick((int)imageViewPairs5.getTag(),R.id.imageViewPairs5);
            }
        });
        imageViewPairs6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonClick((int)imageViewPairs6.getTag(),R.id.imageViewPairs6);
            }
        });
        imageViewPairs7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonClick((int)imageViewPairs7.getTag(),R.id.imageViewPairs7);
            }
        });
        imageViewPairs8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonClick((int)imageViewPairs8.getTag(),R.id.imageViewPairs8);
            }
        });
        imageViewPairs9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonClick((int)imageViewPairs9.getTag(),R.id.imageViewPairs9);
            }
        });
        imageViewPairs10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonClick((int)imageViewPairs10.getTag(),R.id.imageViewPairs10);
            }
        });
        imageViewPairs11.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonClick((int)imageViewPairs11.getTag(),R.id.imageViewPairs11);
            }
        });
        imageViewPairs12.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonClick((int)imageViewPairs12.getTag(),R.id.imageViewPairs12);
            }
        });

        setTags();
    }

    void setTags(){
        int[] indexes=new int[]{0,0,1,1,2,2,3,3,4,4,5,5};
        shuffleArray(indexes);
        imageViewPairs1.setTag(Images[indexes[0]]);
        imageViewPairs2.setTag(Images[indexes[1]]);
        imageViewPairs3.setTag(Images[indexes[2]]);
        imageViewPairs4.setTag(Images[indexes[3]]);
        imageViewPairs5.setTag(Images[indexes[4]]);
        imageViewPairs6.setTag(Images[indexes[5]]);
        imageViewPairs7.setTag(Images[indexes[6]]);
        imageViewPairs8.setTag(Images[indexes[7]]);
        imageViewPairs9.setTag(Images[indexes[8]]);
        imageViewPairs10.setTag(Images[indexes[9]]);
        imageViewPairs11.setTag(Images[indexes[10]]);
        imageViewPairs12.setTag(Images[indexes[11]]);
        imageViewPairs1.setImageResource(idQuestionMark);
        imageViewPairs2.setImageResource(idQuestionMark);
        imageViewPairs3.setImageResource(idQuestionMark);
        imageViewPairs4.setImageResource(idQuestionMark);
        imageViewPairs5.setImageResource(idQuestionMark);
        imageViewPairs6.setImageResource(idQuestionMark);
        imageViewPairs7.setImageResource(idQuestionMark);
        imageViewPairs8.setImageResource(idQuestionMark);
        imageViewPairs9.setImageResource(idQuestionMark);
        imageViewPairs10.setImageResource(idQuestionMark);
        imageViewPairs11.setImageResource(idQuestionMark);
        imageViewPairs12.setImageResource(idQuestionMark);
        imageViewPairs1.setVisibility(View.VISIBLE);
        imageViewPairs2.setVisibility(View.VISIBLE);
        imageViewPairs3.setVisibility(View.VISIBLE);
        imageViewPairs4.setVisibility(View.VISIBLE);
        imageViewPairs5.setVisibility(View.VISIBLE);
        imageViewPairs6.setVisibility(View.VISIBLE);
        imageViewPairs7.setVisibility(View.VISIBLE);
        imageViewPairs8.setVisibility(View.VISIBLE);
        imageViewPairs9.setVisibility(View.VISIBLE);
        imageViewPairs10.setVisibility(View.VISIBLE);
        imageViewPairs11.setVisibility(View.VISIBLE);
        imageViewPairs12.setVisibility(View.VISIBLE);

    }
    void buttonClick(int tag,int id){
        if(!anyOpenImage){
            Rotate(id,0);
            anyOpenImage=true;
            openImage=tag;
            openID=id;
        }
        else{
            anyOpenImage=false;

            if(tag==openImage){
                Rotate(id,3);
                score++;
                if(score==6){
                    score=0;
                    new AlertDialog.Builder(this).setTitle(R.string.victory).setMessage(R.string.youwon).show();
                    setTags();

                }

            }
            else{
                Rotate(id,2);
            }
        }

    }
    void Rotate(int id,int mode){
        //mode 0          ?==>image
        //mode 1      image==>?
        //mode 2      ?==>image==>?
        //mode 3   ?==>image then invisible
        ImageButton imageButton = findViewById(id);
        ObjectAnimator flipAnimator = ObjectAnimator.ofFloat(imageButton, "rotationY", 0f, 90f);
        flipAnimator.setDuration(500);
        flipAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        flipAnimator.start();
        ObjectAnimator flipAnimator2 = ObjectAnimator.ofFloat(imageButton, "rotationY", 270f, 360f);
        flipAnimator2.setDuration(500);
        flipAnimator2.setInterpolator(new AccelerateDecelerateInterpolator());
        flipAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                // Animation started
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                // Animation ended
                if(mode==0||mode==2)imageButton.setImageResource((int)imageButton.getTag());

                else if(mode==1) imageButton.setImageResource(idQuestionMark);
                imageButton.setVisibility(View.INVISIBLE);
                imageButton.setRotationY(270f);
                imageButton.setVisibility(View.VISIBLE);
                flipAnimator2.start();

            }

            @Override
            public void onAnimationCancel(Animator animation) {
                // Animation cancelled
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
                // Animation repeated
            }
        });
        flipAnimator2.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                // Animation started
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                // Animation ended
                if(mode==3){
                    ImageButton imgb=findViewById(id);
                    imgb.setVisibility(View.INVISIBLE);
                    ImageButton imgb2=findViewById(openID);
                    imgb2.setVisibility(View.INVISIBLE);
                }
                if(mode==2){
                    Rotate(openID,1);
                    Rotate(id,1);
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                // Animation cancelled
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
                // Animation repeated
            }
        });


    }
    void setImageButtonImage(int id,int image){
        ImageButton imageButton=findViewById(id);
        imageButton.setImageResource(image);
        imageButton.setTag(image);
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