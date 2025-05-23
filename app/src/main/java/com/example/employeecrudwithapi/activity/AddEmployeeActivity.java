package com.example.employeecrudwithapi.activity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.employeecrudwithapi.R;
import com.example.employeecrudwithapi.model.Employee;
import com.example.employeecrudwithapi.service.ApiService;
import com.example.employeecrudwithapi.util.ApiClient;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;

import java.util.Calendar;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AddEmployeeActivity extends AppCompatActivity {

    private TextInputEditText editTextDob;
    private TextInputLayout dateLayout;
    private EditText textName, textEmail, textDesignation, numberAge, multilineAddress, decimalSalary;
    private Button btnSave;
    private ApiService apiService = ApiClient.getApiService();
    private boolean isEditMode = false;
    private int employeeId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_employee);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        // Enable the Up button (back arrow) in the ActionBar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        editTextDob = findViewById(R.id.editTextDob);
        dateLayout = findViewById(R.id.dateLayout);
        textName = findViewById(R.id.textName);
        textEmail = findViewById(R.id.textEmail);
        textDesignation = findViewById(R.id.textDesignation);
        numberAge = findViewById(R.id.numberAge);
        multilineAddress = findViewById(R.id.multilineAddress);
        decimalSalary = findViewById(R.id.decimalSalary);
        btnSave = findViewById(R.id.btnSave);

        Intent intent = getIntent();
        if (getIntent().hasExtra("employee")) {
            Employee employee = new Gson()
                    .fromJson(intent.getStringExtra("employee"), Employee.class);
            employeeId = employee.getId();

            textName.setText(employee.getName());
            textEmail.setText(employee.getEmail());
            textDesignation.setText(employee.getDesignation());
            numberAge.setText(String.valueOf(employee.getAge()));
            multilineAddress.setText(employee.getAddress());
            editTextDob.setText(employee.getDob());
            decimalSalary.setText(String.valueOf(employee.getSalary()));

            btnSave.setText(R.string.update);
            isEditMode = true;
        }

        btnSave.setOnClickListener(v -> saveOrUpdateEmployee());
        dateLayout.setEndIconOnClickListener(v -> showDatePicker());

    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    private void showDatePicker() {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog picker = new DatePickerDialog(
                this,
                (view, year1, month1, day1) -> {
                    String dob = String.format(Locale.US, "%04d-%02d-%02d", year1, month1, day1);
                    editTextDob.setText(dob);
                },
                year,
                month,
                day
        );
        picker.show();
    }

    private void saveOrUpdateEmployee() {
        String name = textName.getText().toString().trim();
        String email = textEmail.getText().toString().trim();
        String designation = textDesignation.getText().toString().trim();
        int age = Integer.parseInt(numberAge.getText().toString().trim());
        String address = multilineAddress.getText().toString().trim();
        assert editTextDob.getText() != null;
        String dobString = editTextDob.getText().toString().trim();
        double salary = Double.parseDouble(decimalSalary.getText().toString().trim());

        Employee employee = new Employee();
        if (isEditMode) {
            employee.setId(employeeId);
        }
        employee.setName(name);
        employee.setEmail(email);
        employee.setDesignation(designation);
        employee.setAge(age);
        employee.setAddress(address);
        employee.setDob(dobString);
        employee.setSalary(salary);

        Call<Employee> call;
        if (isEditMode) {
            call = apiService.updateEmployee(employeeId, employee);
        } else {
            call = apiService.saveEmployee(employee);
        }

        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<Employee> call, @NonNull Response<Employee> response) {
                if (response.isSuccessful()) {
                    String message;
                    if (isEditMode)
                        message = "Employee Updated Successfully!";
                    else
                        message = "Employee Saved Successfully!";

                    Toast.makeText(AddEmployeeActivity.this, message, Toast.LENGTH_SHORT).show();
                    clearForm();
                    Intent intent = new Intent(AddEmployeeActivity.this, EmployeeListActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(AddEmployeeActivity.this, "Failed to save employee "
                            + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<Employee> call, @NonNull Throwable t) {
                Toast.makeText(AddEmployeeActivity.this, "Error: " + t.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void clearForm() {
        textName.setText("");
        textEmail.setText("");
        textDesignation.setText("");
        numberAge.setText("");
        multilineAddress.setText("");
        editTextDob.setText("");
        decimalSalary.setText("");
    }
}