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
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

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
    private ApiService apiService;

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

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:8081/") // for emulator
                // .baseUrl("http://172.28.64.1:8081/") // Give you computers IP
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        apiService = retrofit.create(ApiService.class);

        dateLayout.setEndIconOnClickListener(v -> showDatePicker());

        btnSave.setOnClickListener(v -> saveEmployee());
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

        DatePickerDialog picker = new DatePickerDialog(this,
                (view, year1, month1, day1) -> {
                    String dob = String.format(Locale.US, "%04d-%02d-%02d", year1, month1, day1);
                    editTextDob.setText(dob);
                },
                year, month, day);
        picker.show();
    }

    private void saveEmployee() {
        String name = textName.getText().toString().trim();
        String email = textEmail.getText().toString().trim();
        String designation = textDesignation.getText().toString().trim();
        int age = Integer.parseInt(numberAge.getText().toString().trim());
        String address = multilineAddress.getText().toString().trim();
        assert editTextDob.getText() != null;
        String dobString = editTextDob.getText().toString().trim();
        double salary = Double.parseDouble(decimalSalary.getText().toString().trim());

        // Create Employee object
        Employee employee = new Employee();
        employee.setName(name);
        employee.setEmail(email);
        employee.setDesignation(designation);
        employee.setAge(age);
        employee.setAddress(address);
        employee.setDob(dobString);
        employee.setSalary(salary);

        // Make API call
        Call<Employee> call = apiService.saveEmployee(employee);
        String string = call.toString();
        System.out.println(string);
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<Employee> call, @NonNull Response<Employee> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(AddEmployeeActivity.this, "Employee saved successfully!",
                            Toast.LENGTH_SHORT).show();
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