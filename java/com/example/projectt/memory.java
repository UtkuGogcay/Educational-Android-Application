package com.example.projectt;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class memory extends AppCompatActivity {

    Button memory_fwd,memory_rvs;
    String userEmail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memory);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getString(R.string.memory));
        userEmail = getIntent().getStringExtra("userEmail");

        memory_fwd=findViewById(R.id.button_memory_practice_forward);
        memory_rvs=findViewById(R.id.button_memory_practice_reverse);
        memory_fwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), memory_practice.class);
                intent.putExtra("choice","forward");
                intent.putExtra("userEmail",userEmail);

                startActivity(intent);
            }
        });
        memory_rvs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), memory_practice.class);
                intent.putExtra("choice","reverse");
                intent.putExtra("userEmail",userEmail);

                startActivity(intent);
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
