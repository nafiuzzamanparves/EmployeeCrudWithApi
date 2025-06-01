package com.example.employeecrudwithapi;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.employeecrudwithapi.activity.SearchableGridViewActivity;
import com.example.employeecrudwithapi.activity.SearchableListViewActivity;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Button gridView = findViewById(R.id.gridView);
        Button listView = findViewById(R.id.listView);

        gridView.setOnClickListener(v -> openSearchableGridView());
        listView.setOnClickListener(v -> openSearchableListView());
    }

    private void openSearchableGridView() {
        Intent intent = new Intent(this, SearchableGridViewActivity.class);
        startActivity(intent);
    }

    private void openSearchableListView() {
        Intent intent = new Intent(this, SearchableListViewActivity.class);
        startActivity(intent);
    }
}