package com.example.employeecrudwithapi.activity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.employeecrudwithapi.R;
import com.example.employeecrudwithapi.adapter.CustomListAdapter;
import com.example.employeecrudwithapi.model.ListItem;

import java.util.ArrayList;
import java.util.List;

public class SearchableListViewActivity extends AppCompatActivity {

    private EditText searchEditText;
    private ListView listView;
    private CustomListAdapter adapter;
    private List<ListItem> originalList;
    private List<ListItem> filteredList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_searchable_list_view);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initViews();
        setupData();
        setupSearch();
    }

    private void initViews() {
        searchEditText = findViewById(R.id.searchEditText);
        listView = findViewById(R.id.listView);
    }

    private void setupData() {
        originalList = new ArrayList<>();
        // Sample data - replace with your actual data
        originalList.add(new ListItem("John Doe", "Software Engineer", R.drawable.ic_launcher_foreground));
        originalList.add(new ListItem("Jane Smith", "UI/UX Designer", R.drawable.ic_launcher_foreground));
        originalList.add(new ListItem("Mike Johnson", "Product Manager", R.drawable.ic_launcher_foreground));
        originalList.add(new ListItem("Sarah Wilson", "Data Scientist", R.drawable.ic_launcher_foreground));
        originalList.add(new ListItem("David Brown", "DevOps Engineer", R.drawable.ic_launcher_foreground));
        originalList.add(new ListItem("Emily Davis", "Marketing Manager", R.drawable.ic_launcher_foreground));
        originalList.add(new ListItem("Chris Miller", "Backend Developer", R.drawable.ic_launcher_foreground));
        originalList.add(new ListItem("Lisa Garcia", "Frontend Developer", R.drawable.ic_launcher_foreground));

        filteredList = new ArrayList<>(originalList);
        adapter = new CustomListAdapter(this, filteredList);
        listView.setAdapter(adapter);
    }

    private void setupSearch() {
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterList(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void filterList(String query) {
        filteredList.clear();
        if (query.isEmpty()) {
            filteredList.addAll(originalList);
        } else {
            for (ListItem item : originalList) {
                if (item.getName().toLowerCase().contains(query.toLowerCase()) ||
                        item.getDescription().toLowerCase().contains(query.toLowerCase())) {
                    filteredList.add(item);
                }
            }
        }
        adapter.notifyDataSetChanged();
    }
}