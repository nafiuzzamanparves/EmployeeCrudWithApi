package com.example.employeecrudwithapi.activity;

import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.employeecrudwithapi.R;
import com.example.employeecrudwithapi.adapter.EmployeeAdapter;
import com.example.employeecrudwithapi.model.Employee;
import com.example.employeecrudwithapi.service.ApiService;
import com.example.employeecrudwithapi.util.ApiClient;
import com.example.employeecrudwithapi.util.EmployeeDiffCallback;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EmployeeListActivity extends AppCompatActivity {

    private static final String TAG = "EmployeeListActivity";
    private RecyclerView recyclerView;
    private EmployeeAdapter employeeAdapter;
    private List<Employee> employeeList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_employee_list);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        // Enable the Up button (back arrow) in the ActionBar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        employeeAdapter = new EmployeeAdapter(this, employeeList);
        recyclerView = findViewById(R.id.employeeRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(employeeAdapter);

        fetchEmployees();
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    private void fetchEmployees() {
        ApiService apiService = ApiClient.getApiService();
        Call<List<Employee>> call = apiService.getAllEmployee();

        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<List<Employee>> call, @NonNull Response<List<Employee>> response) {
                if (response.isSuccessful()) {
                    List<Employee> employees = response.body();
                    assert employees != null;
                    for (Employee emp : employees) {
                        Log.d(TAG, "ID: " + emp.getId() + ", Name: "
                                + emp.getName() + ", Designation: " + emp.getDesignation());
                    }
                    DiffUtil.DiffResult result = DiffUtil.calculateDiff(new EmployeeDiffCallback(employeeList, employees));
                    employeeList.clear();
                    employeeList.addAll(employees);
                    // The below code does change the whole content of the RecyclerView which is inefficient
                    // employeeAdapter.notifyDataSetChanged();
                    // Rather use this one
                    result.dispatchUpdatesTo(employeeAdapter);
                } else {
                    Log.e(TAG, "API Response Error: " + response.code());
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Employee>> call, @NonNull Throwable t) {
                Log.e(TAG, "API Call Failed: " + t.getMessage());
            }
        });
    }
}