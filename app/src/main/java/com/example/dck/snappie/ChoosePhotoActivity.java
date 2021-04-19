package com.example.dck.snappie;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.PopupMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class ChoosePhotoActivity extends AppCompatActivity {

    static Uri image;
    static Bitmap bitmap;
    static int flag = 0;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.AppTheme);
        setContentView(R.layout.activity_choose_photo);



    }



    public void galleryClicked(View view) {

        if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        } else {
            pickImage();
        }
    }
    public void pickImage() {
        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, 1);
    }


    public void cameraClicked(View view) {

        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, 0);

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode , data);
        if (requestCode == 0 && data != null) {
            flag = 0;

            Bitmap image = (Bitmap) data.getExtras().get("data");

            Intent intent = new Intent(ChoosePhotoActivity.this, PhotoActivity.class);
            intent.putExtra("image", image);
            startActivity(intent);
        }
        else if (requestCode == 1 && resultCode == RESULT_OK && data!= null){
            image = data.getData();
            flag = 1;
            Toast.makeText(this, "uploadingg", Toast.LENGTH_SHORT).show();

            try {
                Toast.makeText(this, "uploaded", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(ChoosePhotoActivity.this, PhotoActivity.class);
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(),image);

                startActivity(intent);

            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(this, "failed uploading", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void mySnapClicked(View view) {

        Intent intent = new Intent(ChoosePhotoActivity.this,MySnapActivity.class);
        startActivity(intent);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.signout_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        super.onOptionsItemSelected(item);


        switch (item.getItemId()) {
            case R.id.signOut:
                mAuth.signOut();
                Toast.makeText(this, "Sign out successfull", Toast.LENGTH_SHORT).show();
                finish();
                return true;

            default:
                return false;
        }

    }


    public void onBackPressed(){

        finishAffinity();

    }

}
