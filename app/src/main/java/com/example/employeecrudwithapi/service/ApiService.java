package com.example.employeecrudwithapi.service;

import com.example.employeecrudwithapi.model.Employee;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ApiService {

    @POST("employee")
    Call<Employee> saveEmployee(@Body Employee employee);

    @GET("employee")
    Call<List<Employee>> getAllEmployee();

    @GET("employee/{id}")
    Call<Employee> getEmployeeById(@Path("id") int id);

    @PUT("employee/{id}")
    Call<Employee> updateEmployee(@Path("id") int id, @Body Employee employee);

    @DELETE("employee/{id}")
    Call<Void> deleteEmployee(@Path("id") int id);
}