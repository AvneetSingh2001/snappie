package com.example.dck.snappie;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.UUID;

public class PhotoActivity extends AppCompatActivity {
    Bitmap bitmap;
    ImageView imageView;
    String imageName = UUID.randomUUID().toString() + ".jpg";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);

        imageView = (ImageView)findViewById(R.id.imageView);

        if (ChoosePhotoActivity.flag == 0){
            bitmap = (Bitmap) getIntent().getParcelableExtra("image");
            imageView.setImageBitmap(bitmap);

        }
        else if (ChoosePhotoActivity.flag == 1){
            imageView.setImageBitmap(ChoosePhotoActivity.bitmap);
        }




    }

    public void sendButoonClicked(View view){

        //Toast.makeText(this, "button tapped" , Toast.LENGTH_SHORT).show();

        // Get the data from an ImageView as bytes
        imageView.setDrawingCacheEnabled(true);
        imageView.buildDrawingCache();
        Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = FirebaseStorage.getInstance().getReference().child("images").child(imageName).putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
                Toast.makeText(PhotoActivity.this, "unable to send", Toast.LENGTH_SHORT).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                // ...
                Uri downloadUrl = taskSnapshot.getDownloadUrl();

                Intent intent = new Intent(PhotoActivity.this,SendSnapActivity.class);
                intent.putExtra("imageName",imageName);
                intent.putExtra("imageURL",downloadUrl.toString());
                startActivity(intent);


                Toast.makeText(PhotoActivity.this, "uploaded", Toast.LENGTH_SHORT).show();
            }
        });


    }

    public void editButtonClicked(View view){


    }


}
