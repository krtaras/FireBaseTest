package com.test.android.firebase;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class HomeScreen extends AppCompatActivity {

    private Button openDBActivity;
    private Button openStorageActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        this.openDBActivity = findViewById(R.id.openDBActivity);
        this.openStorageActivity = findViewById(R.id.openStorageActivity);

        initButtons();
    }

    private void initButtons() {
        this.openDBActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeScreen.this, DataBaseActivity.class);
                startActivity(intent);
            }
        });

        this.openStorageActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeScreen.this, StorageActivity.class);
                startActivity(intent);
            }
        });
    }

}
