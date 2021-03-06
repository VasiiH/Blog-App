package com.vasi.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.vasi.project2.HideActivity;
import com.vasi.project2.R;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseUser mUser;
    private Button loginButton;
    private Button createActButton;
    private EditText emailField;
    private EditText passwordField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();

        loginButton = (Button) findViewById(R.id.btn_login);
        createActButton = (Button) findViewById(R.id.btn_create_account);
        emailField = (EditText) findViewById(R.id.et_email);
        passwordField = (EditText) findViewById(R.id.et_password);


        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                mUser = firebaseAuth.getCurrentUser();

                if (mUser != null){
                    Toast.makeText(LoginActivity.this,"Signed In",Toast.LENGTH_SHORT)
                            .show();
                    startActivity(new Intent(LoginActivity.this, PostListActivity.class));
                    finish();
                }
                else{
                    Toast.makeText(LoginActivity.this,"Not Signed In",Toast.LENGTH_SHORT)
                            .show();
                }
            }
        };

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                  if (!TextUtils.isEmpty(emailField.getText().toString())
                          && !TextUtils.isEmpty(passwordField.getText().toString())){

                      String email = emailField.getText().toString();
                      String pwd = passwordField.getText().toString();

                      login(email,pwd);
                  }
                  else{

                  }
            }
        });


    }

    private void login(String email, String pwd) {
        mAuth.signInWithEmailAndPassword(email,pwd)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(LoginActivity.this,"SInged In",Toast.LENGTH_SHORT)
                                    .show();

                            startActivity(new Intent(LoginActivity.this, HideActivity.class));
                            finish();
                        }
                        else {
                            Toast.makeText(LoginActivity.this,"NOt Singed In",Toast.LENGTH_SHORT)
                                    .show();
                        }
                    }
                });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_signout){
            mAuth.signOut();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mAuthListener != null){
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }
}
