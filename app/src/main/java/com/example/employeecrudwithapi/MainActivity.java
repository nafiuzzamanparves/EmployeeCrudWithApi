package com.example.employeecrudwithapi;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.employeecrudwithapi.activity.AddEmployeeActivity;
import com.example.employeecrudwithapi.activity.EmployeeListActivity;

public class MainActivity extends AppCompatActivity {

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

        Button btnAddEmployee = findViewById(R.id.btnAddEmployee);
        Button btnListEmployee = findViewById(R.id.btnListEmployee);

        btnAddEmployee.setOnClickListener(v -> navigateToAddEmployeePage());
        btnListEmployee.setOnClickListener(v -> navigateToEmployeeListPage());
    }

    private void navigateToAddEmployeePage() {
        Intent intent = new Intent(MainActivity.this, AddEmployeeActivity.class);
        startActivity(intent);
    }

    private void navigateToEmployeeListPage() {
        Intent intent = new Intent(MainActivity.this, EmployeeListActivity.class);
        startActivity(intent);
    }

}