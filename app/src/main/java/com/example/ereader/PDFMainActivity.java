package com.example.ereader;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class PDFMainActivity extends AppCompatActivity {

    FloatingActionButton fb;
    RecyclerView recview;
    myadapter adapter;
    DatabaseReference likereference;
    Boolean testclick=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_p_d_f_main);

        likereference=FirebaseDatabase.getInstance().getReference("likes");

        fb=(FloatingActionButton)findViewById(R.id.floatingActionButton);
        fb.setOnClickListener(view -> {
            startActivity(new Intent(getApplicationContext(), uploadfile.class));
        });

        recview = (RecyclerView) findViewById(R.id.recview);
        recview.setLayoutManager(new LinearLayoutManager(this));

        FirebaseRecyclerOptions<model> options =
                new FirebaseRecyclerOptions.Builder<model>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("mydocuments"), model.class)
                        .build();
        adapter=new myadapter(options, likereference, testclick);
        recview.setAdapter(adapter);

    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}