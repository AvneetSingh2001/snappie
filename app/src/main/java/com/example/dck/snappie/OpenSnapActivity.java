package com.example.dck.snappie;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutionException;

public class OpenSnapActivity extends AppCompatActivity {

    ImageView imageView;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_snap);


        imageView = (ImageView)findViewById(R.id.imageView);
        
        DownloadTask task = new DownloadTask();
        Bitmap image;

        try {
            image = task.execute(getIntent().getStringExtra("imageURL")).get();
            imageView.setImageBitmap(image);

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "failed", Toast.LENGTH_SHORT).show();
        }

        
        
        
    }

    public class DownloadTask extends AsyncTask<String, Void , Bitmap>{

        @Override
        protected Bitmap doInBackground(String... urls) {

            try {
                URL url = new URL(urls[0]);

                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                
                httpURLConnection.connect();

                InputStream in = httpURLConnection.getInputStream();

                Bitmap bitmap = BitmapFactory.decodeStream(in);
                
                return bitmap;
                
                
            } catch (Exception e) {
                e.printStackTrace();
                return null;

            }


        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        FirebaseDatabase.getInstance("https://snapchat-7100e-default-rtdb.firebaseio.com/").getReference()
                .child("users").child(mAuth.getCurrentUser().getUid()).child("snaps").child(getIntent().getStringExtra("snapKey")).removeValue();

        FirebaseStorage.getInstance().getReference().child("images").child(getIntent().getStringExtra("imageName")).delete();
    }
}
