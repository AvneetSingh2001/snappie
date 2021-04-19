package com.example.dck.snappie;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class SignInActivity extends AppCompatActivity {

    EditText emailEditText;
    EditText passwordEditText;
    FirebaseAuth mAuth= FirebaseAuth.getInstance();
    boolean cancel;
    View focusView;

    public void signUpClicked(View view){

        cancel = false;
        focusView = null;

        if (emailEditText.getText().toString().isEmpty() || passwordEditText.getText().toString().isEmpty()){
            emailEditText.setError("Required field");
            passwordEditText.setError("Required field");
            focusView = emailEditText;
            cancel = true;


        }

        if (!cancel){

            mAuth.createUserWithEmailAndPassword(emailEditText.getText().toString(), passwordEditText.getText().toString())
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                FirebaseDatabase.getInstance("https://snapchat-7100e-default-rtdb.firebaseio.com/").getReference().child("users").child(task.getResult().getUser().getUid())
                                        .child("email").setValue(emailEditText.getText().toString());
                                login();


                            } else {
                                // If sign in fails, display a message to the user.
                                Toast.makeText(SignInActivity.this, "Invalid username and password", Toast.LENGTH_SHORT).show();
                                emailEditText.setError("invalid");
                                passwordEditText.setError("invalid");

                            }

                            // ...
                        }
                    });


        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);



        emailEditText = (EditText) findViewById(R.id.emailEditText);
        passwordEditText = (EditText)findViewById(R.id.passwordEditText);


    }

    public void login(){

        Toast.makeText(this, "Sign up done!\n" +
                "login to open account", Toast.LENGTH_LONG).show();
        finish();

    }
}
