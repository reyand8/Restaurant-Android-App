package com.example.ecommerce_android_app.activities;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecommerce_android_app.R;
import com.example.ecommerce_android_app.adapters.NavCategoryDetailedAdapter;
import com.example.ecommerce_android_app.models.NavCategoryDetailedModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

public class NavCategoryActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    List<NavCategoryDetailedModel> list;
    NavCategoryDetailedAdapter adapter;
    FirebaseFirestore db;
    ProgressBar progressBar;

    private static final Map<String, String> categoryMap = new HashMap<>();

    static {
        categoryMap.put("Pizza", "Pizza");
        categoryMap.put("Vegetables", "Vegetables");
        categoryMap.put("Burger", "Burger");
        categoryMap.put("Pasta", "Pasta");
        categoryMap.put("Rice", "Rice");
        categoryMap.put("Fish", "Fish");
        categoryMap.put("Salad", "Salad");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nav_category);

        db = FirebaseFirestore.getInstance();
        progressBar = findViewById(R.id.progressbar);
        progressBar.setVisibility(View.VISIBLE);

        String type = getIntent().getStringExtra("type");
        recyclerView = findViewById(R.id.nav_cat_det_rec);
        recyclerView.setVisibility(View.GONE);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        list = new ArrayList<>();
        adapter = new NavCategoryDetailedAdapter(this, list);
        recyclerView.setAdapter(adapter);

        if (type != null) {
            String category = categoryMap.get(type);
            if (category != null) {
                fetchCategoryData(category);
            } else {
                Toast.makeText(this, "Unknown category type: " + type,
                        Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
            }
        } else {
            Toast.makeText(this, "Category type is null",
                    Toast.LENGTH_SHORT).show();
            progressBar.setVisibility(View.GONE);
        }
    }

    private void fetchCategoryData(String category) {
        db.collection("AllProducts")
                .whereEqualTo("type", category)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot documentSnapshot :
                                    task.getResult().getDocuments()) {
                                NavCategoryDetailedModel model =
                                        documentSnapshot.toObject(NavCategoryDetailedModel.class);
                                list.add(model);
                            }
                            adapter.notifyDataSetChanged();
                            progressBar.setVisibility(View.GONE);
                            recyclerView.setVisibility(View.VISIBLE);
                        } else {
                            Toast.makeText(NavCategoryActivity.this,
                                    "Failed to load data. Please try again.",
                                    Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                });
    }
}
