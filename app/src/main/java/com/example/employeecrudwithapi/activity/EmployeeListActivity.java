package com.example.employeecrudwithapi.activity;

import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.employeecrudwithapi.R;
import com.example.employeecrudwithapi.model.Employee;
import com.example.employeecrudwithapi.service.ApiService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class EmployeeListActivity extends AppCompatActivity {

    private static final String TAG = "EmployeeListActivity";

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

        fetchEmployees();
    }

    private void fetchEmployees() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:8081/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiService apiService = retrofit.create(ApiService.class);
        Call<List<Employee>> call = apiService.getAllEmployee();

        call.enqueue(new Callback<List<Employee>>() {
            @Override
            public void onResponse(Call<List<Employee>> call, Response<List<Employee>> response) {
                if (response.isSuccessful()) {
                    List<Employee> employees = response.body();
                    assert employees != null;
                    for (Employee emp : employees) {
                        Log.d(TAG, "ID: " + emp.getId() + ", Name: "
                                + emp.getName() + ", Designation: " + emp.getDesignation());
                    }
                } else {
                    Log.e(TAG, "API Response Error: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<Employee>> call, Throwable t) {
                Log.e(TAG, "API Call Failed: " + t.getMessage());
            }
        });
    }
}