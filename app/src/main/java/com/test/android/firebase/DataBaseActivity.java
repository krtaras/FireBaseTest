package com.test.android.firebase;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class DataBaseActivity extends AppCompatActivity {

    private TextView saveView;
    private TextView loadingView;

    private EditText nameInput;
    private EditText descriptionInput;

    private TextView nameView;
    private TextView descriptionView;

    private Button saveToDB;
    private Button loadFromDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_base);

        createComponents();
        initComponents();
    }

    private void createComponents() {
        this.saveView = findViewById(R.id.saveView);
        this.loadingView = findViewById(R.id.loadingView);

        this.nameInput = findViewById(R.id.nameInput);
        this.descriptionInput = findViewById(R.id.descriptionInput);

        this.nameView = findViewById(R.id.nameView);
        this.descriptionView = findViewById(R.id.descriptionView);

        this.saveToDB = findViewById(R.id.saveToDbBtn);
        this.loadFromDB = findViewById(R.id.loadFromDbBtn);
    }

    private void initComponents() {
        initSaveToDbBtn();
        initLoadFromDbBtn();
    }

    private void initSaveToDbBtn() {

        this.saveToDB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                saveView.setText("Start Saving");

                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference();
                myRef.child("quests").child("quest").child("name").setValue(nameInput.getText().toString());
                myRef.child("quests").child("quest").child("description").setValue(descriptionInput.getText().toString());
                myRef.child("quests").child("quest").child("image").setValue("image name");

                saveView.setText("Data Saved!");
            }
        });
    }

    private void initLoadFromDbBtn() {

        this.loadFromDB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                loadingView.setText("Start Loading");

                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference("quests/");
                Query phoneQuery = myRef.orderByKey();
                phoneQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for(DataSnapshot singleSnapshot : dataSnapshot.getChildren()){
                            Quest quest = singleSnapshot.getValue(Quest.class);
                            nameView.setText(quest.getName());
                            descriptionView.setText(quest.getDescription());
                        }
                        loadingView.setText("Data loaded!");
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });
    }
}
