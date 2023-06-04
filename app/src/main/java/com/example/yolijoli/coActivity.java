package com.example.yolijoli;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.PopupMenu;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;

public class coActivity extends AppCompatActivity {

    FloatingActionButton addNoteBtn;
    RecyclerView recyclerView;
    ImageButton menuBtn, refreshBtn;
    coAdapter noteAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_co);

        addNoteBtn = findViewById(R.id.add_co_btn);
        recyclerView = findViewById(R.id.co_recyler_view);
        menuBtn = findViewById(R.id.co_menu_btn);
        refreshBtn = findViewById(R.id.co_refresh);


        addNoteBtn.setOnClickListener((v)-> startActivity(new Intent(coActivity.this,coDetailsActivity.class)) );
        menuBtn.setOnClickListener((v)->showMenu() );
        setupRecyclerView();

        refreshBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(getIntent());
                finish();
                overridePendingTransition(0,0);
            }
        });
    }
    void showMenu(){
        PopupMenu popupMenu  = new PopupMenu(coActivity.this,menuBtn);
        popupMenu.getMenu().add("설정");
        popupMenu.show();
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                if(menuItem.getTitle()=="설정"){
                    startActivity(new Intent(coActivity.this,settingActivity.class));
                    return true;
                }
                return false;
            }
        });

    }

    void setupRecyclerView(){
        Query query  = Utility.getCollectionReferenceForNotes().orderBy("timest",Query.Direction.DESCENDING);
        FirestoreRecyclerOptions<coInfo> options = new FirestoreRecyclerOptions.Builder<coInfo>()
                .setQuery(query,coInfo.class).build();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        noteAdapter = new coAdapter(options,this);
        recyclerView.setAdapter(noteAdapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        noteAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        noteAdapter.stopListening();
    }

    @Override
    protected void onResume() {
        super.onResume();
        noteAdapter.notifyDataSetChanged();
    }
}