package com.example.dck.snappie;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SendSnapActivity extends AppCompatActivity {

    ListView userListView;

    ArrayList<String> users;
    ArrayList<String> key;

    ArrayAdapter arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_snap);


        userListView = (ListView)findViewById(R.id.userListView);

        users = new ArrayList<>();
        key = new ArrayList<>();

        arrayAdapter =  new ArrayAdapter(this,android.R.layout.simple_list_item_1,users);
        userListView.setAdapter(arrayAdapter);

        FirebaseDatabase.getInstance("https://snapchat-7100e-default-rtdb.firebaseio.com/").getReference().child("users").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                String email = dataSnapshot.child("email").getValue().toString();
                users.add(email);
                key.add(dataSnapshot.getKey());
                arrayAdapter.notifyDataSetChanged();
            }
            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }
            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }
            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        userListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Map snapMap = new HashMap();
                snapMap.put("from", FirebaseAuth.getInstance().getCurrentUser().getEmail());
                snapMap.put("imageURL",getIntent().getStringExtra("imageURL"));
                snapMap.put("imageName",getIntent().getStringExtra("imageName"));

                FirebaseDatabase.getInstance("https://snapchat-7100e-default-rtdb.firebaseio.com/").getReference()
                        .child("users").child(key.get(i).toString()).child("snaps").push().setValue(snapMap);
                Intent intent = new Intent(SendSnapActivity.this,ChoosePhotoActivity.class);
                startActivity(intent);








            }
        });


    }

}
