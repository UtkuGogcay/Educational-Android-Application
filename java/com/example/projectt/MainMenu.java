package com.example.projectt;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

public class MainMenu extends AppCompatActivity {
    ImageButton analog_clocks,digital_clocks,multiplication,memory,directions,spelling,days,months,seasons,pairs;
    String TAG="tag";
    String userEmail ="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_menu);
        userEmail = getIntent().getStringExtra("userEmail");
        analog_clocks = findViewById(R.id.analog_clocks);
        digital_clocks = findViewById(R.id.ibutton_digital_clock);
        multiplication = findViewById(R.id.ibutton_multiplication);
        directions=findViewById(R.id.ibutton_directions);
        memory = findViewById(R.id.ibutton_memory);
        spelling=findViewById(R.id.ibutton_spelling);
        days=findViewById(R.id.ibutton_days);
        months=findViewById(R.id.ibutton_months);
        seasons=findViewById(R.id.ibutton_seasons);
        pairs=findViewById(R.id.ibutton_pairs);
        analog_clocks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), analog_clock.class);
                intent.putExtra("userEmail",userEmail);
                startActivity(intent);
            }

        });
        digital_clocks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), digital_clock.class);
                intent.putExtra("userEmail",userEmail);
                startActivity(intent);
            }

        });
        multiplication.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), multiplication.class);
                intent.putExtra("userEmail",userEmail);// TODO
                startActivity(intent);
            }

        });
        memory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), memory.class);
                intent.putExtra("userEmail",userEmail);// TODO
                startActivity(intent);
            }

        });
        directions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),com.example.projectt.directions.class);
                intent.putExtra("userEmail",userEmail);
                startActivity(intent);
            }

        });
        spelling.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),spelling_practice.class);
                intent.putExtra("userEmail",userEmail);
                startActivity(intent);
            }

        });
        days.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),days.class);
                intent.putExtra("userEmail",userEmail);
                startActivity(intent);
            }

        });
        months.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),months.class);
                intent.putExtra("userEmail",userEmail);
                startActivity(intent);
            }

        });
        seasons.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),find_pairs.class);
                intent.putExtra("userEmail",userEmail);
                startActivity(intent);
            }

        });
        pairs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), find_pairs.class);
                intent.putExtra("userEmail",userEmail);
                startActivity(intent);
            }

        });
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar_main_menu);
        setSupportActionBar(myToolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle(getString(R.string.mainmenu));
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_toolbar, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_button1) {
            Locale locale = new Locale("tr", "TR");
            Configuration configuration = getResources().getConfiguration();
            configuration.setLocale(locale);
            getResources().updateConfiguration(configuration, getResources().getDisplayMetrics());
            recreate();
            return true;
        } else if (id == R.id.action_button2) {
            Locale locale = new Locale("en", "US");
            Configuration configuration = getResources().getConfiguration();
            configuration.setLocale(locale);
            getResources().updateConfiguration(configuration, getResources().getDisplayMetrics());
            recreate();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public void  onBackPressed(){}
}
