package com.example.dck.snappie;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class MySnapActivity extends AppCompatActivity {

    ListView listView;

    ArrayList<String> emails;
    ArrayAdapter arrayAdapter;
    ArrayList<DataSnapshot> snaps;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_snap);


        listView = (ListView)findViewById(R.id.listView);
        emails = new ArrayList<String>();
        snaps = new ArrayList<>();
        arrayAdapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1,emails);
        listView.setAdapter(arrayAdapter);

        FirebaseDatabase.getInstance("https://snapchat-7100e-default-rtdb.firebaseio.com/").getReference()
                .child("users").child(mAuth.getCurrentUser().getUid()).child("snaps").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                String user = dataSnapshot.child("from").getValue().toString();
                emails.add(user);
                snaps.add(dataSnapshot);
                arrayAdapter.notifyDataSetChanged();
            }
            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {}
            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

                String key = dataSnapshot.getKey().toString();

                int index = 0;

                for (DataSnapshot snap : snaps){
                    if (key.equals(snap.getKey().toString())){
                        Toast.makeText(MySnapActivity.this, "Snap deleted", Toast.LENGTH_SHORT).show();
                        snaps.remove(index);
                        emails.remove(index);
                        break;
                    }
                    index++;
                }

                arrayAdapter.notifyDataSetChanged();


            }
            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {}
            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                DataSnapshot snapshot = snaps.get(i);
                Intent intent = new Intent(MySnapActivity.this,OpenSnapActivity.class);
                intent.putExtra("imageName",snapshot.child("imageName").getValue().toString());
                intent.putExtra("imageURL",snapshot.child("imageURL").getValue().toString());
                intent.putExtra("snapKey",snapshot.getKey());
                startActivity(intent);
            }
        });



    }
}
