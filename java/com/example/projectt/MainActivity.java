package com.example.projectt;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    EditText username;
    EditText editTextPassword;
    Button login;
    Button register;
    private FirebaseAuth mAuth;
    final String TAG="fBaseAuth";
    String email, password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();

        username = findViewById(R.id.Username);
        editTextPassword = findViewById(R.id.Password);
        login = findViewById(R.id.button_login);
        register = findViewById(R.id.button_register);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle(getString(R.string.loginpage));

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email=username.getText().toString();
                password = editTextPassword.getText().toString();
                loginCheck();
            }
        });
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email=username.getText().toString();
                password = editTextPassword.getText().toString();
                registerCheck();
            }
        });
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
    boolean isEmpty(EditText textBox){
        CharSequence str=textBox.getText().toString();
        return TextUtils.isEmpty(str);
    }
    void register (String email,String password){
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(MainActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }
                    }
                });
        loginCheck();
    }
    void updateUI(FirebaseUser user){
        if(user!=null){
            Intent intent = new Intent(getApplicationContext(), MainMenu.class);
            intent.putExtra("userEmail",user.getEmail().toString());
            startActivity(intent);
        }
        else{
            username.setText("");
            editTextPassword.setText("");
        }
    }
    void loginCheck(){
        boolean error=false;
        if(email.equals("")){
            username.setError("Username can't be empty");
            error=true;
        }
        if(password.equals("")){
            editTextPassword.setError("Password can't be empty");
            error=true;
        }
        if(password.length()<6){
            editTextPassword.setError("Password can't be less than 6 characters");
            error=true;
        }
        if(!error){
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d(TAG, "signInWithEmail:success");
                                FirebaseUser user = mAuth.getCurrentUser();
                                updateUI(user);
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w(TAG, "signInWithEmail:failure", task.getException());
                                Toast.makeText(MainActivity.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                                updateUI(null);
                            }
                        }
                    });
            /*
            SharedPreferences users = getSharedPreferences("Users", Context.MODE_PRIVATE);
            if(users.getString(username.getText().toString(), "").equals(password.getText().toString())) {
                Toast t = Toast.makeText(this,"Logged in to user : "+username.getText().toString(),Toast.LENGTH_SHORT);
                t.show();
                SharedPreferences logged_in = getSharedPreferences("User", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = logged_in.edit();
                editor.putString("User",username.getText().toString());
                editor.apply();
                Intent intent = new Intent(getApplicationContext(), MainMenu.class);
                startActivity(intent);
            }
            else{
                Toast t = Toast.makeText(this,"Wrong credentials",Toast.LENGTH_SHORT);
                t.show();

            }*/
        }
    }
    void registerCheck(){
        boolean error=false;
        if(isEmpty(username)){
            username.setError("Username can't be empty");
            error=true;
        }
        if(isEmpty(editTextPassword)){
            editTextPassword.setError("Password can't be empty");
            error=true;
        }
        if(password.length()<6){
            editTextPassword.setError("Password can't be less than 6 characters");
            error=true;
        }
        if(!error){
            register(username.getText().toString(), editTextPassword.getText().toString());
            /*
            SharedPreferences users = getSharedPreferences("Users", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = users.edit();
            editor.putString(username.getText().toString(),password.getText().toString());
            editor.apply();
            SharedPreferences logged_in = getSharedPreferences("User", Context.MODE_PRIVATE);
            SharedPreferences.Editor editorr = logged_in.edit();
            editorr.putString("User",username.getText().toString());
            editorr.apply();
            Toast t = Toast.makeText(this,"Register success.",Toast.LENGTH_SHORT);
            t.show();*/

    }   }
}