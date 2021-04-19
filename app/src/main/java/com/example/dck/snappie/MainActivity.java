package com.example.dck.snappie;

import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
//import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class MainActivity extends AppCompatActivity {

    EditText emailEditText;
    EditText passwordEditText;
    View focusView;
    boolean cancel;
    FirebaseAuth mAuth= FirebaseAuth.getInstance();
    private GoogleSignInClient mGoogleSignInClient;
    private final static int RC_SIGN_IN = 123;

    //FirebaseAuth mAuth;

    public void signUpClicked(View view){
        emailEditText.setText("");
        passwordEditText.setText("");
        Intent intent = new Intent(MainActivity.this,SignInActivity.class);
        startActivity(intent);

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        emailEditText = (EditText)findViewById(R.id.emailEditText);
        passwordEditText = (EditText)findViewById(R.id.passwordEditText);


        // Initialize Firebase Auth
        createRequest();

        if (mAuth.getCurrentUser() != null){

            login();

        }



    }

    public void googleSignInClicked(View view){

        googleSignIn();

    }

    public void signInClicked(View view){

        cancel = false;
        focusView = null;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
            if (emailEditText.getText().toString().isEmpty() || passwordEditText.getText().toString().isEmpty()){
    
    
                emailEditText.setError("Required field");
                passwordEditText.setError("Required field");
                focusView = emailEditText;
                cancel = true;
    
            }
        }

        if (!cancel){

            mAuth.signInWithEmailAndPassword(emailEditText.getText().toString(), passwordEditText.getText().toString())
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                login();

                            } else {

                                Toast.makeText(MainActivity.this, "Invalid username and password", Toast.LENGTH_SHORT).show();
                                emailEditText.setError("invalid");
                                passwordEditText.setError("invalid");

                            }

                            // ...
                        }
                    });



        }
        //login();




    }

    public void login(){
        Toast.makeText(this, "login", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(MainActivity.this,ChoosePhotoActivity.class);
        startActivity(intent);


    }

    public void createRequest(){
        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_clident_id))
                .requestEmail()
                .build();

        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);






    }

    private void googleSignIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                // Log.d(TAG, "firebaseAuthWithGoogle:" + account.getId());
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                //Log.w(TAG, "Google sign in failed", e);
                // ...
                Toast.makeText(this, "failed 1", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            //Log.d(TAG, "signInWithCredential:success");
                            login();

                        } else {
                            Toast.makeText(MainActivity.this, "failed 2", Toast.LENGTH_SHORT).show();

                        }

                        // ...
                    }
                });
    }






}
